package com.universidad.reservas.controller;

import com.universidad.reservas.dto.request.CancelacionRequest;
import com.universidad.reservas.dto.request.ReservaRequest;
import com.universidad.reservas.dto.response.ReservaResponse;
import com.universidad.reservas.model.EstadoReserva;
import com.universidad.reservas.service.ReservaService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
@RequestMapping(value = "/api/v1/reservas", produces = MediaType.APPLICATION_JSON_VALUE)
public class ReservaController {

    private final ReservaService servicio;

    public ReservaController(ReservaService servicio) {
        this.servicio = servicio;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReservaResponse> crear(@Valid @RequestBody ReservaRequest request) {
        ReservaResponse creada = servicio.crear(request);
        return ResponseEntity
                .created(URI.create("/api/v1/reservas/" + creada.id()))
                .body(creada);
    }

    @GetMapping
    public ResponseEntity<List<ReservaResponse>> consultarTodas(
            @RequestParam(required = false) EstadoReserva estado,
            @RequestParam(required = false) String habitacion) {
        return ResponseEntity.ok(servicio.consultarTodas(estado, habitacion));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservaResponse> consultarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(servicio.consultarPorId(id));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReservaResponse> actualizar(@PathVariable Long id,
                                                      @Valid @RequestBody ReservaRequest request) {
        return ResponseEntity.ok(servicio.actualizar(id, request));
    }

    @PostMapping("/{id}/cancelacion")
    public ResponseEntity<ReservaResponse> cancelar(
            @PathVariable Long id,
            @Valid @RequestBody(required = false) CancelacionRequest request) {
        return ResponseEntity.ok(servicio.cancelar(id, request));
    }
}
