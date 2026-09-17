package com.universidad.cursos.service;

import com.universidad.cursos.dto.request.CursoRequest;
import com.universidad.cursos.dto.response.CursoResponse;
import com.universidad.cursos.model.EstadoCurso;

import java.util.List;

public interface CursoService {

    CursoResponse crear(CursoRequest request);

    List<CursoResponse> consultarTodos(EstadoCurso estado, Integer creditos);

    CursoResponse consultarPorId(Long id);

    CursoResponse consultarPorCodigo(String codigo);

    CursoResponse actualizar(Long id, CursoRequest request);

    void eliminar(Long id);
}
