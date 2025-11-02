package prototypeapis.service;

import prototypeapis.model.Deposit;
import prototypeapis.model.DepositStatus;
import prototypeapis.repository.DepositRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class CleanupService {

    private static final Logger logger = LoggerFactory.getLogger(CleanupService.class);

    private final DepositRepository depositRepository;
    private final GarageStorageService fileStorageService;

    @Autowired
    public CleanupService(DepositRepository depositRepository,
                          GarageStorageService fileStorageService) {
        this.depositRepository = depositRepository;
        this.fileStorageService = fileStorageService;
    }

    /**
     * Nettoie les dépôts expirés tous les jours à 2h du matin
     */
    @Scheduled(cron = "0 0 2 * * ?") // Tous les jours à 2h
    public void cleanupExpiredDeposits() {
        logger.info("Début du nettoyage des dépôts expirés");

        List<Deposit> expiredDeposits = depositRepository
                .findByExpirationDateBeforeAndStatus(LocalDateTime.now(), DepositStatus.ACTIVE);

        int cleanedCount = 0;

        for (Deposit deposit : expiredDeposits) {
            try {
                // Marquer le dépôt comme expiré
                deposit.setStatus(DepositStatus.EXPIRED);
                deposit.setDeletionDate(LocalDateTime.now());
                depositRepository.save(deposit);

                // Supprimer le fichier chiffré
//                fileStorageService.deleteEncryptedFile(deposit.getId());

                cleanedCount++;
                logger.info("Dépôt expiré nettoyé: {}", deposit.getId());

            } catch (Exception e) {
                logger.error("Erreur lors du nettoyage du dépôt {}: {}", deposit.getId(), e.getMessage());
            }
        }

        logger.info("Nettoyage terminé: {} dépôts nettoyés", cleanedCount);
    }

    /**
     * Nettoie les dépôts marqués pour suppression (toutes les heures)
     */
    @Scheduled(cron = "0 0 * * * ?") // Toutes les heures
    public void cleanupDeletedDeposits() {
        logger.info("Début du nettoyage des dépôts supprimés");

        List<Deposit> deletedDeposits = depositRepository
                .findDepositsReadyForDeletion(LocalDateTime.now().minusDays(1)); // Supprimés il y a plus de 24h

        int cleanedCount = 0;

        for (Deposit deposit : deletedDeposits) {
            try {
                // Supprimer définitivement le dépôt
                depositRepository.delete(deposit);
                cleanedCount++;
                logger.info("Dépôt supprimé définitivement: {}", deposit.getId());

            } catch (Exception e) {
                logger.error("Erreur lors de la suppression définitive du dépôt {}: {}",
                        deposit.getId(), e.getMessage());
            }
        }

        logger.info("Suppression définitive terminée: {} dépôts supprimés", cleanedCount);
    }

    /**
     * Méthode manuelle pour forcer le nettoyage
     */
    public void forceCleanup() {
        cleanupExpiredDeposits();
        cleanupDeletedDeposits();
    }
}