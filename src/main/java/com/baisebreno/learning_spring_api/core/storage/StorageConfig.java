package com.baisebreno.learning_spring_api.core.storage;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.baisebreno.learning_spring_api.domain.service.PhotoStorageService;
import com.baisebreno.learning_spring_api.core.storage.StorageProperties.StorageType;
import com.baisebreno.learning_spring_api.infrastructure.service.storage.LocalPhotoStorageService;
import com.baisebreno.learning_spring_api.infrastructure.service.storage.S3PhotoStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StorageConfig {

    @Autowired
    private StorageProperties storageProperties;

    @Bean
    public AmazonS3 amazonS3(){
        var credentials = new BasicAWSCredentials(
                storageProperties.getS3().getIdAccessKey(),
                storageProperties.getS3().getSecretAccessKey());

        return AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(storageProperties.getS3().getRegion())
                .build();
    }

    @Bean
    public PhotoStorageService photoStorageService(){
        if(StorageType.S3.equals(storageProperties.getType())){
            return new S3PhotoStorageService();
        }else{
            return new LocalPhotoStorageService();
        }
    }
}
