package com.universidad.cursos.mapper;

import com.universidad.cursos.dto.request.CursoRequest;
import com.universidad.cursos.dto.response.CursoResponse;
import com.universidad.cursos.model.Curso;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CursoMapper {

    public Curso aEntidad(CursoRequest request) {
        Curso curso = new Curso();
        copiarDatos(request, curso);
        return curso;
    }

    public void copiarDatos(CursoRequest request, Curso destino) {
        destino.setNombre(request.nombre().trim());
        destino.setCodigo(request.codigo().trim().toUpperCase());
        destino.setCreditos(request.creditos());
        destino.setEstado(request.estado());
    }

    public CursoResponse aRespuesta(Curso curso) {
        return new CursoResponse(
                curso.getId(),
                curso.getNombre(),
                curso.getCodigo(),
                curso.getCreditos(),
                curso.getEstado());
    }

    public List<CursoResponse> aRespuestas(List<Curso> cursos) {
        return cursos.stream().map(this::aRespuesta).toList();
    }
}
