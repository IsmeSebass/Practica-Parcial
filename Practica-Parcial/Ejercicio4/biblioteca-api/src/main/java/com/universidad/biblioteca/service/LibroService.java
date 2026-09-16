package com.universidad.biblioteca.service;

import com.universidad.biblioteca.dto.request.LibroRequest;
import com.universidad.biblioteca.dto.response.LibroResponse;
import com.universidad.biblioteca.model.EstadoLibro;

import java.util.List;

public interface LibroService {

    LibroResponse registrar(LibroRequest request);

    List<LibroResponse> consultarTodos(String autor, EstadoLibro estado);

    LibroResponse consultarPorId(Long id);

    LibroResponse consultarPorTitulo(String titulo);

    LibroResponse actualizar(Long id, LibroRequest request);

    void eliminar(Long id);
}
