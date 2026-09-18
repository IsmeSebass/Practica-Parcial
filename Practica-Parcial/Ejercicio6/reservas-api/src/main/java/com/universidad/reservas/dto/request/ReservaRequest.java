package com.universidad.reservas.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.universidad.reservas.model.EstadoReserva;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record ReservaRequest(

        @NotBlank(message = "el nombreCliente es obligatorio")
        @Size(min = 3, max = 120, message = "el nombreCliente debe tener entre 3 y 120 caracteres")
        String nombreCliente,

        @NotBlank(message = "la habitacion es obligatoria")
        @Pattern(regexp = "^[0-9]{3}-[A-Z]$", message = "la habitacion debe cumplir el formato 000-X")
        String habitacion,

        @NotNull(message = "la fechaEntrada es obligatoria")
        @FutureOrPresent(message = "la fechaEntrada no puede ser anterior al dia actual")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        LocalDate fechaEntrada,

        @NotNull(message = "la fechaSalida es obligatoria")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        LocalDate fechaSalida,

        @NotNull(message = "el estado es obligatorio")
        EstadoReserva estado
) {

    @JsonIgnore
    @AssertTrue(message = "la fechaSalida debe ser posterior a la fechaEntrada")
    public boolean isRangoDeFechasValido() {
        if (fechaEntrada == null || fechaSalida == null) {
            return true;
        }
        return fechaSalida.isAfter(fechaEntrada);
    }
}
