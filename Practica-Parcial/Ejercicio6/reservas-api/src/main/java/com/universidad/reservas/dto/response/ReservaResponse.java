package com.universidad.reservas.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.universidad.reservas.model.EstadoReserva;

import java.time.LocalDate;

public record ReservaResponse(

        Long id,

        String nombreCliente,

        String habitacion,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        LocalDate fechaEntrada,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        LocalDate fechaSalida,

        EstadoReserva estado,

        long cantidadNoches
) {
}
