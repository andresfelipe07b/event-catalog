package org.riwi.eventcatalog.infraestructura.adapters.in.web.error;

import java.time.LocalDateTime;

public record ApiError(
        int statusCode,
        String message,
        LocalDateTime timestamp
) {
}
