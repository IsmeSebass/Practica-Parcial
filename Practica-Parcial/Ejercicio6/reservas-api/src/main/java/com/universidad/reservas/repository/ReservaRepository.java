package com.universidad.reservas.repository;

import com.universidad.reservas.model.Reserva;

import java.util.List;
import java.util.Optional;

public interface ReservaRepository {

    Reserva guardar(Reserva reserva);

    List<Reserva> buscarTodas();

    Optional<Reserva> buscarPorId(Long id);

    List<Reserva> buscarPorHabitacion(String habitacion);
}
