package com.universidad.reservas.mapper;

import com.universidad.reservas.dto.request.ReservaRequest;
import com.universidad.reservas.dto.response.ReservaResponse;
import com.universidad.reservas.model.Reserva;
import org.springframework.stereotype.Component;

import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
public class ReservaMapper {

    public Reserva aEntidad(ReservaRequest request) {
        Reserva reserva = new Reserva();
        copiarDatos(request, reserva);
        return reserva;
    }

    public void copiarDatos(ReservaRequest request, Reserva destino) {
        destino.setNombreCliente(request.nombreCliente().trim());
        destino.setHabitacion(request.habitacion().trim().toUpperCase());
        destino.setFechaEntrada(request.fechaEntrada());
        destino.setFechaSalida(request.fechaSalida());
        destino.setEstado(request.estado());
    }

    public ReservaResponse aRespuesta(Reserva reserva) {
        long noches = ChronoUnit.DAYS.between(reserva.getFechaEntrada(), reserva.getFechaSalida());
        return new ReservaResponse(
                reserva.getId(),
                reserva.getNombreCliente(),
                reserva.getHabitacion(),
                reserva.getFechaEntrada(),
                reserva.getFechaSalida(),
                reserva.getEstado(),
                noches);
    }

    public List<ReservaResponse> aRespuestas(List<Reserva> reservas) {
        return reservas.stream().map(this::aRespuesta).toList();
    }
}
