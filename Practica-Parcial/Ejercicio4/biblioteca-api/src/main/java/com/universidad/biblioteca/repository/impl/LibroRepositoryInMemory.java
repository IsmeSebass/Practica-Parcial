package com.universidad.biblioteca.repository.impl;

import com.universidad.biblioteca.model.EstadoLibro;
import com.universidad.biblioteca.model.Libro;
import com.universidad.biblioteca.repository.LibroRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class LibroRepositoryInMemory implements LibroRepository {

    private final List<Libro> libros = new CopyOnWriteArrayList<>();
    private final AtomicLong secuenciaId = new AtomicLong(0);

    @PostConstruct
    void cargarDatosIniciales() {
        guardar(new Libro(null, "Cien anios de soledad", "Gabriel Garcia Marquez",
                "978-84-376-0494-7", 1967, EstadoLibro.DISPONIBLE));
        guardar(new Libro(null, "El principito", "Antoine de Saint-Exupery",
                "978-84-204-8054-9", 1943, EstadoLibro.PRESTADO));
        guardar(new Libro(null, "Rayuela", "Julio Cortazar",
                "978-84-376-0495-4", 1963, EstadoLibro.DISPONIBLE));
    }

    @Override
    public Libro guardar(Libro libro) {
        if (libro.getId() == null) {
            libro.setId(secuenciaId.incrementAndGet());
            libros.add(libro);
            return libro;
        }
        for (int i = 0; i < libros.size(); i++) {
            if (libros.get(i).getId().equals(libro.getId())) {
                libros.set(i, libro);
                return libro;
            }
        }
        libros.add(libro);
        return libro;
    }

    @Override
    public List<Libro> buscarTodos() {
        return List.copyOf(libros);
    }

    @Override
    public Optional<Libro> buscarPorId(Long id) {
        return libros.stream()
                .filter(libro -> libro.getId().equals(id))
                .findFirst();
    }

    @Override
    public Optional<Libro> buscarPorTitulo(String titulo) {
        return libros.stream()
                .filter(libro -> libro.getTitulo().equalsIgnoreCase(titulo))
                .findFirst();
    }

    @Override
    public Optional<Libro> buscarPorIsbn(String isbn) {
        return libros.stream()
                .filter(libro -> libro.getIsbn().equalsIgnoreCase(isbn))
                .findFirst();
    }

    @Override
    public boolean eliminarPorId(Long id) {
        return libros.removeIf(libro -> libro.getId().equals(id));
    }
}
