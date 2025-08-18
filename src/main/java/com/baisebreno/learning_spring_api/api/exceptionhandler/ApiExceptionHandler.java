package com.baisebreno.learning_spring_api.api.exceptionhandler;

import com.baisebreno.learning_spring_api.domain.exceptions.BusinessException;
import com.baisebreno.learning_spring_api.domain.exceptions.EntityInUseException;
import com.baisebreno.learning_spring_api.domain.exceptions.EntityNotFoundException;
import com.fasterxml.jackson.databind.exc.IgnoredPropertyException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.PropertyBindingException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Objects;
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

    /**
     * Handles cases where the HTTP request body cannot be read or parsed.
     *
     * <p>This method overrides Spring’s default {@link #handleHttpMessageNotReadable}
     * to provide a {@link Problem} response instead of a generic error.</p>
     *
     * <ul>
     *   <li>If the root cause is an {@link com.fasterxml.jackson.databind.exc.InvalidFormatException},
     *       it delegates to {@link #handleInvalidFormatException(InvalidFormatException, HttpHeaders, HttpStatus, WebRequest)}
     *       to provide detailed feedback about the property and invalid value.</li>
     *   <li>Otherwise, it responds with a generic "message not readable" problem.</li>
     * </ul>
     *
     * @param ex      the thrown {@link org.springframework.http.converter.HttpMessageNotReadableException}.
     * @param headers HTTP headers to be written to the response.
     * @param status  HTTP status code to use (typically 400 Bad Request).
     * @param request the current web request context.
     * @return a {@link ResponseEntity} containing a {@link Problem} with details of the parsing failure.
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {

        Throwable rootCause = ExceptionUtils.getRootCause(ex);

        // Specific case: JSON field has wrong type (e.g. string instead of number).
        if (rootCause instanceof InvalidFormatException) {
            return handleInvalidFormatException((InvalidFormatException) rootCause, headers, status, request);
        } else if (rootCause instanceof PropertyBindingException) {
            return handlePropertyBindingException((PropertyBindingException) rootCause, headers,status,request);
        }

        // Generic unreadable body case (malformed JSON, missing braces, etc.)
        ProblemType problemType = ProblemType.MESSAGE_NOT_READABLE;
        String detail = "The request body is invalid. Please check the JSON syntax.";

        Problem problem = createProblemBuilder(status, problemType, detail).build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    }



    /**
     * Handles Jackson {@link PropertyBindingException} variants that occur when the JSON payload
     * contains properties that are not allowed or not recognized by the target type.
     *
     * <p>Typical cases:
     * <ul>
     *   <li>{@link IgnoredPropertyException}: property is explicitly ignored via Jackson annotations/config.</li>
     *   <li>{@link UnrecognizedPropertyException}: property does not exist on the target type.</li>
     * </ul>
     * </p>
     *
     * <p>Responds with {@code ProblemType.MESSAGE_NOT_READABLE} and a helpful {@code detail}
     * message pointing to the offending property path.</p>
     *
     * @param ex      the {@link PropertyBindingException} root cause from Jackson.
     * @param headers HTTP headers to be written to the response.
     * @param status  HTTP status to use (typically 400 Bad Request).
     * @param request the current web request context.
     * @return a {@link ResponseEntity} with a {@link Problem} body describing the error.
     */
    private ResponseEntity<Object> handlePropertyBindingException(
            PropertyBindingException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {

        // Build a dotted path like "address.street"
        String path = ex.getPath().stream()
                .map(ref -> ref.getFieldName())
                .filter(Objects::nonNull)
                .collect(Collectors.joining("."));

        ProblemType problemType = ProblemType.MESSAGE_NOT_READABLE;
        String detail;

        // If it's an @JsonIgnore Annotated property
        if (ex instanceof IgnoredPropertyException) {
            detail = String.format(
                    "The property '%s' (path: '%s') is not accepted by this resource. Remove it and try again.",
                    ex.getPropertyName(), path.isEmpty() ? ex.getPropertyName() : path
            );

        // if the property does not exist.
        } else if (ex instanceof UnrecognizedPropertyException) {
            detail = String.format(
                    "The property '%s' (path: '%s') does not exist. Check for typos or remove it.",
                    ex.getPropertyName(), path.isEmpty() ? ex.getPropertyName() : path
            );
        } else {
            // Fallback for other PropertyBindingException cases
            detail = String.format(
                    "The property '%s' (path: '%s') is invalid for this resource.",
                    ex.getPropertyName(), path.isEmpty() ? ex.getPropertyName() : path
            );
        }

        // creates the problem builder
        Problem problem = createProblemBuilder(status, problemType, detail).build();
        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    /**
     * Handles Jackson {@link InvalidFormatException}, which occurs when a property
     * in the JSON request body receives a value of the wrong type.
     *
     * <p>Example: sending <code>{"age": "twenty"}</code> when the field is declared as an {@code Integer}.</p>
     *
     * <p>This handler extracts the path to the offending field, the invalid value,
     * and the expected Java type, then builds a {@link Problem} response with that information.</p>
     *
     * @param ex      the {@link InvalidFormatException} containing details about the type mismatch.
     * @param headers HTTP headers to be written to the response.
     * @param status  HTTP status code to use (typically 400 Bad Request).
     * @param request the current web request context.
     * @return a {@link ResponseEntity} containing a {@link Problem} describing the invalid format error.
     */
    private ResponseEntity<Object> handleInvalidFormatException(InvalidFormatException ex, HttpHeaders headers,
                                                                HttpStatus status, WebRequest request) {

        // Build a JSON path like "address.streetNumber"
        String path = ex.getPath().stream()
                .map(ref -> ref.getFieldName())
                .collect(Collectors.joining("."));

        ProblemType problemType = ProblemType.MESSAGE_NOT_READABLE;

        // Build a human-readable detail message to be displayed in the HTTP response
        String detail = String.format(
                "The property '%s' received the value '%s', which is an invalid type. " +
                        "Submit a value compatible with type %s.",
                path,
                ex.getValue(),
                ex.getTargetType().getSimpleName()
        );

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
    private Problem.ProblemBuilder createProblemBuilder(HttpStatus status, ProblemType problemType,
                                                        String detail) {
        return Problem.builder()
                .status(status.value())
                .type(problemType.getUri())
                .title(problemType.getTitle())
                .detail(detail);
    }
}
