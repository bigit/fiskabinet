package ru.antelit.fiskabinet.service;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.UploadObjectArgs;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.MinioException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
@Log4j2
public class MinioService {

    @Autowired
    private MinioClient minioClient;

    @Value("${minio.bucket}")
    private String bucketName;

    public String upload(File file) throws MinioException, IOException, InvalidKeyException, NoSuchAlgorithmException {
        var response = minioClient.uploadObject(
                UploadObjectArgs.builder()
                        .bucket(bucketName)
                        .object(file.getName())
                        .filename(file.toString())
                        .build());
        log.debug("Response: {}, {}, {}", response.etag(), response.headers(), response.object());
        return file.getName();
    }

    public InputStream getFileStream(String filename)
            throws MinioException, IOException, InvalidKeyException, NoSuchAlgorithmException {
        InputStream is;
        try {
            is = minioClient.getObject(GetObjectArgs.builder()
                    .bucket(bucketName)
                    .object(filename)
                    .build());
        } catch (ErrorResponseException e) {
            log.error("File {} not found", filename);
            return null;
        }
        return is;
    }

}
