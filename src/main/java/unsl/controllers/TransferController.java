package unsl.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unsl.entities.*;
import unsl.services.TransferService;
import unsl.utils.RestService;


@RestController
public class TransferController {

    @Autowired
    TransferService transferService;

    @Autowired
    RestService restService;

    @GetMapping(value = "/transfers/{transferId}")
    @ResponseBody
    public Object getTransfer(@PathVariable("transferId") Long transferId) {

        return transferService.getTransfer(transferId);
    }

    @GetMapping(value = "/transfers/search")
    @ResponseBody
    public Object serchTransfer(@RequestParam("user_id") Long userId) {

        return transferService.searchTransfer(userId);
    }

    @PostMapping(value = "/transfers")
    @ResponseBody
    public Object getTransfer(@RequestBody Transfer transfer) throws Exception {

        /* 1. los usurios tienen que existir */
        User collector = restService.getUser(String.format("http://localhost:8888/users/%d", transfer.getCollectorId()));
        User payer = restService.getUser(String.format("http://localhost:8888/users/%d", transfer.getPayerId()));

        /* 2. no se puede transferir a el mismo */
        if ( collector.getId() == payer.getId() ) {
            return new ResponseEntity(new ResponseError(400, "Collector and payer can't be the same person"),
                    HttpStatus.NOT_FOUND);
        }

        /* 3. tienen que tener un balance creado los usuarios */
        Balance balanceCollector = restService.getBalance(String.format("http://localhost:8889/balance/search?user_id=%d", transfer.getCollectorId()));
        Balance balancePayer = restService.getBalance(String.format("http://localhost:8889/balance/search?user_id=%d", transfer.getPayerId()));

        /* 4. la moneda tiene que ser la misma */
        if ( !balanceCollector.getCurrency().equals(balancePayer.getCurrency()) ) {
            return new ResponseEntity(new ResponseError(400, "Collector and payer must have the same currency"),
                    HttpStatus.NOT_FOUND);
        }

        /* 5. tiene que tener dinero suficiente para transferir*/
        if ( transfer.getAmount().compareTo(balancePayer.getAmount())  > 0 ) {
            return new ResponseEntity(new ResponseError(400, "Insufficient money to transfer"),
                    HttpStatus.NOT_FOUND);
        }

        /* 6. tengo que descontar del balance del payer y sumar al collector*/
        Amount collectorAmount = new Amount(balanceCollector.getAmount().add(transfer.getAmount()));
        Amount payerAmount = new Amount(balancePayer.getAmount().subtract(transfer.getAmount()));

        restService.putBalance(String.format("http://localhost:8889/balance/%d",balanceCollector.getId()), collectorAmount);
        restService.putBalance(String.format("http://localhost:8889/balance/%d",balancePayer.getId()), payerAmount);


        return transferService.saveTransfer(transfer);
    }

}