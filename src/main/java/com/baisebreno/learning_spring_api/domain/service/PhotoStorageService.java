package com.baisebreno.learning_spring_api.domain.service;

import lombok.Builder;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.UUID;

@Service
public interface PhotoStorageService {

    void store(NewPhoto newPhoto);

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
