package com.apis.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.UUID;

@Document(collection = "encrypted_files")
public class EncryptedFile {

    @Id
    private String id;

    private UUID depositId;

    private byte[] encryptedData; // ✅ Seulement les données chiffrées (le backend ne peut pas les lire)

    private LocalDateTime uploadDate;


    // Constructeurs
    public EncryptedFile() {
        this.uploadDate = LocalDateTime.now();
    }

    public EncryptedFile(UUID depositId, byte[] encryptedData) {
        this();
        this.depositId = depositId;
        this.encryptedData = encryptedData;
    }

    // Getters et Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public UUID getDepositId() { return depositId; }
    public void setDepositId(UUID depositId) { this.depositId = depositId; }

    public byte[] getEncryptedData() { return encryptedData; }
    public void setEncryptedData(byte[] encryptedData) { this.encryptedData = encryptedData; }

    public LocalDateTime getUploadDate() { return uploadDate; }
    public void setUploadDate(LocalDateTime uploadDate) { this.uploadDate = uploadDate; }
}