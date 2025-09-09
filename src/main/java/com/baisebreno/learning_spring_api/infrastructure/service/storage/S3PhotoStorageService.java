package com.baisebreno.learning_spring_api.infrastructure.service.storage;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.baisebreno.learning_spring_api.core.storage.StorageProperties;
import com.baisebreno.learning_spring_api.domain.exceptions.StorageException;
import com.baisebreno.learning_spring_api.domain.service.PhotoStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
public class S3PhotoStorageService implements PhotoStorageService {

    @Autowired
    private AmazonS3 amazonS3;

    @Autowired
    private StorageProperties storageProperties;


    /**
     * @param fileName
     * @return
     */
    @Override
    public InputStream getFile(String fileName) {
        return null;
    }

    /**
     * @param newPhoto
     */
    @Override
    public void store(NewPhoto newPhoto) {
        try {
            String pathFile = getFilePath(newPhoto.getFileName());


            var objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(newPhoto.getContentType());

            var putObjectRequest  = new PutObjectRequest(
                    storageProperties.getS3().getBucket(),
                    pathFile,
                    newPhoto.getInputStream(),
                    objectMetadata)
                .withCannedAcl(CannedAccessControlList.PublicRead);

            amazonS3.putObject(putObjectRequest);
        } catch (Exception e) {
            throw new StorageException("Not possible to upload file to Amazon s3", e);
        }

    }


    /**
     * @param fileName
     */
    @Override
    public void remove(String fileName) {
        try{
            String filepath = getFilePath(fileName);
            System.out.println(" FIEL NAME ----------" + fileName);

            System.out.println("PATH FILE ----------" + filepath);
            var deleteObjectRequest = new DeleteObjectRequest(
                    storageProperties.getS3().getBucket(),
                    filepath);

            amazonS3.deleteObject(deleteObjectRequest);

        }catch (Exception e){
            throw new StorageException("Not possible to delete file from Amazon s3", e);

        }

    }


    private String getFilePath(String fileName) {
        String text = String.format("%s/%s",
                storageProperties.getS3().getPhotoDirectory(),
                fileName);

        System.out.println("GET FILE PATH FORMATION " + text);
        return text;
    }
}
