package com.universidad.biblioteca.mapper;

import com.universidad.biblioteca.dto.request.LibroRequest;
import com.universidad.biblioteca.dto.response.LibroResponse;
import com.universidad.biblioteca.model.Libro;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LibroMapper {

    public Libro aEntidad(LibroRequest request) {
        Libro libro = new Libro();
        libro.setTitulo(request.titulo().trim());
        libro.setAutor(request.autor().trim());
        libro.setIsbn(request.isbn().trim());
        libro.setAnioPublicacion(request.anioPublicacion());
        libro.setEstado(request.estado());
        return libro;
    }

    public void copiarDatos(LibroRequest request, Libro destino) {
        destino.setTitulo(request.titulo().trim());
        destino.setAutor(request.autor().trim());
        destino.setIsbn(request.isbn().trim());
        destino.setAnioPublicacion(request.anioPublicacion());
        destino.setEstado(request.estado());
    }

    public LibroResponse aRespuesta(Libro libro) {
        return new LibroResponse(
                libro.getId(),
                libro.getTitulo(),
                libro.getAutor(),
                libro.getIsbn(),
                libro.getAnioPublicacion(),
                libro.getEstado());
    }

    public List<LibroResponse> aRespuestas(List<Libro> libros) {
        return libros.stream().map(this::aRespuesta).toList();
    }
}
