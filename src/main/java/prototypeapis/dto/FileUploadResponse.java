package prototypeapis.dto;


public class FileUploadResponse {

    private boolean success;
    private String message;
    private String fileId;

    // Constructeurs
    public FileUploadResponse() {}

    public FileUploadResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public FileUploadResponse(boolean success, String message, String fileId) {
        this(success, message);
        this.fileId = fileId;
    }

    // Getters et Setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getFileId() { return fileId; }
    public void setFileId(String fileId) { this.fileId = fileId; }
}
