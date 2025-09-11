package com.baisebreno.learning_spring_api.infrastructure.service.storage;

import com.baisebreno.learning_spring_api.core.storage.StorageProperties;
import com.baisebreno.learning_spring_api.domain.exceptions.StorageException;
import com.baisebreno.learning_spring_api.domain.service.PhotoStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class LocalPhotoStorageService implements PhotoStorageService {

    @Autowired
    private StorageProperties storageProperties;

    @Override
    public FetchedPhoto getFile(String fileName) {
        try {
            Path filePath = getFilePath(fileName);

            return FetchedPhoto.builder()
                    .inputStream(Files.newInputStream(filePath))
                    .build();


        } catch (Exception e) {
            throw new StorageException("Could not find file", e);
        }
    }

    @Override
    public void store(NewPhoto newPhoto) {
        try {
            Path filePath = getFilePath(newPhoto.getFileName());
            Files.createDirectories(filePath.getParent());
            try (var out = Files.newOutputStream(filePath)) {
                FileCopyUtils.copy(newPhoto.getInputStream(), out);
            }
        } catch (Exception e) {
            throw new StorageException("Not possible to save file: " + newPhoto.getFileName(), e);
        }
    }

    @Override
    public void remove(String fileName) {
        try {
            Path filePath = getFilePath(fileName);
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new StorageException("Not possible to delete file", e);
        }
    }


    private Path getFilePath(String fileName) {
        return storageProperties.getLocal().getPhotoDirectory()
                .resolve(Path.of(fileName));
    }
}
