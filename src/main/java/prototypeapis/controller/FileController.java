package prototypeapis.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import prototypeapis.service.StorageService;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {
    private final StorageService storageService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) throws Exception {
        String path = storageService.saveFile(file);
        String qrData = "download-link?id=123"; // simulé
        QrCodeGenerator.generateQRCode(qrData, "uploads/qr_share.png");
        return ResponseEntity.ok("Fichier uploadé avec QR code généré !");
    }


}
