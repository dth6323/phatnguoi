package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.MinioConfig;
import io.minio.*;
import io.minio.errors.MinioException;
import io.minio.messages.Item;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileService {

    private final MinioConfig minioConfig;
    private final MinioClient minioClient;

    public FileService(MinioConfig minioConfig) {
        this.minioConfig = minioConfig;
        this.minioClient = MinioClient.builder()
            .endpoint(minioConfig.getUrl())
            .credentials(minioConfig.getAccessKey(), minioConfig.getSecretKey())
            .build();
    }

    public String uploadFile(MultipartFile file) throws Exception {
        String bucketName = minioConfig.getBucketName();
        boolean isBucketExists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        if (!isBucketExists) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }

        minioClient.putObject(
            PutObjectArgs.builder()
                .bucket(bucketName)
                .object(file.getOriginalFilename()) // Tên file
                .stream(file.getInputStream(), file.getSize(), -1) // Dữ liệu
                .contentType(file.getContentType()) // Loại file
                .build()
        );
        return minioConfig.getUrl() + "/" + bucketName + "/" + file.getOriginalFilename();
    }

    public InputStream getVideoStream(String fileName) {
        String bucketName = minioConfig.getBucketName();
        try {
            return minioClient.getObject(GetObjectArgs.builder().bucket(bucketName).object(fileName).build());
        } catch (MinioException e) {
            System.out.println("Error occurred: " + e);
            throw new RuntimeException("Error retrieving video stream from MinIO", e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Unexpected error", e);
        }
    }
}
