package unsl.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sun.rmi.transport.ObjectTable;
import unsl.Entities.Account;
import unsl.Entities.ResponseError;
import unsl.Entities.Transfer;
import unsl.Entities.User;
import unsl.Services.TransferService;

import java.util.List;

@RestController
public class TransferController {
    @Autowired
    TransferService transferService;

    @GetMapping(value = "/transferencias")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public List<Transfer> getAll() { return transferService.getAll();}



    @GetMapping(value = "/transferencias/{transferId}")
    public Object getTransfer(@PathVariable("transferId") Long transferId){
        Transfer transfer = transferService.getTransfer(transferId);
        if(transfer == null){
            return new ResponseEntity(new ResponseError(404,String.format("Transfer Not Found", transferId)), HttpStatus.NOT_FOUND);
        }
        return transfer;
    }
    @PostMapping(value = "/transferencias")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public Object createTransfer(@RequestBody Transfer transfer) {
        try {
            Account rootAccount = transferService.getAccount(transfer.getId_cuenta_origen());
            User.Status rootClientStatus = transferService.getUserStatus(rootAccount.getHolder());
            Account destinationAccount = transferService.getAccount(transfer.getId_cuenta_destino());
            User.Status destinationClientStatus = transferService.getUserStatus(destinationAccount.getHolder());
            double balanceRootAccount;

            if(rootAccount == null || destinationAccount == null) {
                return new ResponseEntity<>(new ResponseError(404, "root account or destination account doesn't exist."), HttpStatus.NOT_FOUND);
            }
            if(rootAccount.getAccount_balance() < transfer.getMonto()) {
                return new ResponseEntity<>(new ResponseError(404, "Insuficient Balance."), HttpStatus.NOT_FOUND);

            }
            if(rootAccount.getStatus() == Account.Status.BAJA || destinationAccount.getStatus() == Account.Status.BAJA){
                return  new ResponseEntity<>(new ResponseError(404,"Some accounts are dicharged."), HttpStatus.BAD_REQUEST);
            }
            if(rootAccount.getCurrency() != destinationAccount.getCurrency()){
                return  new ResponseEntity<>(new ResponseError(404,"Currency of both accounts are not the same."), HttpStatus.BAD_REQUEST);
            }
            if(rootClientStatus == User.Status.BAJA || destinationClientStatus == User.Status.BAJA){
                return  new ResponseEntity<>(new ResponseError(404,"some clients are discharged"), HttpStatus.BAD_REQUEST);
            }
            balanceRootAccount = rootAccount.getAccount_balance() - transfer.getMonto();
            rootAccount.setAccount_balance(balanceRootAccount);

            transferService.updateAccount(rootAccount);
            transfer.setStatus(Transfer.Status.PENDIENTE);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return transferService.saveTranfer(transfer);
    }

    @PutMapping(value = "/transferencias/{idTransferencias}")
    @ResponseBody
    public Object updateTransfer (@RequestBody Transfer transfer, @PathVariable("idTransferencias") Long idTransferencias) throws Exception {
        Transfer pendingTransfer = transferService.getTransfer(idTransferencias);
        if (pendingTransfer == null){
            return new ResponseEntity<>(new ResponseError(404, "Transfer doesn't exist"), HttpStatus.NOT_FOUND);
        }
        if(pendingTransfer.getStatus() == Transfer.Status.PENDIENTE) {
            try {
                Account destinationAccount = transferService.getAccount(pendingTransfer.getId_cuenta_destino());
                destinationAccount.setAccount_balance(destinationAccount.getAccount_balance() + pendingTransfer.getMonto());
                transferService.updateAccount(destinationAccount);
                pendingTransfer.setStatus(Transfer.Status.PROCESADA);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if (pendingTransfer.getStatus() == Transfer.Status.CANCELADA){
            return new ResponseEntity<>(new ResponseError(404, "Transfer is canceled"), HttpStatus.BAD_REQUEST);
        }else if (pendingTransfer.getStatus() == Transfer.Status.PROCESADA){
            return new ResponseEntity<>(new ResponseError(404, "Transfer is processed"), HttpStatus.BAD_REQUEST);
        }



            return transferService.updateTranfer(pendingTransfer);
    }



}
