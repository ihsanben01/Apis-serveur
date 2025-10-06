package prototypeapis.repository;

import prototypeapis.model.Deposit;
import prototypeapis.model.DepositStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DepositRepository extends JpaRepository<Deposit, UUID> {

    Optional<Deposit> findByIdAndStatus(UUID id, DepositStatus status);

    List<Deposit> findByStatus(DepositStatus status);

    @Query("SELECT d FROM Deposit d WHERE d.expirationDate < :date AND d.status = :status")
    List<Deposit> findByExpirationDateBeforeAndStatus(LocalDateTime date, DepositStatus status);

    @Query("SELECT d FROM Deposit d WHERE d.deletionDate IS NOT NULL AND d.deletionDate < :date")
    List<Deposit> findDepositsReadyForDeletion(LocalDateTime date);
}