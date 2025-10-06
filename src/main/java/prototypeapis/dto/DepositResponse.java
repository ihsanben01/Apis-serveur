package prototypeapis.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class DepositResponse {

    private UUID id;
    private String fileName;
    private Long fileSize;
    private String mimeType;
    private LocalDateTime uploadDate;
    private LocalDateTime expirationDate;
    private String shareToken;
    private String manageToken;

    // Constructeurs
    public DepositResponse() {}

    public DepositResponse(UUID id, String fileName, Long fileSize, String mimeType,
                           LocalDateTime uploadDate, LocalDateTime expirationDate) {
        this.id = id;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.mimeType = mimeType;
        this.uploadDate = uploadDate;
        this.expirationDate = expirationDate;
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

    public String getShareToken() { return shareToken; }
    public void setShareToken(String shareToken) { this.shareToken = shareToken; }

    public String getManageToken() { return manageToken; }
    public void setManageToken(String manageToken) { this.manageToken = manageToken; }
}