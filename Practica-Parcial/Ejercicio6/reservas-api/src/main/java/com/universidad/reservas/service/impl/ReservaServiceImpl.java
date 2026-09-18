package com.universidad.reservas.service.impl;

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
import com.universidad.reservas.service.ReservaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservaServiceImpl implements ReservaService {

    private static final Logger log = LoggerFactory.getLogger(ReservaServiceImpl.class);

    private final ReservaRepository repositorio;
    private final ReservaMapper mapper;

    public ReservaServiceImpl(ReservaRepository repositorio, ReservaMapper mapper) {
        this.repositorio = repositorio;
        this.mapper = mapper;
    }

    @Override
    public ReservaResponse crear(ReservaRequest request) {
        validarDisponibilidad(request, null);
        Reserva guardada = repositorio.guardar(mapper.aEntidad(request));
        return mapper.aRespuesta(guardada);
    }

    @Override
    public List<ReservaResponse> consultarTodas(EstadoReserva estado, String habitacion) {
        List<Reserva> encontradas = repositorio.buscarTodas().stream()
                .filter(reserva -> estado == null || reserva.getEstado() == estado)
                .filter(reserva -> habitacion == null || reserva.getHabitacion().equalsIgnoreCase(habitacion))
                .toList();
        return mapper.aRespuestas(encontradas);
    }

    @Override
    public ReservaResponse consultarPorId(Long id) {
        return mapper.aRespuesta(obtenerReserva(id));
    }

    @Override
    public ReservaResponse actualizar(Long id, ReservaRequest request) {
        Reserva existente = obtenerReserva(id);
        if (existente.getEstado() == EstadoReserva.CANCELADA) {
            throw new ReglaNegocioException(
                    "La reserva %d esta CANCELADA y no puede modificarse".formatted(id));
        }
        validarDisponibilidad(request, id);
        mapper.copiarDatos(request, existente);
        return mapper.aRespuesta(repositorio.guardar(existente));
    }

    @Override
    public ReservaResponse cancelar(Long id, CancelacionRequest request) {
        Reserva reserva = obtenerReserva(id);
        if (!reserva.getEstado().esCancelable()) {
            throw new ReglaNegocioException(
                    "La reserva %d ya se encuentra %s".formatted(id, reserva.getEstado()));
        }
        reserva.setEstado(EstadoReserva.CANCELADA);
        String motivo = request == null || request.motivo() == null ? "sin motivo declarado" : request.motivo();
        log.info("Reserva {} cancelada. Motivo: {}", id, motivo);
        return mapper.aRespuesta(repositorio.guardar(reserva));
    }

    private Reserva obtenerReserva(Long id) {
        return repositorio.buscarPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe una reserva con el id %d".formatted(id)));
    }

    private void validarDisponibilidad(ReservaRequest request, Long idAExcluir) {
        String habitacion = request.habitacion().trim().toUpperCase();
        boolean ocupada = repositorio.buscarPorHabitacion(habitacion).stream()
                .filter(reserva -> !reserva.getId().equals(idAExcluir))
                .filter(reserva -> reserva.getEstado().ocupaHabitacion())
                .anyMatch(reserva -> reserva.seSolapaCon(request.fechaEntrada(), request.fechaSalida()));

        if (ocupada) {
            throw new RecursoDuplicadoException(
                    "La habitacion %s ya tiene una reserva entre %s y %s"
                            .formatted(habitacion, request.fechaEntrada(), request.fechaSalida()));
        }
    }
}
