package com.universidad.reservas.dto.request;

import jakarta.validation.constraints.Size;

public record CancelacionRequest(

        @Size(max = 200, message = "el motivo no puede superar los 200 caracteres")
        String motivo
) {
}
