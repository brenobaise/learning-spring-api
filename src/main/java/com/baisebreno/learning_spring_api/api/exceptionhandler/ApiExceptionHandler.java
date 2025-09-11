package com.baisebreno.learning_spring_api.api.exceptionhandler;

import com.baisebreno.learning_spring_api.core.validation.ValidationException;
import com.baisebreno.learning_spring_api.domain.exceptions.BusinessException;
import com.baisebreno.learning_spring_api.domain.exceptions.EntityInUseException;
import com.baisebreno.learning_spring_api.domain.exceptions.EntityNotFoundException;
import com.fasterxml.jackson.databind.exc.IgnoredPropertyException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.PropertyBindingException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.OffsetDateTime;
import java.util.List;
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

    public static final String INTERNAL_SERVER_ERROR_MESSAGE = "There was an internal server error, try again or let the system admin know.";
    public static final String MULTIPLE_INVALID_FIELDS_MESSAGE = "One or more fields are invalid. Correct the errors, and try again.";

    @Autowired
    private MessageSource messageSource;

    /**
     * Handles {@link EntityNotFoundException}, which is typically thrown when a requested
     * JPA entity or domain object does not exist in the database.
     * <p>
     * This method returns a standardized {@link Problem} response with HTTP 404 (Not Found),
     * following the RFC 7807 "Problem Details for HTTP APIs" specification.
     * <p>
     * The response includes both a technical {@code detail} message and a user-friendly
     * {@code userMessage}, which in this implementation are based on the exception's message.
     *
     * @param ex       the {@link EntityNotFoundException} thrown when an entity cannot be found
     * @param request  the current web request context
     * @return a {@link ResponseEntity} containing a {@link Problem} object describing the error
     *         in a structured, standards-compliant format
     *
     * @see javax.persistence.EntityNotFoundException
     * @see com.baisebreno.learning_spring_api.api.exceptionhandler.Problem
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleEntityNotFound(EntityNotFoundException ex, WebRequest request){

        HttpStatus status = HttpStatus.NOT_FOUND;
        ProblemType problemType = ProblemType.RESOURCE_NOT_FOUND;
        String detail = ex.getMessage();

        Problem problem = createProblemBuilder(status, problemType ,detail, "The resource was not found, double check the URL.", OffsetDateTime.now() )
                .build();

        return handleExceptionInternal(ex,problem,new HttpHeaders(), status, request);

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
    public ResponseEntity<?> handleEntityInUse(EntityInUseException ex, WebRequest request) {

        HttpStatus status = HttpStatus.CONFLICT;
        ProblemType problemType = ProblemType.ENTITY_IN_USE;

        Problem problem = createProblemBuilder(status, problemType, ex.getMessage(),ex.getMessage(), OffsetDateTime.now())
                .userMessage(INTERNAL_SERVER_ERROR_MESSAGE)
                .build();

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

        Problem problem = createProblemBuilder(status, problemType, ex.getMessage(),ex.getMessage(), OffsetDateTime.now())
                .build();

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
                    .userMessage(INTERNAL_SERVER_ERROR_MESSAGE)
                    .timestamp(OffsetDateTime.now())
                    .build();
        } else if (body instanceof String) {
            body = Problem.builder()
                    .title((String) body)
                    .status(status.value())
                    .userMessage(INTERNAL_SERVER_ERROR_MESSAGE)
                    .timestamp(OffsetDateTime.now())
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
     *       it delegates to {@link #handleInvalidFormat(InvalidFormatException, HttpHeaders, HttpStatus, WebRequest)}
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
            return handleInvalidFormat((InvalidFormatException) rootCause, headers, status, request);
        } else if (rootCause instanceof PropertyBindingException) {
            return handlePropertyBinding((PropertyBindingException) rootCause, headers, status, request);
        }

        // Generic unreadable body case (malformed JSON, missing braces, etc.)
        ProblemType problemType = ProblemType.MESSAGE_NOT_READABLE;
        String detail = "The request body is invalid. Please check the JSON syntax.";

        Problem problem = createProblemBuilder(status, problemType, detail, INTERNAL_SERVER_ERROR_MESSAGE, OffsetDateTime.now())
                .build();

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
    private ResponseEntity<Object> handlePropertyBinding(PropertyBindingException ex, HttpHeaders headers,
                                                         HttpStatus status, WebRequest request) {

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
        Problem problem = createProblemBuilder(status, problemType, detail,INTERNAL_SERVER_ERROR_MESSAGE, OffsetDateTime.now())
                .build();

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
    private ResponseEntity<Object> handleInvalidFormat(InvalidFormatException ex, HttpHeaders headers,
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

        Problem problem = createProblemBuilder(status, problemType, detail, INTERNAL_SERVER_ERROR_MESSAGE, OffsetDateTime.now())
                .build();

        return handleExceptionInternal(ex, problem, headers, status, request);
    }


    /**
     * Helper for building a {@link Problem} aligned with RFC 7807 fields.
     *
     * @param status      the HTTP status to set ({@code Problem.status}).
     * @param problemType the high-level problem classification (maps to {@code type} and {@code title}).
     * @param detail      human-readable description of this particular occurrence, to the api consumer.
     * @param userMessage human-readable description of this particular occurrence, to the user.
     * @param timestamp   time of occurrence.
     * @return a {@link Problem.ProblemBuilder} primed with {@code status}, {@code type}, {@code title}, and {@code detail}.
     */
    private Problem.ProblemBuilder createProblemBuilder(HttpStatus status, ProblemType problemType,
                                                        String detail, String userMessage, OffsetDateTime timestamp) {
        return Problem.builder()
                .status(status.value())
                .type(problemType.getUri())
                .title(problemType.getTitle())
                .userMessage(userMessage)
                .timestamp(timestamp)
                .detail(detail);
    }

    /**
     * Handles {@link TypeMismatchException} thrown when a request parameter, path variable,
     * or other input value cannot be converted to the expected type.
     * <p>
     * If the exception is an instance of {@link MethodArgumentTypeMismatchException},
     * this method delegates handling to {@link #handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException, HttpHeaders, HttpStatus, WebRequest)}
     * for more specific error reporting.
     * Otherwise, it falls back to the default {@link ResponseEntityExceptionHandler#handleTypeMismatch(TypeMismatchException, HttpHeaders, HttpStatus, WebRequest)}.
     *
     * @param ex       the exception to handle (may be {@link MethodArgumentTypeMismatchException} or a general {@link TypeMismatchException})
     * @param headers  the HTTP headers for the response
     * @param status   the HTTP status to return
     * @param request  the current web request
     * @return a {@link ResponseEntity} containing a structured error response
     */
    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        // if it's of this type, call the handler
        if (ex instanceof MethodArgumentTypeMismatchException) {
            return handleMethodArgumentTypeMismatch(
                    (MethodArgumentTypeMismatchException) ex, headers, status, request);
        }

        return super.handleTypeMismatch(ex, headers, status, request);
    }

    /**
     * Handles {@link MethodArgumentTypeMismatchException}, which occurs when a controller method
     * receives a request parameter or path variable that cannot be converted to the expected type.
     * <p>
     * This method creates a problem detail response indicating which parameter was invalid,
     * the invalid value provided, and the expected type.
     *
     * @param ex      the exception containing details about the type mismatch
     * @param headers the HTTP headers for the response
     * @param status  the HTTP status to return
     * @param request the current web request
     * @return a {@link ResponseEntity} containing a {@link Problem} object with detailed error information
     */
    private ResponseEntity<Object> handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {

        ProblemType problemType = ProblemType.INVALID_PARAMETER;

        String detail = String.format(
                "The URL parameter '%s' received the value '%s', which is an invalid type. Type: %s",
                ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName()
        );

        Problem problem = createProblemBuilder(status, problemType, detail,ex.getMessage(), OffsetDateTime.now())
                .build();
        return handleExceptionInternal(ex, problem, headers, status, request);
    }


    /**
     * Handles {@link NoHandlerFoundException}, which occurs when a client attempts
     * to access a resource or endpoint that does not exist in the application.
     * <p>
     * This method builds a standardized {@link Problem} response indicating that the
     * requested resource could not be found, following the application's error response model.
     *
     * @param ex       the exception that was thrown when no handler was found for the request
     * @param headers  the HTTP headers for the response
     * @param status   the HTTP status to return (typically 404 Not Found)
     * @param request  the current web request
     * @return a {@link ResponseEntity} containing a {@link Problem} object with
     *         details about the missing resource
     */
    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ProblemType problemType = ProblemType.RESOURCE_NOT_FOUND;

        String detail = String.format("The resource %s you tried to access, does not exist.", ex.getRequestURL());
        Problem problem = createProblemBuilder(status,problemType,detail,INTERNAL_SERVER_ERROR_MESSAGE, OffsetDateTime.now())
                .build();

        return handleExceptionInternal(ex,problem,headers,status,request);
    }

    /**
     * Catch-all handler for any uncaught exceptions that were not matched by more specific handlers.
     * <p>
     * Returns a standardized {@link Problem} response with HTTP 500 (Internal Server Error),
     * ensuring clients receive a consistent error payload without exposing stack traces.
     * In development, this method currently prints the stack trace; replace this with structured
     * logging before deploying to production.
     *
     * @param ex       the unhandled exception that bubbled up from the application
     * @param request  the current web request context
     * @return a {@link ResponseEntity} containing a {@link Problem} describing a system error
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleUnCaught(Exception ex, WebRequest request){
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ProblemType problemType = ProblemType.SYSTEM_ERROR;
        String detail = String.format(INTERNAL_SERVER_ERROR_MESSAGE);

        /*
        Printing the stack trace needs to be removed for production, we use it until logging replaces this.
         */
        ex.printStackTrace();
        Problem problem = createProblemBuilder(status, problemType, detail, detail, OffsetDateTime.now())
                .build();

        return handleExceptionInternal(ex,problem, new HttpHeaders(), status, request);
    }

    /**
     * Handles {@link MethodArgumentNotValidException}, which occurs when an incoming
     * request body annotated with {@code @Valid} fails Bean Validation.
     *
     * <p>This method delegates to {@link #handleValidationInternal(Exception, BindingResult, HttpHeaders, HttpStatus, WebRequest)}
     * to build a structured {@link Problem} response containing details of all invalid fields.</p>
     *
     * <h2>Example scenario</h2>
     * <pre>
     * POST /users
     * {
     *   "name": "",      // @NotBlank violation
     *   "age": 10        // @Min(18) violation
     * }
     *
     * Response:
     * {
     *   "type": "https://algafood.co.uk/invalid-parameter",
     *   "title": "Invalid Parameter",
     *   "status": 400,
     *   "detail": "One or more fields are invalid.",
     *   "objects": [
     *     { "name": "name", "userMessage": "must not be blank" },
     *     { "name": "age", "userMessage": "must be greater than or equal to 18" }
     *   ]
     * }
     * </pre>
     *
     * @param ex       the validation exception containing field errors
     * @param headers  the HTTP headers
     * @param status   the HTTP status code (typically 400 Bad Request)
     * @param request  the current web request
     * @return a {@link ResponseEntity} with a {@link Problem} body describing validation errors
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {

        return handleValidationInternal(ex, ex.getBindingResult(), headers, status, request);
    }

    /**
     * Centralized handler for translating Bean Validation errors into
     * a structured {@link Problem} response.
     *
     * <p>This method extracts all {@link ObjectError} and {@link FieldError} instances
     * from the {@link BindingResult}, resolves their localized error messages via
     * Spring's {@link org.springframework.context.MessageSource}, and maps them into
     * a list of {@link Problem.Object} entries.</p>
     *
     * <p>Both field-level and object-level (class-level) validation errors are supported.
     * Field errors are reported with the field name, while global errors fall back to the
     * object name.</p>
     *
     * @param ex            the exception that triggered validation handling
     * @param bindingResult the binding result containing validation errors
     * @param headers       the HTTP headers
     * @param status        the HTTP status to return
     * @param request       the current web request
     * @return a {@link ResponseEntity} containing a {@link Problem} object with details
     *         of all validation failures
     */
    private ResponseEntity<Object> handleValidationInternal(
            Exception ex,
            BindingResult bindingResult,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {

        ProblemType problemType = ProblemType.INVALID_PARAMETER;
        String detail = MULTIPLE_INVALID_FIELDS_MESSAGE;

        List<Problem.Object> problemObjects = bindingResult.getAllErrors().stream()
                .map(objectError -> {
                    // Use i18n message source to resolve default messages
                    String message = messageSource.getMessage(objectError, LocaleContextHolder.getLocale());

                    String name = objectError.getObjectName();
                    if (objectError instanceof FieldError) {
                        name = ((FieldError) objectError).getField();
                    }

                    return Problem.Object.builder()
                            .name(name)
                            .userMessage(message)
                            .build();
                })
                .collect(Collectors.toList());

        Problem problem = createProblemBuilder(status, problemType, detail, detail, OffsetDateTime.now())
                .objects(problemObjects)
                .build();

        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    /**
     * Handles custom {@link ValidationException} thrown explicitly within
     * the application (outside of the default Spring MVC validation flow).
     *
     * <p>This allows domain or service-layer validation errors to be reported
     * consistently with request body validation errors.</p>
     *
     * <p>For example, if a business rule check is wrapped in a custom
     * {@link ValidationException}, this handler will produce the same RFC-7807
     * problem response as {@link #handleMethodArgumentNotValid}.</p>
     *
     * @param ex      the custom validation exception containing a {@link BindingResult}
     * @param request the current web request
     * @return a {@link ResponseEntity} containing a {@link Problem} body with
     *         validation error details
     */
    @ExceptionHandler({ ValidationException.class })
    public ResponseEntity<Object> handleValidationException(
            ValidationException ex,
            WebRequest request) {

        return handleValidationInternal(
                ex,
                ex.getBindingResult(),
                new HttpHeaders(),
                HttpStatus.BAD_REQUEST,
                request);
    }

    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status,
                                                         WebRequest request) {
        return handleValidationInternal(ex, ex.getBindingResult(), headers, status, request);
    }
}
