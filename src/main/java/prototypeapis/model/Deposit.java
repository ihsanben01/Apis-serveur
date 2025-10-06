package com.apis.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "deposits")
public class Deposit {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank
    @Column(name = "file_name", nullable = false)
    private String fileName;

    @NotNull
    @Column(name = "file_size", nullable = false)
    private Long fileSize;

    @Column(name = "mime_type")
    private String mimeType;

    @NotNull
    @Column(name = "upload_date", nullable = false)
    private LocalDateTime uploadDate;

    @NotNull
    @Column(name = "expiration_date", nullable = false)
    private LocalDateTime expirationDate;

    @Column(name = "deletion_date")
    private LocalDateTime deletionDate;

    @Column(name = "download_count")
    private Integer downloadCount = 0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DepositStatus status = DepositStatus.ACTIVE;


    // Constructeurs
    public Deposit() {
        this.uploadDate = LocalDateTime.now();
        this.expirationDate = LocalDateTime.now().plusDays(7);
        this.downloadCount = 0;
        this.status = DepositStatus.ACTIVE;
    }

    public Deposit(String fileName, Long fileSize, String mimeType) {
        this();
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.mimeType = mimeType;
    }

    // Getters et Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public Long getFileSize() { return fileSize; }
    public void setFileSize(Long fileSize) { this.fileSize = fileSize; }

    public String getMimeType() { return mimeType; }
    public void setMimeType(String mimeType) { this.mimeType = mimeType; }

    public LocalDateTime getUploadDate() { return uploadDate; }
    public void setUploadDate(LocalDateTime uploadDate) { this.uploadDate = uploadDate; }

    public LocalDateTime getExpirationDate() { return expirationDate; }
    public void setExpirationDate(LocalDateTime expirationDate) { this.expirationDate = expirationDate; }

    public LocalDateTime getDeletionDate() { return deletionDate; }
    public void setDeletionDate(LocalDateTime deletionDate) { this.deletionDate = deletionDate; }

    public Integer getDownloadCount() { return downloadCount; }
    public void setDownloadCount(Integer downloadCount) { this.downloadCount = downloadCount; }

    public DepositStatus getStatus() { return status; }
    public void setStatus(DepositStatus status) { this.status = status; }
}

enum DepositStatus {
    ACTIVE, EXPIRED, DELETED
}