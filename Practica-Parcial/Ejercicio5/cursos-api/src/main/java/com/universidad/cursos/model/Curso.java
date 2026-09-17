package com.universidad.cursos.model;

import java.util.Objects;

public class Curso {

    private Long id;
    private String nombre;
    private String codigo;
    private Integer creditos;
    private EstadoCurso estado;

    public Curso() {
    }

    public Curso(Long id, String nombre, String codigo, Integer creditos, EstadoCurso estado) {
        this.id = id;
        this.nombre = nombre;
        this.codigo = codigo;
        this.creditos = creditos;
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Integer getCreditos() {
        return creditos;
    }

    public void setCreditos(Integer creditos) {
        this.creditos = creditos;
    }

    public EstadoCurso getEstado() {
        return estado;
    }

    public void setEstado(EstadoCurso estado) {
        this.estado = estado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Curso otro)) {
            return false;
        }
        return Objects.equals(id, otro.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Curso{id=%d, nombre='%s', codigo='%s', creditos=%d, estado=%s}"
                .formatted(id, nombre, codigo, creditos, estado);
    }
}
