package com.universidad.biblioteca.service;

import com.universidad.biblioteca.dto.request.LibroRequest;
import com.universidad.biblioteca.dto.response.LibroResponse;
import com.universidad.biblioteca.exception.RecursoDuplicadoException;
import com.universidad.biblioteca.exception.RecursoNoEncontradoException;
import com.universidad.biblioteca.mapper.LibroMapper;
import com.universidad.biblioteca.model.EstadoLibro;
import com.universidad.biblioteca.model.Libro;
import com.universidad.biblioteca.repository.LibroRepository;
import com.universidad.biblioteca.service.impl.LibroServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LibroServiceImplTest {

    private LibroService servicio;

    @BeforeEach
    void prepararEscenario() {
        servicio = new LibroServiceImpl(new RepositorioDePrueba(), new LibroMapper());
        servicio.registrar(new LibroRequest("Rayuela", "Julio Cortazar",
                "978-84-376-0495-4", 1963, EstadoLibro.DISPONIBLE));
    }

    @Test
    void registrarAsignaId() {
        LibroResponse creado = servicio.registrar(new LibroRequest("El Aleph", "Jorge Luis Borges",
                "978-84-206-3311-1", 1949, EstadoLibro.DISPONIBLE));

        assertEquals(2L, creado.id());
        assertEquals("El Aleph", creado.titulo());
        assertEquals(EstadoLibro.DISPONIBLE, creado.estado());
    }

    @Test
    void registrarRechazaIsbnDuplicado() {
        LibroRequest duplicado = new LibroRequest("Otro titulo", "Otro autor",
                "978-84-376-0495-4", 1980, EstadoLibro.DISPONIBLE);

        assertThrows(RecursoDuplicadoException.class, () -> servicio.registrar(duplicado));
    }

    @Test
    void consultarPorTituloIgnoraMayusculas() {
        assertEquals("Rayuela", servicio.consultarPorTitulo("rayuela").titulo());
    }

    @Test
    void consultarPorTituloInexistente() {
        assertThrows(RecursoNoEncontradoException.class, () -> servicio.consultarPorTitulo("El Quijote"));
    }

    @Test
    void consultarTodosFiltraPorEstado() {
        servicio.registrar(new LibroRequest("Ficciones", "Jorge Luis Borges",
                "978-84-206-3312-8", 1944, EstadoLibro.PRESTADO));

        List<LibroResponse> prestados = servicio.consultarTodos(null, EstadoLibro.PRESTADO);

        assertEquals(1, prestados.size());
        assertEquals("Ficciones", prestados.get(0).titulo());
    }

    @Test
    void actualizarConservaSuIsbn() {
        LibroResponse actualizado = servicio.actualizar(1L, new LibroRequest("Rayuela", "Julio Cortazar",
                "978-84-376-0495-4", 1963, EstadoLibro.PRESTADO));

        assertEquals(EstadoLibro.PRESTADO, actualizado.estado());
    }

    @Test
    void eliminarQuitaElLibro() {
        servicio.eliminar(1L);

        assertTrue(servicio.consultarTodos(null, null).isEmpty());
        assertThrows(RecursoNoEncontradoException.class, () -> servicio.eliminar(1L));
    }

    private static class RepositorioDePrueba implements LibroRepository {

        private final List<Libro> libros = new ArrayList<>();
        private final AtomicLong secuencia = new AtomicLong(0);

        @Override
        public Libro guardar(Libro libro) {
            if (libro.getId() == null) {
                libro.setId(secuencia.incrementAndGet());
                libros.add(libro);
            }
            return libro;
        }

        @Override
        public List<Libro> buscarTodos() {
            return List.copyOf(libros);
        }

        @Override
        public Optional<Libro> buscarPorId(Long id) {
            return libros.stream().filter(l -> l.getId().equals(id)).findFirst();
        }

        @Override
        public Optional<Libro> buscarPorTitulo(String titulo) {
            return libros.stream().filter(l -> l.getTitulo().equalsIgnoreCase(titulo)).findFirst();
        }

        @Override
        public Optional<Libro> buscarPorIsbn(String isbn) {
            return libros.stream().filter(l -> l.getIsbn().equalsIgnoreCase(isbn)).findFirst();
        }

        @Override
        public boolean eliminarPorId(Long id) {
            return libros.removeIf(l -> l.getId().equals(id));
        }
    }
}
