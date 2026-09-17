package com.universidad.cursos.dto.request;

import com.universidad.cursos.model.EstadoCurso;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CursoRequest(

        @NotBlank(message = "el nombre es obligatorio")
        @Size(min = 3, max = 120, message = "el nombre debe tener entre 3 y 120 caracteres")
        String nombre,

        @NotBlank(message = "el codigo es obligatorio")
        @Pattern(regexp = "^[A-Z]{2,4}-[0-9]{3}$", message = "el codigo debe cumplir el formato XXX-000")
        String codigo,

        @NotNull(message = "los creditos son obligatorios")
        @Min(value = 1, message = "los creditos deben ser mayor o igual a 1")
        @Max(value = 12, message = "los creditos deben ser menor o igual a 12")
        Integer creditos,

        @NotNull(message = "el estado es obligatorio")
        EstadoCurso estado
) {
}
