package com.universidad.reservas.model;

import java.time.LocalDate;
import java.util.Objects;

public class Reserva {

    private Long id;
    private String nombreCliente;
    private String habitacion;
    private LocalDate fechaEntrada;
    private LocalDate fechaSalida;
    private EstadoReserva estado;

    public Reserva() {
    }

    public Reserva(Long id, String nombreCliente, String habitacion,
                   LocalDate fechaEntrada, LocalDate fechaSalida, EstadoReserva estado) {
        this.id = id;
        this.nombreCliente = nombreCliente;
        this.habitacion = habitacion;
        this.fechaEntrada = fechaEntrada;
        this.fechaSalida = fechaSalida;
        this.estado = estado;
    }

    public boolean seSolapaCon(LocalDate entrada, LocalDate salida) {
        return fechaEntrada.isBefore(salida) && entrada.isBefore(fechaSalida);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getHabitacion() {
        return habitacion;
    }

    public void setHabitacion(String habitacion) {
        this.habitacion = habitacion;
    }

    public LocalDate getFechaEntrada() {
        return fechaEntrada;
    }

    public void setFechaEntrada(LocalDate fechaEntrada) {
        this.fechaEntrada = fechaEntrada;
    }

    public LocalDate getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(LocalDate fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public EstadoReserva getEstado() {
        return estado;
    }

    public void setEstado(EstadoReserva estado) {
        this.estado = estado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Reserva otra)) {
            return false;
        }
        return Objects.equals(id, otra.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Reserva{id=%d, nombreCliente='%s', habitacion='%s', fechaEntrada=%s, fechaSalida=%s, estado=%s}"
                .formatted(id, nombreCliente, habitacion, fechaEntrada, fechaSalida, estado);
    }
}
