package com.universidad.biblioteca.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;

public record ErrorResponse(

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime timestamp,

        int status,

        String error,

        String message,

        String path,

        List<String> detalles
) {

    public static ErrorResponse of(int status, String error, String message, String path, List<String> detalles) {
        return new ErrorResponse(LocalDateTime.now(), status, error, message, path,
                detalles == null ? List.of() : detalles);
    }
}
