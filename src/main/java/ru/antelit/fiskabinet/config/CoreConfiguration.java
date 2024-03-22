package ru.antelit.fiskabinet.config;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.format.DateTimeFormatter;

@Slf4j
@Configuration
public class CoreConfiguration {

    @Value(" ${minio.accesskey}")
    private String accessKey;

    @Value("${minio.secretkey}")
    private String secretKey;

    @Value("${minio.url}")
    private String url;

    @Value("${minio.port}")
    private Integer port;

    @Value("${minio.bucket}")
    String bucket;

    @Bean
    public MinioClient minioClient() throws ServerException, InsufficientDataException, ErrorResponseException,
            IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException,
            InternalException {
        MinioClient minioClient = MinioClient.builder()
                .credentials(accessKey, secretKey)
                .endpoint(url, port, false)
                .build();
        if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build())) {
            log.warn("Specified bucket '{}' doesn't exist", bucket);
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
            log.info("Bucket '{}' created", bucket);
        }
        return minioClient;
    }

    @Bean
    public DateTimeFormatter dateFormatter() {
        return DateTimeFormatter.ofPattern("dd.MM.yyyy");
    }

    @Bean
    public DateTimeFormatter dateReportFormatter() {
        return DateTimeFormatter.ofPattern("ddMMyyyy");
    }

    @Bean
    public DateTimeFormatter timeFormatter() {
        return DateTimeFormatter.ofPattern("HH:mm");
    }

    @Bean DateTimeFormatter timeReportFormatter() {
        return DateTimeFormatter.ofPattern("HHmm");
    }
}
