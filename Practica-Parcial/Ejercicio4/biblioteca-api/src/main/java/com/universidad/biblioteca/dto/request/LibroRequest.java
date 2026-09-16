package com.universidad.biblioteca.dto.request;

import com.universidad.biblioteca.model.EstadoLibro;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record LibroRequest(

        @NotBlank(message = "el titulo es obligatorio")
        @Size(max = 150, message = "el titulo no puede superar los 150 caracteres")
        String titulo,

        @NotBlank(message = "el autor es obligatorio")
        @Size(max = 120, message = "el autor no puede superar los 120 caracteres")
        String autor,

        @NotBlank(message = "el isbn es obligatorio")
        @Pattern(regexp = "^[0-9Xx-]{10,17}$", message = "el isbn debe tener entre 10 y 17 caracteres numericos, X o guiones")
        String isbn,

        @NotNull(message = "el anioPublicacion es obligatorio")
        @Min(value = 1450, message = "el anioPublicacion debe ser mayor o igual a 1450")
        @Max(value = 2100, message = "el anioPublicacion debe ser menor o igual a 2100")
        Integer anioPublicacion,

        @NotNull(message = "el estado es obligatorio")
        EstadoLibro estado
) {
}
