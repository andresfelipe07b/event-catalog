package org.riwi.eventcatalog.exception;

import java.time.LocalDateTime;

public record ApiError(
        int statusCode,
        String message,
        LocalDateTime timestamp
) {
}
