package com.universidad.reservas.model;

public enum EstadoReserva {

    CONFIRMADA,

    EN_CURSO,

    FINALIZADA,

    CANCELADA;

    public boolean esCancelable() {
        return this == CONFIRMADA || this == EN_CURSO;
    }

    public boolean ocupaHabitacion() {
        return this == CONFIRMADA || this == EN_CURSO;
    }
}
