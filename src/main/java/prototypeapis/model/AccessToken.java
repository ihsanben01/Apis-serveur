package prototypeapis.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "access_tokens")
public class AccessToken {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deposit_id", nullable = false)
    private Deposit deposit;

    @Enumerated(EnumType.STRING)
    @Column(name = "token_type", nullable = false)
    private TokenType tokenType;

    @Column(name = "token_hash", nullable = false, length = 255)
    private String tokenHash;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @Column(name = "expires_date", nullable = false)
    private LocalDateTime expiresDate;

    // Constructeurs
    public AccessToken() {
        this.createdDate = LocalDateTime.now();
    }

    public AccessToken(Deposit deposit, TokenType tokenType, String tokenHash) {
        this();
        this.deposit = deposit;
        this.tokenType = tokenType;
        this.tokenHash = tokenHash;
        this.expiresDate = LocalDateTime.now().plusDays(30); // Tokens valides 30 jours
    }

    // Getters et Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public Deposit getDeposit() { return deposit; }
    public void setDeposit(Deposit deposit) { this.deposit = deposit; }

    public TokenType getTokenType() { return tokenType; }
    public void setTokenType(TokenType tokenType) { this.tokenType = tokenType; }

    public String getTokenHash() { return tokenHash; }
    public void setTokenHash(String tokenHash) { this.tokenHash = tokenHash; }

    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }

    public LocalDateTime getExpiresDate() { return expiresDate; }
    public void setExpiresDate(LocalDateTime expiresDate) { this.expiresDate = expiresDate; }
}
