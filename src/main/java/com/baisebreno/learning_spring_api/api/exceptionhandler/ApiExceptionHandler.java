package com.baisebreno.learning_spring_api.api.exceptionhandler;

import com.baisebreno.learning_spring_api.domain.exceptions.BusinessException;
import com.baisebreno.learning_spring_api.domain.exceptions.EntityInUseException;
import com.baisebreno.learning_spring_api.domain.exceptions.EntityNotFoundException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Arrays;
import java.util.stream.Collectors;


/**
 * Global API exception handler that produces RFC 7807-style error responses.
 *
 * <p>This component centralizes mapping of domain exceptions to HTTP responses,
 * using a {@link Problem} payload shaped after the Problem Details format
 * (RFC 7807). It also overrides {@link #handleExceptionInternal(Exception, Object, HttpHeaders, HttpStatus, WebRequest)}
 * to enforce a consistent response body even for framework-raised exceptions.</p>
 *
 * <h2>Responsibilities</h2>
 * <ul>
 *   <li>Translate {@link EntityNotFoundException} to 404 Not Found.</li>
 *   <li>Translate {@link EntityInUseException} to 409 Conflict.</li>
 *   <li>Translate {@link BusinessException} to 400 Bad Request.</li>
 *   <li>Build a {@link Problem} with {@code type}, {@code title}, {@code status}, and {@code detail} fields.</li>
 * </ul>
 *
 * <h2>Response shape</h2>
 * <p>Example JSON:</p>
 * <pre>{@code
 * {
 *   "type": "https://algafood.co.uk/entity-not-found",
 *   "title": "Entity Not Found",
 *   "status": 404,
 *   "detail": "Restaurant with id=10 does not exist"
 * }
 * }</pre>
 */
@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Handles resource-not-found scenarios.
     *
     * <p>Typically thrown when the requested entity does not exist in persistence.</p>
     *
     * @param ex      the domain exception describing the missing entity.
     * @param request the current request context.
     * @return a 404 Not Found response containing a {@link Problem} body.
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleGeographicalStateNotFoundException(
            EntityNotFoundException ex, WebRequest request) {

        HttpStatus status = HttpStatus.NOT_FOUND;
        ProblemType problemType = ProblemType.ENTITY_NOT_FOUND;

        Problem problem = createProblemBuilder(status, problemType, ex.getMessage()).build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    }

    /**
     * Handles integrity/conflict violations where an entity cannot be removed or altered
     * due to existing references or constraints.
     *
     * <p>Example: attempting to delete a parent that still has children.</p>
     *
     * @param ex      the domain exception indicating the entity is in use.
     * @param request the current request context.
     * @return a 409 Conflict response containing a {@link Problem} body.
     */
    @ExceptionHandler(EntityInUseException.class)
    public ResponseEntity<?> handlerEntityInUseException(EntityInUseException ex, WebRequest request) {

        HttpStatus status = HttpStatus.CONFLICT;
        ProblemType problemType = ProblemType.ENTITY_IN_USE;

        Problem problem = createProblemBuilder(status, problemType, ex.getMessage()).build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    }

    /**
     * Handles business rule violations (validation beyond basic field-level validation,
     * domain invariants, or cross-entity checks).
     *
     * @param ex      the domain exception describing the business rule breach.
     * @param request the current request context.
     * @return a 400 Bad Request response containing a {@link Problem} body.
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<?> handleBusinessException(BusinessException ex, WebRequest request) {

        HttpStatus status = HttpStatus.BAD_REQUEST;
        ProblemType problemType = ProblemType.BUSINESS_LOGIC_ERROR;

        Problem problem = createProblemBuilder(status, problemType, ex.getMessage()).build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    }

    /**
     * Ensures a standardized {@link Problem} body for exceptions handled by Spring MVC.
     *
     * <p>If the {@code body} is {@code null} or a raw {@link String}, it is wrapped into
     * a {@link Problem} with a {@code title} and {@code status}. Framework exceptions that
     * already supply a structured body will pass through unchanged.</p>
     *
     * @param ex      the exception being handled.
     * @param body    the response body (may be {@code null}, a {@link String}, or a structured object).
     * @param headers response headers.
     * @param status  HTTP status to return.
     * @param request the current request context.
     * @return a response entity carrying a {@link Problem} payload.
     */
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {

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

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Throwable rootCause = ExceptionUtils.getRootCause(ex);


        if(rootCause instanceof InvalidFormatException){
            return handleInvalidFormatException((InvalidFormatException) rootCause,headers,status,request);
        }

        ProblemType problemType = ProblemType.MESSAGE_NOT_READABLE;
        String detail = "The request's body is invalid, Check your syntax.";

        Problem problem = createProblemBuilder(status, problemType, detail).build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    }

    private ResponseEntity<Object> handleInvalidFormatException(InvalidFormatException ex, HttpHeaders headers, HttpStatus status,
                                                                WebRequest request) {
        String path = ex.getPath().stream()
                .map(ref -> ref.getFieldName())
                .collect(Collectors.joining("."));

        ProblemType problemType = ProblemType.MESSAGE_NOT_READABLE;

        String detail = String.format("The property '%s' received the value '%s', which is an invalid type." +
                        " Submit a value of type %s.", path,
                ex.getValue().toString(),
                ex.getTargetType().getSimpleName());

        Problem problem = createProblemBuilder(status, problemType, detail).build();

        return handleExceptionInternal(ex, problem, headers, status, request);
    }


    /**
     * Helper for building a {@link Problem} aligned with RFC 7807 fields.
     *
     * @param status      the HTTP status to set ({@code Problem.status}).
     * @param problemType the high-level problem classification (maps to {@code type} and {@code title}).
     * @param detail      human-readable description of this particular occurrence.
     * @return a {@link Problem.ProblemBuilder} primed with {@code status}, {@code type}, {@code title}, and {@code detail}.
     */
    private Problem.ProblemBuilder createProblemBuilder(HttpStatus status, ProblemType problemType, String detail) {
        return Problem.builder()
                .status(status.value())
                .type(problemType.getUri())
                .title(problemType.getTitle())
                .detail(detail);
    }
}
