package com.baisebreno.learning_spring_api.api;

import lombok.experimental.UtilityClass;
import org.apache.http.HttpHeaders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.util.Objects;

@UtilityClass
public class ResourceURIHelper {

    /**
     * Adds a {@code Location} header to the current HTTP response,
     * pointing to the newly created resource URI.
     *
     * <p>The URI is built from the current request’s URI and the given resource ID.
     * For example, if the current request is {@code POST /restaurants} and the
     * created resource has ID {@code 10}, this method will set:</p>
     *
     * <pre>
     * Location: http://localhost:8080/restaurants/10
     * </pre>
     *
     * @param resourceId the identifier of the newly created resource
     *                   (appended to the current request URI)
     */
    public static void addUriInResponseHeader(Object resourceId){
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(resourceId).toUri();

        HttpServletResponse response = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder
                .getRequestAttributes()))
                .getResponse();

        assert response != null;
        response.setHeader(HttpHeaders.LOCATION, uri.toString());
    }
}
