package com.universidad.biblioteca.controller;

import com.universidad.biblioteca.dto.request.LibroRequest;
import com.universidad.biblioteca.dto.response.LibroResponse;
import com.universidad.biblioteca.model.EstadoLibro;
import com.universidad.biblioteca.service.LibroService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/libros", produces = MediaType.APPLICATION_JSON_VALUE)
public class LibroController {

    private final LibroService servicio;

    public LibroController(LibroService servicio) {
        this.servicio = servicio;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LibroResponse> registrar(@Valid @RequestBody LibroRequest request) {
        LibroResponse creado = servicio.registrar(request);
        return ResponseEntity
                .created(URI.create("/api/v1/libros/" + creado.id()))
                .body(creado);
    }

    @GetMapping
    public ResponseEntity<List<LibroResponse>> consultarTodos(
            @RequestParam(required = false) String autor,
            @RequestParam(required = false) EstadoLibro estado) {
        return ResponseEntity.ok(servicio.consultarTodos(autor, estado));
    }

    @GetMapping("/titulo/{titulo}")
    public ResponseEntity<LibroResponse> consultarPorTitulo(@PathVariable String titulo) {
        return ResponseEntity.ok(servicio.consultarPorTitulo(titulo));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LibroResponse> consultarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(servicio.consultarPorId(id));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LibroResponse> actualizar(@PathVariable Long id,
                                                    @Valid @RequestBody LibroRequest request) {
        return ResponseEntity.ok(servicio.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        servicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
