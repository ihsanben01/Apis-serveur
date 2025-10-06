package com.apis.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateDepositRequest {

    @NotBlank(message = "Le nom du fichier est obligatoire")
    private String fileName;

    @NotNull(message = "La taille du fichier est obligatoire")
    private Long fileSize;

    private String mimeType;

    // Getters et Setters
    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public Long getFileSize() { return fileSize; }
    public void setFileSize(Long fileSize) { this.fileSize = fileSize; }

    public String getMimeType() { return mimeType; }
    public void setMimeType(String mimeType) { this.mimeType = mimeType; }
}