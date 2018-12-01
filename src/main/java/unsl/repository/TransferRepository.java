package unsl.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import unsl.entities.Balance;
import unsl.entities.Transfer;

import java.util.List;

public interface TransferRepository extends CrudRepository<Transfer, Long> {


	/**
	 * @param collectorId
	 * @param payerId
	 * @return
	 */
	List<Transfer> findAllByCollectorIdOrPayerId(@Param("collectorId") Long collectorId, @Param("payerId") Long payerId);
}
