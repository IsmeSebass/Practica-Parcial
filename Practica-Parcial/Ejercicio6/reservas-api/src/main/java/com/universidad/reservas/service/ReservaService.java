package com.universidad.reservas.service;

import com.universidad.reservas.dto.request.CancelacionRequest;
import com.universidad.reservas.dto.request.ReservaRequest;
import com.universidad.reservas.dto.response.ReservaResponse;
import com.universidad.reservas.model.EstadoReserva;

import java.util.List;

public interface ReservaService {

    ReservaResponse crear(ReservaRequest request);

    List<ReservaResponse> consultarTodas(EstadoReserva estado, String habitacion);

    ReservaResponse consultarPorId(Long id);

    ReservaResponse actualizar(Long id, ReservaRequest request);

    ReservaResponse cancelar(Long id, CancelacionRequest request);
}
