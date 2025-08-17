package com.baisebreno.learning_spring_api.api.exceptionhandler;

import com.baisebreno.learning_spring_api.domain.exceptions.BusinessException;
import com.baisebreno.learning_spring_api.domain.exceptions.EntityInUseException;
import com.baisebreno.learning_spring_api.domain.exceptions.EntityNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

/**
 * {@code ApiExceptionHandler} is a global exception handler for the API.
 * <p>
 * It centralizes the handling of domain-specific exceptions (e.g., {@link EntityNotFoundException},
 * {@link EntityInUseException}, {@link BusinessException}) and ensures a consistent
 * response structure across the API.
 * </p>
 *
 * <p>It extends {@link ResponseEntityExceptionHandler}, which provides default
 * handling for Spring MVC exceptions, and overrides {@code handleExceptionInternal}
 * to format error responses into a unified {@code Problem} object.</p>
 *
 * <h2>Responsibilities:</h2>
 * <ul>
 *     <li>Catch and map domain exceptions to appropriate HTTP status codes.</li>
 *     <li>Build a {@code Problem} object with timestamp and error message.</li>
 *     <li>Provide consistent error response format across the API.</li>
 * </ul>
 */
@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Handles cases where an entity is not found in the system.
     * <p>
     * Typically thrown when a resource requested by the client does not exist.
     * </p>
     *
     * @param ex      the thrown {@link EntityNotFoundException}.
     * @param request the current web request.
     * @return a {@link ResponseEntity} containing a {@code Problem} object and a {@code 404 Not Found} status.
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleGeographicalStateNotFoundException(
            EntityNotFoundException ex, WebRequest request) {

        HttpStatus status = HttpStatus.NOT_FOUND;
        ProblemType problemType = ProblemType.ENTITY_NOT_FOUND;

        Problem problem = createProblemBuilder(status, problemType, ex.getMessage())
                .build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    }

    /**
     * Handles cases where an entity cannot be deleted because it is in use.
     * <p>
     * Example: trying to delete a state that is still linked to a city.
     * </p>
     *
     * @param ex      the thrown {@link EntityInUseException}.
     * @param request the current web request.
     * @return a {@link ResponseEntity} containing a {@code Problem} object and a {@code 409 Conflict} status.
     */
    @ExceptionHandler(EntityInUseException.class)
    public ResponseEntity<?> handlerEntityInUseException(EntityInUseException ex, WebRequest request) {

        HttpStatus status = HttpStatus.CONFLICT;
        ProblemType problemType = ProblemType.ENTITY_IN_USE;

        Problem problem = createProblemBuilder(status, problemType, ex.getMessage())
                .build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    }

    /**
     * Handles general business logic violations.
     * <p>
     * Example: trying to register a duplicate entry where uniqueness is required.
     * </p>
     *
     * @param ex      the thrown {@link BusinessException}.
     * @param request the current web request.
     * @return a {@link ResponseEntity} containing a {@code Problem} object and a {@code 400 Bad Request} status.
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<?> handleBusinessException(BusinessException ex, WebRequest request) {

        HttpStatus status = HttpStatus.BAD_REQUEST;
        ProblemType problemType = ProblemType.BUSINESS_LOGIC_ERROR;

        Problem problem = createProblemBuilder(status, problemType, ex.getMessage())
                .build();

        return handleExceptionInternal(ex,problem, new HttpHeaders(),status, request);
    }

    /**
     * Overrides the default Spring MVC exception handling to always return
     * a standardized {@code Problem} response body.
     * <p>
     * If the provided {@code body} is {@code null} or just a {@code String},
     * this method wraps it into a {@code Problem} object with a timestamp and message.
     * </p>
     *
     * @param ex      the exception being handled.
     * @param body    the body to be returned (can be {@code null}, a message, or a structured object).
     * @param headers HTTP headers to include in the response.
     * @param status  HTTP status code of the response.
     * @param request the current web request.
     * @return a {@link ResponseEntity} containing a {@code Problem} object and the given status.
     */
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
                                                             HttpStatus status, WebRequest request) {
        if (body == null) {
            body = Problem.builder()
                    .title(status.getReasonPhrase())
                    .status(status.value())
                    .build();
        } else if (body instanceof String) {
            body = Problem.builder()
                    .title((String) body)
                    .status(status.value())
                    .build();
        }

        return super.handleExceptionInternal(ex, body, headers, status, request);
    }

    /**
     * Assembles a {@link Problem.ProblemBuilder} with the given params.
     * @param status  {@link HttpStatus}
     * @param problemType {@link ProblemType}
     * @param detail The description of the problem, a message.
     * @return {@link Problem.ProblemBuilder} to be used to create a {@link Problem}
     */
    private Problem.ProblemBuilder createProblemBuilder(HttpStatus status, ProblemType problemType, String detail){
        return  Problem.builder()
                .status(status.value())
                .type(problemType.getUri())
                .title(problemType.getTitle())
                .detail(detail);
    }

}
