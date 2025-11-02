package prototypeapis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;

import java.net.URI;

@Configuration
public class S3Config {

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .endpointOverride(URI.create("http://127.0.0.1:3900"))
                .region(Region.of("garage"))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create("GKb4e3223e3128d3992e64cf5c", "c59a380264a4d8018fcba46f09c1c3099e273a9d5d9ecdb6141725c14f2648d3")
                ))
                .serviceConfiguration(S3Configuration.builder()
                        .pathStyleAccessEnabled(true) // important pour Garage
                        .build())
                .build();
    }
}

