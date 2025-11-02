package prototypeapis.controller;

import prototypeapis.dto.FileUploadResponse;
import prototypeapis.model.Deposit;
import prototypeapis.model.EncryptedFile;
import prototypeapis.service.DepositService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import prototypeapis.service.GarageStorageService;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/v1/files")
@CrossOrigin(origins = "*") // À configurer proprement en production
public class FileController {

    private final GarageStorageService fileStorageService;
    private final DepositService depositService;

    @Autowired
    public FileController(GarageStorageService fileStorageService,
                          DepositService depositService) {
        this.fileStorageService = fileStorageService;
        this.depositService = depositService;
    }

    /**
     * Upload un fichier chiffré pour un dépôt
     */
    @PostMapping("/{depositId}")
    public ResponseEntity<FileUploadResponse> uploadEncryptedFile(
            @PathVariable UUID depositId,
            @RequestParam("file") MultipartFile file) {

        try {
            // Vérifier que le dépôt existe et est actif
            Optional<Deposit> depositOpt = depositService.getDepositInfo(depositId)
                    .map(response ->    new Deposit()); // Simplification

            if (depositOpt.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new FileUploadResponse(false, "Dépôt non trouvé ou inactif"));
            }

            // Stocker le fichier chiffré
             fileStorageService.uploadFile( file);

            FileUploadResponse response = new FileUploadResponse(
                    true,
                    "Fichier uploadé avec succès",
                    depositId.toString()          );

            return ResponseEntity.ok(response);

        } catch (IOException e) {
            return ResponseEntity.internalServerError()
                    .body(new FileUploadResponse(false, "Erreur lors de l'upload du fichier"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new FileUploadResponse(false, e.getMessage()));
        }
    }

    /**
     * Télécharge un fichier chiffré via token de partage
     */
    @GetMapping("/download/{shareToken}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String shareToken) {
        try {
            // Récupérer le dépôt via le token de partage
            Optional<Deposit> depositOpt = depositService.getDepositByShareToken(shareToken);

            if (depositOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Deposit deposit = depositOpt.get();
            byte[] fileBytes = fileStorageService.downloadFile("encrypted_"+deposit.getFileName());
            if (fileBytes == null || fileBytes.length == 0) {
                return ResponseEntity.notFound().build();
            }
            EncryptedFile encryptedFile = new EncryptedFile();
            // Incrémenter le compteur de téléchargements
            depositService.incrementDownloadCount(deposit.getId());

            // Préparer la réponse
            ByteArrayResource resource = new ByteArrayResource(fileBytes  );


            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"encrypted_" + deposit.getFileName() + "\"")
                    .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(fileBytes.length))
                    .body(resource);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

//    /**
//     * Récupère les informations d'un fichier
//     */
//    @GetMapping("/{depositId}/info")
//    public ResponseEntity<?> getFileInfo(@PathVariable UUID depositId) {
//        boolean exists = fileStorageService.fileExists(depositId);
//
//        if (exists) {
//            return ResponseEntity.ok().body("{\"exists\": true}");
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }
}