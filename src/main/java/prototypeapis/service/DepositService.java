package prototypeapis.service;

import prototypeapis.dto.CreateDepositRequest;
import prototypeapis.dto.DepositResponse;
import prototypeapis.model.AccessToken;
import prototypeapis.model.Deposit;
import prototypeapis.model.DepositStatus;
import prototypeapis.model.TokenType;
import prototypeapis.repository.DepositRepository;
import prototypeapis.repository.AccessTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class DepositService {

    private final DepositRepository depositRepository;
    private final AccessTokenRepository accessTokenRepository;
    private final SecureRandom random = new SecureRandom();

    @Autowired
    public DepositService(DepositRepository depositRepository,
                          AccessTokenRepository accessTokenRepository) {
        this.depositRepository = depositRepository;
        this.accessTokenRepository = accessTokenRepository;
    }

    /**
     * Crée un nouveau dépôt avec des tokens d'accès
     */
    public DepositResponse createDeposit(CreateDepositRequest request) {
        // Créer le dépôt
        Deposit deposit = new Deposit(
                request.getFileName(),
                request.getFileSize(),
                request.getMimeType()
        );

        deposit = depositRepository.save(deposit);

        // Générer les tokens
        String shareToken = generateSecureToken();
        String manageToken = generateSecureToken();

        // Hasher les tokens pour la sécurité
        String shareTokenHash = hashToken(shareToken);
        String manageTokenHash = hashToken(manageToken);

        // Sauvegarder les tokens
        AccessToken shareAccessToken = new AccessToken(deposit, TokenType.SHARE, shareTokenHash);
        AccessToken manageAccessToken = new AccessToken(deposit, TokenType.MANAGE, manageTokenHash);

        accessTokenRepository.save(shareAccessToken);
        accessTokenRepository.save(manageAccessToken);

        // Construire la réponse
        DepositResponse response = new DepositResponse(
                deposit.getId(),
                deposit.getFileName(),
                deposit.getFileSize(),
                deposit.getMimeType(),
                deposit.getUploadDate(),
                deposit.getExpirationDate()
        );
        response.setShareToken(shareToken);
        response.setManageToken(manageToken);

        return response;
    }

    /**
     * Récupère les informations d'un dépôt
     */
    public Optional<DepositResponse> getDepositInfo(UUID depositId) {
        return depositRepository.findByIdAndStatus(depositId, DepositStatus.ACTIVE)
                .map(deposit -> {
                    DepositResponse response = new DepositResponse(
                            deposit.getId(),
                            deposit.getFileName(),
                            deposit.getFileSize(),
                            deposit.getMimeType(),
                            deposit.getUploadDate(),
                            deposit.getExpirationDate()
                    );
                    return response;
                });
    }

    /**
     * Renouvelle la durée de vie d'un dépôt
     */
    public boolean renewDeposit(UUID depositId) {
        Optional<Deposit> depositOpt = depositRepository.findByIdAndStatus(depositId, DepositStatus.ACTIVE);

        if (depositOpt.isPresent()) {
            Deposit deposit = depositOpt.get();
            deposit.setExpirationDate(LocalDateTime.now().plusDays(7));
            depositRepository.save(deposit);
            return true;
        }

        return false;
    }

    /**
     * Supprime un dépôt (soft delete)
     */
    public boolean deleteDeposit(UUID depositId) {
        Optional<Deposit> depositOpt = depositRepository.findById(depositId);

        if (depositOpt.isPresent()) {
            Deposit deposit = depositOpt.get();
            deposit.setStatus(DepositStatus.DELETED);
            deposit.setDeletionDate(LocalDateTime.now());
            depositRepository.save(deposit);

            // Supprimer les tokens associés
            accessTokenRepository.deleteByDeposit(deposit);

            return true;
        }

        return false;
    }

    /**
     * Incrémente le compteur de téléchargements
     */
    public void incrementDownloadCount(UUID depositId) {
        depositRepository.findById(depositId).ifPresent(deposit -> {
            deposit.setDownloadCount(deposit.getDownloadCount() + 1);
            depositRepository.save(deposit);
        });
    }

    /**
     * Récupère les dépôts expirés
     */
    public List<Deposit> getExpiredDeposits() {
        return depositRepository.findByExpirationDateBeforeAndStatus(
                LocalDateTime.now(), DepositStatus.ACTIVE);
    }

    /**
     * Génère un token sécurisé
     */
    private String generateSecureToken() {
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    /**
     * Hash un token pour le stockage (simplifié)
     */
    private String hashToken(String token) {
        // Dans un vrai projet, utiliser BCrypt ou Argon2
        return Base64.getEncoder().encodeToString(token.getBytes());
    }

    /**
     * Vérifie un token
     */
    public boolean verifyToken(String token, TokenType tokenType) {
        String tokenHash = hashToken(token);
        return accessTokenRepository.findByTokenHashAndTokenType(tokenHash, tokenType)
                .map(accessToken -> {
                    // Vérifier que le token n'est pas expiré
                    return accessToken.getExpiresDate().isAfter(LocalDateTime.now());
                })
                .orElse(false);
    }

    /**
     * Récupère un dépôt par son token de partage
     */
    public Optional<Deposit> getDepositByShareToken(String shareToken) {
        String tokenHash = hashToken(shareToken);
        return accessTokenRepository.findByTokenHashAndTokenType(tokenHash, TokenType.SHARE)
                .map(AccessToken::getDeposit)
                .filter(deposit -> deposit.getStatus() == DepositStatus.ACTIVE)
                .filter(deposit -> deposit.getExpirationDate().isAfter(LocalDateTime.now()));
    }

    /**
     * Récupère un dépôt par son token de gestion
     */
    public Optional<Deposit> getDepositByManageToken(String manageToken) {
        String tokenHash = hashToken(manageToken);
        return accessTokenRepository.findByTokenHashAndTokenType(tokenHash, TokenType.MANAGE)
                .map(AccessToken::getDeposit);
    }
}