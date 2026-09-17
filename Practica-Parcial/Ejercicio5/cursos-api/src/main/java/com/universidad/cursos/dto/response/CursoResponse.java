package com.universidad.cursos.dto.response;

import com.universidad.cursos.model.EstadoCurso;

public record CursoResponse(

        Long id,

        String nombre,

        String codigo,

        Integer creditos,

        EstadoCurso estado
) {
}
