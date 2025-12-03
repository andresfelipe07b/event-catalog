package org.riwi.eventcatalog.infraestructura.adapters.in.web.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    private final int status;
    private final String error;
    private final String message;
    private final String path;
    private final String traceId;
    private final LocalDateTime timestamp;
    private List<SubError> validationErrors;

    public ErrorResponse(HttpStatus status, String message, String path) {
        this.status = status.value();
        this.error = status.getReasonPhrase();
        this.message = message;
        this.path = path;
        this.traceId = MDC.get("traceId");
        this.timestamp = LocalDateTime.now();
    }

    // Interfaz para sub-errores, útil para validación
    interface SubError {
    }

    // Clase para errores de validación específicos
    record ValidationError(String field, String message) implements SubError {
    }
}
