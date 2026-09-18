package com.universidad.reservas.service;

import com.universidad.reservas.dto.request.CancelacionRequest;
import com.universidad.reservas.dto.request.ReservaRequest;
import com.universidad.reservas.dto.response.ReservaResponse;
import com.universidad.reservas.exception.RecursoDuplicadoException;
import com.universidad.reservas.exception.RecursoNoEncontradoException;
import com.universidad.reservas.exception.ReglaNegocioException;
import com.universidad.reservas.mapper.ReservaMapper;
import com.universidad.reservas.model.EstadoReserva;
import com.universidad.reservas.model.Reserva;
import com.universidad.reservas.repository.ReservaRepository;
import com.universidad.reservas.service.impl.ReservaServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ReservaServiceImplTest {

    private static final LocalDate HOY = LocalDate.now();

    private ReservaService servicio;

    @BeforeEach
    void prepararEscenario() {
        servicio = new ReservaServiceImpl(new RepositorioDePrueba(), new ReservaMapper());
        servicio.crear(nuevaReserva("Maria Fernandez", "301-A", 10, 14, EstadoReserva.CONFIRMADA));
    }

    @Test
    void crearCalculaNoches() {
        ReservaResponse creada = servicio.crear(nuevaReserva("Carlos Rojas", "302-B", 2, 5, EstadoReserva.CONFIRMADA));

        assertEquals(2L, creada.id());
        assertEquals(3L, creada.cantidadNoches());
    }

    @Test
    void crearRechazaSolapamiento() {
        ReservaRequest solapada = nuevaReserva("Otro cliente", "301-A", 12, 16, EstadoReserva.CONFIRMADA);

        assertThrows(RecursoDuplicadoException.class, () -> servicio.crear(solapada));
    }

    @Test
    void crearAdmiteFechasContiguas() {
        assertDoesNotThrow(() -> servicio.crear(
                nuevaReserva("Cliente siguiente", "301-A", 14, 18, EstadoReserva.CONFIRMADA)));
    }

    @Test
    void crearAdmiteHabitacionLiberadaPorCancelacion() {
        servicio.cancelar(1L, new CancelacionRequest("El cliente cambio de fechas"));

        assertDoesNotThrow(() -> servicio.crear(
                nuevaReserva("Nuevo cliente", "301-A", 12, 16, EstadoReserva.CONFIRMADA)));
    }

    @Test
    void consultarPorIdInexistente() {
        assertThrows(RecursoNoEncontradoException.class, () -> servicio.consultarPorId(99L));
    }

    @Test
    void consultarTodasFiltraPorHabitacion() {
        servicio.crear(nuevaReserva("Carlos Rojas", "302-B", 2, 5, EstadoReserva.CONFIRMADA));

        List<ReservaResponse> de302 = servicio.consultarTodas(null, "302-B");

        assertEquals(1, de302.size());
        assertEquals("Carlos Rojas", de302.get(0).nombreCliente());
    }

    @Test
    void cancelarCambiaElEstado() {
        ReservaResponse cancelada = servicio.cancelar(1L, new CancelacionRequest("Viaje reprogramado"));

        assertEquals(EstadoReserva.CANCELADA, cancelada.estado());
        assertEquals(1, servicio.consultarTodas(EstadoReserva.CANCELADA, null).size());
    }

    @Test
    void cancelarDosVecesFalla() {
        servicio.cancelar(1L, null);

        assertThrows(ReglaNegocioException.class, () -> servicio.cancelar(1L, null));
    }

    @Test
    void actualizarCanceladaFalla() {
        servicio.cancelar(1L, null);
        ReservaRequest cambio = nuevaReserva("Maria Fernandez", "301-A", 10, 14, EstadoReserva.CONFIRMADA);

        assertThrows(ReglaNegocioException.class, () -> servicio.actualizar(1L, cambio));
    }

    @Test
    void actualizarConservaSusFechas() {
        ReservaResponse actualizada = servicio.actualizar(1L,
                nuevaReserva("Maria Fernandez Soto", "301-A", 10, 14, EstadoReserva.EN_CURSO));

        assertEquals("Maria Fernandez Soto", actualizada.nombreCliente());
        assertEquals(EstadoReserva.EN_CURSO, actualizada.estado());
    }

    private static ReservaRequest nuevaReserva(String cliente, String habitacion,
                                               int diasHastaEntrada, int diasHastaSalida,
                                               EstadoReserva estado) {
        return new ReservaRequest(cliente, habitacion,
                HOY.plusDays(diasHastaEntrada), HOY.plusDays(diasHastaSalida), estado);
    }

    private static class RepositorioDePrueba implements ReservaRepository {

        private final List<Reserva> reservas = new ArrayList<>();
        private final AtomicLong secuencia = new AtomicLong(0);

        @Override
        public Reserva guardar(Reserva reserva) {
            if (reserva.getId() == null) {
                reserva.setId(secuencia.incrementAndGet());
                reservas.add(reserva);
            }
            return reserva;
        }

        @Override
        public List<Reserva> buscarTodas() {
            return List.copyOf(reservas);
        }

        @Override
        public Optional<Reserva> buscarPorId(Long id) {
            return reservas.stream().filter(r -> r.getId().equals(id)).findFirst();
        }

        @Override
        public List<Reserva> buscarPorHabitacion(String habitacion) {
            return reservas.stream().filter(r -> r.getHabitacion().equalsIgnoreCase(habitacion)).toList();
        }
    }
}
