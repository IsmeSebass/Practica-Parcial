package com.universidad.biblioteca.service.impl;

import com.universidad.biblioteca.dto.request.LibroRequest;
import com.universidad.biblioteca.dto.response.LibroResponse;
import com.universidad.biblioteca.exception.RecursoDuplicadoException;
import com.universidad.biblioteca.exception.RecursoNoEncontradoException;
import com.universidad.biblioteca.mapper.LibroMapper;
import com.universidad.biblioteca.model.EstadoLibro;
import com.universidad.biblioteca.model.Libro;
import com.universidad.biblioteca.repository.LibroRepository;
import com.universidad.biblioteca.service.LibroService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LibroServiceImpl implements LibroService {

    private final LibroRepository repositorio;
    private final LibroMapper mapper;

    public LibroServiceImpl(LibroRepository repositorio, LibroMapper mapper) {
        this.repositorio = repositorio;
        this.mapper = mapper;
    }

    @Override
    public LibroResponse registrar(LibroRequest request) {
        validarIsbnUnico(request.isbn().trim(), null);
        Libro guardado = repositorio.guardar(mapper.aEntidad(request));
        return mapper.aRespuesta(guardado);
    }

    @Override
    public List<LibroResponse> consultarTodos(String autor, EstadoLibro estado) {
        List<Libro> encontrados = repositorio.buscarTodos().stream()
                .filter(libro -> autor == null || libro.getAutor().toLowerCase().contains(autor.toLowerCase()))
                .filter(libro -> estado == null || libro.getEstado() == estado)
                .toList();
        return mapper.aRespuestas(encontrados);
    }

    @Override
    public LibroResponse consultarPorId(Long id) {
        return mapper.aRespuesta(obtenerLibro(id));
    }

    @Override
    public LibroResponse consultarPorTitulo(String titulo) {
        Libro libro = repositorio.buscarPorTitulo(titulo)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe un libro con el titulo '%s'".formatted(titulo)));
        return mapper.aRespuesta(libro);
    }

    @Override
    public LibroResponse actualizar(Long id, LibroRequest request) {
        Libro existente = obtenerLibro(id);
        validarIsbnUnico(request.isbn().trim(), id);
        mapper.copiarDatos(request, existente);
        return mapper.aRespuesta(repositorio.guardar(existente));
    }

    @Override
    public void eliminar(Long id) {
        if (!repositorio.eliminarPorId(id)) {
            throw new RecursoNoEncontradoException("No existe un libro con el id %d".formatted(id));
        }
    }

    private Libro obtenerLibro(Long id) {
        return repositorio.buscarPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe un libro con el id %d".formatted(id)));
    }

    private void validarIsbnUnico(String isbn, Long idAExcluir) {
        Optional<Libro> coincidencia = repositorio.buscarPorIsbn(isbn);
        if (coincidencia.isPresent() && !coincidencia.get().getId().equals(idAExcluir)) {
            throw new RecursoDuplicadoException(
                    "Ya existe un libro registrado con el ISBN %s".formatted(isbn));
        }
    }
}
