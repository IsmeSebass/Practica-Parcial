package com.universidad.cursos.service.impl;

import com.universidad.cursos.dto.request.CursoRequest;
import com.universidad.cursos.dto.response.CursoResponse;
import com.universidad.cursos.exception.RecursoDuplicadoException;
import com.universidad.cursos.exception.RecursoNoEncontradoException;
import com.universidad.cursos.mapper.CursoMapper;
import com.universidad.cursos.model.Curso;
import com.universidad.cursos.model.EstadoCurso;
import com.universidad.cursos.repository.CursoRepository;
import com.universidad.cursos.service.CursoService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CursoServiceImpl implements CursoService {

    private final CursoRepository repositorio;
    private final CursoMapper mapper;

    public CursoServiceImpl(CursoRepository repositorio, CursoMapper mapper) {
        this.repositorio = repositorio;
        this.mapper = mapper;
    }

    @Override
    public CursoResponse crear(CursoRequest request) {
        validarCodigoUnico(request.codigo().trim().toUpperCase(), null);
        Curso guardado = repositorio.guardar(mapper.aEntidad(request));
        return mapper.aRespuesta(guardado);
    }

    @Override
    public List<CursoResponse> consultarTodos(EstadoCurso estado, Integer creditos) {
        List<Curso> encontrados = repositorio.buscarTodos().stream()
                .filter(curso -> estado == null || curso.getEstado() == estado)
                .filter(curso -> creditos == null || curso.getCreditos().equals(creditos))
                .toList();
        return mapper.aRespuestas(encontrados);
    }

    @Override
    public CursoResponse consultarPorId(Long id) {
        return mapper.aRespuesta(obtenerCurso(id));
    }

    @Override
    public CursoResponse consultarPorCodigo(String codigo) {
        Curso curso = repositorio.buscarPorCodigo(codigo)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe un curso con el codigo %s".formatted(codigo)));
        return mapper.aRespuesta(curso);
    }

    @Override
    public CursoResponse actualizar(Long id, CursoRequest request) {
        Curso existente = obtenerCurso(id);
        validarCodigoUnico(request.codigo().trim().toUpperCase(), id);
        mapper.copiarDatos(request, existente);
        return mapper.aRespuesta(repositorio.guardar(existente));
    }

    @Override
    public void eliminar(Long id) {
        if (!repositorio.eliminarPorId(id)) {
            throw new RecursoNoEncontradoException("No existe un curso con el id %d".formatted(id));
        }
    }

    private Curso obtenerCurso(Long id) {
        return repositorio.buscarPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe un curso con el id %d".formatted(id)));
    }

    private void validarCodigoUnico(String codigo, Long idAExcluir) {
        Optional<Curso> coincidencia = repositorio.buscarPorCodigo(codigo);
        if (coincidencia.isPresent() && !coincidencia.get().getId().equals(idAExcluir)) {
            throw new RecursoDuplicadoException(
                    "Ya existe un curso registrado con el codigo %s".formatted(codigo));
        }
    }
}
