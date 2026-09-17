package com.universidad.cursos.controller;

import com.universidad.cursos.dto.request.CursoRequest;
import com.universidad.cursos.dto.response.CursoResponse;
import com.universidad.cursos.model.EstadoCurso;
import com.universidad.cursos.service.CursoService;
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
@RequestMapping(value = "/api/v1/cursos", produces = MediaType.APPLICATION_JSON_VALUE)
public class CursoController {

    private final CursoService servicio;

    public CursoController(CursoService servicio) {
        this.servicio = servicio;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CursoResponse> crear(@Valid @RequestBody CursoRequest request) {
        CursoResponse creado = servicio.crear(request);
        return ResponseEntity
                .created(URI.create("/api/v1/cursos/" + creado.id()))
                .body(creado);
    }

    @GetMapping
    public ResponseEntity<List<CursoResponse>> consultarTodos(
            @RequestParam(required = false) EstadoCurso estado,
            @RequestParam(required = false) Integer creditos) {
        return ResponseEntity.ok(servicio.consultarTodos(estado, creditos));
    }

    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<CursoResponse> consultarPorCodigo(@PathVariable String codigo) {
        return ResponseEntity.ok(servicio.consultarPorCodigo(codigo));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CursoResponse> consultarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(servicio.consultarPorId(id));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CursoResponse> actualizar(@PathVariable Long id,
                                                    @Valid @RequestBody CursoRequest request) {
        return ResponseEntity.ok(servicio.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        servicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
