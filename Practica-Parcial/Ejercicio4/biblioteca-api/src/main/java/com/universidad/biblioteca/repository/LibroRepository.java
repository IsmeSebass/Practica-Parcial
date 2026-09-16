package com.universidad.biblioteca.repository;

import com.universidad.biblioteca.model.Libro;

import java.util.List;
import java.util.Optional;

public interface LibroRepository {

    Libro guardar(Libro libro);

    List<Libro> buscarTodos();

    Optional<Libro> buscarPorId(Long id);

    Optional<Libro> buscarPorTitulo(String titulo);

    Optional<Libro> buscarPorIsbn(String isbn);

    boolean eliminarPorId(Long id);
}
