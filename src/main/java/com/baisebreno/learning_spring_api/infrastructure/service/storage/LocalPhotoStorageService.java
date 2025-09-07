package com.baisebreno.learning_spring_api.infrastructure.service.storage;

import com.baisebreno.learning_spring_api.domain.exceptions.StorageException;
import com.baisebreno.learning_spring_api.domain.service.PhotoStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
@Service
public class LocalPhotoStorageService implements PhotoStorageService {

    @Value("${algafood.storage.local.photo-directory}")
    private String photoDirectoryProp; // <— inject as String

    private Path photoDirectory;

    @PostConstruct
    void init() throws IOException, URISyntaxException {
        // Support both "C:/..." and "file:///C:/..."
        this.photoDirectory = photoDirectoryProp.startsWith("file:")
                ? Path.of(new java.net.URI(photoDirectoryProp))
                : Path.of(photoDirectoryProp);

        Files.createDirectories(this.photoDirectory);
        // Optional: log the resolved absolute path for sanity
        System.out.println("Photo dir: " + this.photoDirectory.toAbsolutePath());
    }

    @Override
    public void store(NewPhoto newPhoto) {
        try {
            Path filePath = getFilePath(newPhoto.getFileName());
            // ensure parent dirs for the specific file too (in case fileName contains subfolders)
            Files.createDirectories(filePath.getParent());
            // Use try-with-resources so the OutputStream is closed even on error
            try (var out = Files.newOutputStream(filePath)) {
                FileCopyUtils.copy(newPhoto.getInputStream(), out);
            }
        } catch (Exception e) {
            throw new StorageException("Not possible to save file: " + newPhoto.getFileName(), e);
        }
    }

    /**
     * Deletes a previous file inside the catalogue folder.
     * @param fileName the target file name.
     */
    @Override
    public void remove(String fileName) {
        try {
            Path filePath = getFilePath(fileName);
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new StorageException("Not possible to delete file", e.getCause());
        }

    }

    private Path getFilePath(String fileName) {
        return photoDirectory.resolve(Path.of(fileName));
    }
}
