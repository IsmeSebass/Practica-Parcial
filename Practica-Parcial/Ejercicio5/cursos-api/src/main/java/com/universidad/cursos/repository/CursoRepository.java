package com.universidad.cursos.repository;

import com.universidad.cursos.model.Curso;

import java.util.List;
import java.util.Optional;

public interface CursoRepository {

    Curso guardar(Curso curso);

    List<Curso> buscarTodos();

    Optional<Curso> buscarPorId(Long id);

    Optional<Curso> buscarPorCodigo(String codigo);

    boolean eliminarPorId(Long id);
}
