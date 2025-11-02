package prototypeapis.service;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.core.sync.ResponseTransformer;

import java.io.IOException;

@Service
public class GarageStorageService {

    private final S3Client s3;
    private static final String BUCKET = "encrypted-files";

    public GarageStorageService(S3Client s3) {
        this.s3 = s3;
    }

    public void uploadFile(MultipartFile file) throws IOException {
        s3.putObject(
                PutObjectRequest.builder()
                        .bucket(BUCKET)
                        .key(file.getOriginalFilename())
                        .build(),
                RequestBody.fromBytes(file.getBytes())
        );
    }

    public byte[] downloadFile(String key) throws IOException {
        return s3.getObject(
                GetObjectRequest.builder()
                        .bucket(BUCKET)
                        .key(key)
                        .build(),
                ResponseTransformer.toBytes()
        ).asByteArray();
    }
}
