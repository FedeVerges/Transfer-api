package unsl.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import unsl.Repository.TransferRepository;
import unsl.entities.Transfer;

import java.util.List;
@Service
public class TransferService {
    @Autowired
    TransferRepository transferRepository;

    public List<Transfer> getAll(){ return transferRepository.findAll();}

    public Transfer getTransfer(long transferId) {return transferRepository.findById(transferId).orElse(null);}

    public Transfer saveTranfer(Transfer transfer){
        return transferRepository.save(transfer);
    }

    public Transfer updateTranfer(Transfer updatedTransfer){
        Transfer transfer = transferRepository.findById(updatedTransfer.getId()).orElse(null);
        if (transfer == null){
            return null;
        }
        transfer.setStatus(updatedTransfer.getStatus());
        return transferRepository.save(transfer);
    }
    public Transfer deleteTransfer(Long transferId){
        Transfer transfer = transferRepository.findById(transferId).orElse(null);
        if (transfer == null) {
            return null;
        }
        transfer.setStatus(Transfer.Status.CANCELADA);
        return transferRepository.save(transfer);
    }

}
