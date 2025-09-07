package com.baisebreno.learning_spring_api.domain.service;

import lombok.Builder;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.UUID;

@Service
public interface PhotoStorageService {

    InputStream getFile(String fileName);

    void store(NewPhoto newPhoto);

    void remove(String fileName);

    /**
     * Saves the new photo on the catalogue folder. And, removes the old/previous file.
     * @param existingFileName current file in the folder
     * @param photo the new photo to replace.
     */
    default void replace(String existingFileName, NewPhoto photo){
        this.store(photo);
        if(existingFileName !=null){
            this.remove(existingFileName);
        }
    }

    /**
     * Appends a UUID to a given String representing the file name.
     * @param originalName the file name
     * @return returns a random UUID concatenated to the original file name.
     */
    default String generateFileName(String originalName){
        return UUID.randomUUID().toString() + "_" + originalName;
    }

    @Getter
    @Builder
    class NewPhoto{
        private String fileName;
        private InputStream inputStream;
    }
}
