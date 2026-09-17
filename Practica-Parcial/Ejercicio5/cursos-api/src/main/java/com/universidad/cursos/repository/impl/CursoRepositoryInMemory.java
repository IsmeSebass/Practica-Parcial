package com.universidad.cursos.repository.impl;

import com.universidad.cursos.model.Curso;
import com.universidad.cursos.model.EstadoCurso;
import com.universidad.cursos.repository.CursoRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class CursoRepositoryInMemory implements CursoRepository {

    private final List<Curso> cursos = new CopyOnWriteArrayList<>();
    private final AtomicLong secuenciaId = new AtomicLong(0);

    @PostConstruct
    void cargarDatosIniciales() {
        guardar(new Curso(null, "Programacion Orientada a Objetos", "INF-201", 4, EstadoCurso.ACTIVO));
        guardar(new Curso(null, "Base de Datos I", "INF-202", 4, EstadoCurso.ACTIVO));
        guardar(new Curso(null, "Calculo II", "MAT-102", 5, EstadoCurso.INACTIVO));
    }

    @Override
    public Curso guardar(Curso curso) {
        if (curso.getId() == null) {
            curso.setId(secuenciaId.incrementAndGet());
            cursos.add(curso);
            return curso;
        }
        for (int i = 0; i < cursos.size(); i++) {
            if (cursos.get(i).getId().equals(curso.getId())) {
                cursos.set(i, curso);
                return curso;
            }
        }
        cursos.add(curso);
        return curso;
    }

    @Override
    public List<Curso> buscarTodos() {
        return List.copyOf(cursos);
    }

    @Override
    public Optional<Curso> buscarPorId(Long id) {
        return cursos.stream()
                .filter(curso -> curso.getId().equals(id))
                .findFirst();
    }

    @Override
    public Optional<Curso> buscarPorCodigo(String codigo) {
        return cursos.stream()
                .filter(curso -> curso.getCodigo().equalsIgnoreCase(codigo))
                .findFirst();
    }

    @Override
    public boolean eliminarPorId(Long id) {
        return cursos.removeIf(curso -> curso.getId().equals(id));
    }
}
