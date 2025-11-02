//package prototypeapis.service;
//
//import prototypeapis.model.EncryptedFile;
//import prototypeapis.repository.EncryptedFileRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.util.Optional;
//import java.util.UUID;
//
//@Service
//public class FileStorageService {
//
//    private final EncryptedFileRepository encryptedFileRepository;
//
//    @Autowired
//    public FileStorageService(EncryptedFileRepository encryptedFileRepository) {
//        this.encryptedFileRepository = encryptedFileRepository;
//    }
//
//    /**
//     * Stocke un fichier chiffré
//     */
//    public String storeEncryptedFile(UUID depositId, MultipartFile file) throws IOException {
//        // Vérifier si un fichier existe déjà pour ce dépôt
//        encryptedFileRepository.findByDepositId(depositId).ifPresent(existingFile -> {
//            encryptedFileRepository.delete(existingFile);
//        });
//
//        // ✅ CORRECTION : Utiliser le bon constructeur
//        EncryptedFile encryptedFile = new EncryptedFile();
//        encryptedFile.setDepositId(depositId);
//        encryptedFile.setEncryptedData(file.getBytes());
//        // Pas besoin de originalSize dans le modèle Zero Trust
//
//        EncryptedFile savedFile = encryptedFileRepository.save(encryptedFile);
//        return savedFile.getId();
//    }
//
//    /**
//     * Récupère un fichier chiffré
//     */
//    public Optional<EncryptedFile> getEncryptedFile(UUID depositId) {
//        return encryptedFileRepository.findByDepositId(depositId);
//    }
//
//    /**
//     * Supprime un fichier chiffré
//     */
//    public boolean deleteEncryptedFile(UUID depositId) {
//        if (encryptedFileRepository.existsByDepositId(depositId)) {
//            encryptedFileRepository.deleteByDepositId(depositId);
//            return true;
//        }
//        return false;
//    }
//
//    /**
//     * Vérifie si un fichier existe pour un dépôt
//     */
//    public boolean fileExists(UUID depositId) {
//        return encryptedFileRepository.existsByDepositId(depositId);
//    }
//
//    /**
//     * Récupère la taille du fichier chiffré
//     */
//    public Optional<Long> getFileSize(UUID depositId) {
//        return encryptedFileRepository.findByDepositId(depositId)
//                .map(file -> (long) file.getEncryptedData().length);
//    }
//}