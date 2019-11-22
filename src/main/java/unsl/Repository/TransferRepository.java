package unsl.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import unsl.Entities.Transfer;

public interface TransferRepository extends JpaRepository<Transfer, Long> {
}
