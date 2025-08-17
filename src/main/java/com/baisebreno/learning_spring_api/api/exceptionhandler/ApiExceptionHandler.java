package com.baisebreno.learning_spring_api.api.exceptionhandler;

import com.baisebreno.learning_spring_api.domain.exceptions.BusinessException;
import com.baisebreno.learning_spring_api.domain.exceptions.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

/**
 * Global Exception Handler
 */
@ControllerAdvice
public class ApiExceptionHandler {

    /**
     * Handles all subclasses and including {@link EntityNotFoundException}
     * @return a {@link ResponseEntity} with a body and a status code.
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleGeographicalStateNotFoundException(
            EntityNotFoundException e){

        Problem problem = Problem.builder()
                .dateTime(LocalDateTime.now())
                .message(e.getMessage())
                .build();


        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(problem);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<?> handleBusinessException(BusinessException e){


        Problem problem = Problem.builder()
                .dateTime(LocalDateTime.now())
                .message(e.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(problem);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<?> handleHttpMediaTypeNotSupportedException(){


        Problem problem = Problem.builder()
                .dateTime(LocalDateTime.now())
                .message("This media type is not accepted")
                .build();

        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                .body(problem);
    }
}
