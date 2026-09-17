package com.universidad.cursos.exception;

import com.universidad.cursos.dto.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> manejarNoEncontrado(RecursoNoEncontradoException ex,
                                                             HttpServletRequest peticion) {
        return construir(HttpStatus.NOT_FOUND, ex.getMessage(), peticion, List.of());
    }

    @ExceptionHandler(RecursoDuplicadoException.class)
    public ResponseEntity<ErrorResponse> manejarDuplicado(RecursoDuplicadoException ex,
                                                          HttpServletRequest peticion) {
        return construir(HttpStatus.CONFLICT, ex.getMessage(), peticion, List.of());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> manejarValidacion(MethodArgumentNotValidException ex,
                                                           HttpServletRequest peticion) {
        List<String> detalles = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> "%s: %s".formatted(error.getField(), error.getDefaultMessage()))
                .sorted()
                .toList();
        return construir(HttpStatus.BAD_REQUEST, "Error de validacion en los datos enviados",
                peticion, detalles);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> manejarJsonInvalido(HttpMessageNotReadableException ex,
                                                             HttpServletRequest peticion) {
        return construir(HttpStatus.BAD_REQUEST,
                "El cuerpo de la peticion no es un JSON valido o contiene valores no admitidos",
                peticion, List.of());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> manejarTipoInvalido(MethodArgumentTypeMismatchException ex,
                                                             HttpServletRequest peticion) {
        String mensaje = "El parametro '%s' tiene un valor invalido: %s"
                .formatted(ex.getName(), ex.getValue());
        return construir(HttpStatus.BAD_REQUEST, mensaje, peticion, List.of());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> manejarErrorInesperado(Exception ex,
                                                                HttpServletRequest peticion) {
        log.error("Error inesperado procesando {}", peticion.getRequestURI(), ex);
        return construir(HttpStatus.INTERNAL_SERVER_ERROR,
                "Ocurrio un error inesperado. Contacte al administrador.", peticion, List.of());
    }

    private ResponseEntity<ErrorResponse> construir(HttpStatus estado, String mensaje,
                                                    HttpServletRequest peticion, List<String> detalles) {
        ErrorResponse cuerpo = ErrorResponse.of(estado.value(), estado.getReasonPhrase(),
                mensaje, peticion.getRequestURI(), detalles);
        return ResponseEntity.status(estado).body(cuerpo);
    }
}
