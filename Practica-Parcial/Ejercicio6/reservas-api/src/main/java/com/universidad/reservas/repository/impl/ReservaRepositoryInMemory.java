package com.universidad.reservas.repository.impl;

import com.universidad.reservas.model.EstadoReserva;
import com.universidad.reservas.model.Reserva;
import com.universidad.reservas.repository.ReservaRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class ReservaRepositoryInMemory implements ReservaRepository {

    private final List<Reserva> reservas = new CopyOnWriteArrayList<>();
    private final AtomicLong secuenciaId = new AtomicLong(0);

    @PostConstruct
    void cargarDatosIniciales() {
        LocalDate hoy = LocalDate.now();
        guardar(new Reserva(null, "Maria Fernandez", "301-A",
                hoy.plusDays(13), hoy.plusDays(17), EstadoReserva.CONFIRMADA));
        guardar(new Reserva(null, "Carlos Rojas", "302-B",
                hoy.plusDays(2), hoy.plusDays(5), EstadoReserva.CONFIRMADA));
        guardar(new Reserva(null, "Ana Quispe", "105-C",
                hoy.minusDays(10), hoy.minusDays(7), EstadoReserva.FINALIZADA));
    }

    @Override
    public Reserva guardar(Reserva reserva) {
        if (reserva.getId() == null) {
            reserva.setId(secuenciaId.incrementAndGet());
            reservas.add(reserva);
            return reserva;
        }
        for (int i = 0; i < reservas.size(); i++) {
            if (reservas.get(i).getId().equals(reserva.getId())) {
                reservas.set(i, reserva);
                return reserva;
            }
        }
        reservas.add(reserva);
        return reserva;
    }

    @Override
    public List<Reserva> buscarTodas() {
        return List.copyOf(reservas);
    }

    @Override
    public Optional<Reserva> buscarPorId(Long id) {
        return reservas.stream()
                .filter(reserva -> reserva.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Reserva> buscarPorHabitacion(String habitacion) {
        return reservas.stream()
                .filter(reserva -> reserva.getHabitacion().equalsIgnoreCase(habitacion))
                .toList();
    }
}
