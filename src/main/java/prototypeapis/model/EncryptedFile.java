package prototypeapis.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class EncryptedFile {

    private UUID depositId;
    private byte[] encryptedData;  // ✅ The encrypted file bytes stored in Garage
    private LocalDateTime uploadDate;

    public EncryptedFile() {
        this.uploadDate = LocalDateTime.now();
    }

    public EncryptedFile(UUID depositId, byte[] encryptedData) {
        this();
        this.depositId = depositId;
        this.encryptedData = encryptedData;
    }

    // Getters & Setters
    public UUID getDepositId() { return depositId; }
    public void setDepositId(UUID depositId) { this.depositId = depositId; }

    public byte[] getEncryptedData() { return encryptedData; }
    public void setEncryptedData(byte[] encryptedData) { this.encryptedData = encryptedData; }

    public LocalDateTime getUploadDate() { return uploadDate; }
    public void setUploadDate(LocalDateTime uploadDate) { this.uploadDate = uploadDate; }
}
