package prototypeapis.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class StorageService {
    private final Path storageLocation = Paths.get("uploads");

    public String saveFile(MultipartFile file) throws IOException {
        Files.createDirectories(storageLocation);
        Path filePath = storageLocation.resolve(UUID.randomUUID() + "_" + file.getOriginalFilename());
        Files.copy(file.getInputStream(), filePath);
        return filePath.toString();
    }
}

