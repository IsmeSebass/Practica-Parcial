package com.universidad.biblioteca.dto.response;

import com.universidad.biblioteca.model.EstadoLibro;

public record LibroResponse(

        Long id,

        String titulo,

        String autor,

        String isbn,

        Integer anioPublicacion,

        EstadoLibro estado
) {
}
