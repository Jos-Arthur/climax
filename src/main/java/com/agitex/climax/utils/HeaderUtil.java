package com.agitex.climax.utils;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;

/**
 * Utility class for HTTP headers creation.
 */
@Slf4j
@NoArgsConstructor
public final class HeaderUtil {

    private static final String APPLICATION_NAME = "climax";

    /**
     * Create alert method.
     *
     * @param message
     * @param param
     * @return Http headers
     */
    public static HttpHeaders createAlert(final String message,
                                          final String param
    ) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-" + APPLICATION_NAME + "-alert", message);
        headers.add("X-" + APPLICATION_NAME + "-params", param);
        return headers;
    }

    /**
     * @param entityName
     * @param param
     * @return Http headers
     */
    public static HttpHeaders createEntityCreationAlert(
            final String entityName,
            final String param
    ) {
        return createAlert("A new "
                + entityName
                + " is created with identifier "
                + param, param);
    }

    /**
     * @param entityName
     * @param param
     * @return Http headers
     */
    public static HttpHeaders createEntityUpdateAlert(
            final String entityName,
            final String param
    ) {
        return createAlert("A "
                + entityName
                + " is updated with identifier "
                + param, param);
    }

    /**
     * @param entityName
     * @param param
     * @return Http headers
     */
    public static HttpHeaders createEntityDeletionAlert(
            final String entityName,
            final String param
    ) {
        return createAlert("A "
                + entityName
                + " is deleted with identifier "
                + param, param);
    }

    /**
     * @param entityName
     * @param errorKey
     * @param defaultMessage
     * @return Http headers
     */
    public static HttpHeaders createFailureAlert(
            final String entityName,
            final String errorKey,
            final String defaultMessage
    ) {
        log.error("Entity processing failed, {}", defaultMessage);
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-" + APPLICATION_NAME + "-error", defaultMessage);
        headers.add("X-" + APPLICATION_NAME + "-params", entityName);
        return headers;
    }
}
