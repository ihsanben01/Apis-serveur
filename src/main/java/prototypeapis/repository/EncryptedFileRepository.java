package prototypeapis.repository;

import prototypeapis.model.EncryptedFile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EncryptedFileRepository extends MongoRepository<EncryptedFile, String> {

    Optional<EncryptedFile> findByDepositId(UUID depositId);

    void deleteByDepositId(UUID depositId);

    boolean existsByDepositId(UUID depositId);
}