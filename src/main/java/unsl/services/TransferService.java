package unsl.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import unsl.entities.Balance;
import unsl.entities.Transfer;
import unsl.repository.TransferRepository;
import unsl.utils.RestService;

@Service
public class TransferService {

    @Autowired
    RestService restService;

    @Autowired
    TransferRepository transferRepository;

    public Object getTransfer(Long id) {

        return transferRepository.findById(id);
    }

    public Object searchTransfer(Long userId) {

        return transferRepository.findAllByCollectorIdOrPayerId(userId, userId);
    }

    public Object saveTransfer(Transfer transfer) {

        return transferRepository.save(transfer);
    }
}
