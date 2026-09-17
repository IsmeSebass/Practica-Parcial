package com.universidad.cursos.service;

import com.universidad.cursos.dto.request.CursoRequest;
import com.universidad.cursos.dto.response.CursoResponse;
import com.universidad.cursos.exception.RecursoDuplicadoException;
import com.universidad.cursos.exception.RecursoNoEncontradoException;
import com.universidad.cursos.mapper.CursoMapper;
import com.universidad.cursos.model.Curso;
import com.universidad.cursos.model.EstadoCurso;
import com.universidad.cursos.repository.CursoRepository;
import com.universidad.cursos.service.impl.CursoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CursoServiceImplTest {

    private CursoService servicio;

    @BeforeEach
    void prepararEscenario() {
        servicio = new CursoServiceImpl(new RepositorioDePrueba(), new CursoMapper());
        servicio.crear(new CursoRequest("Programacion Orientada a Objetos", "INF-201", 4, EstadoCurso.ACTIVO));
    }

    @Test
    void crearAsignaIdYNormaliza() {
        CursoResponse creado = servicio.crear(new CursoRequest("Base de Datos I", "inf-202", 4, EstadoCurso.ACTIVO));

        assertEquals(2L, creado.id());
        assertEquals("INF-202", creado.codigo());
    }

    @Test
    void crearRechazaCodigoDuplicado() {
        CursoRequest duplicado = new CursoRequest("Otro curso", "INF-201", 3, EstadoCurso.ACTIVO);

        assertThrows(RecursoDuplicadoException.class, () -> servicio.crear(duplicado));
    }

    @Test
    void consultarPorCodigoIgnoraMayusculas() {
        assertEquals("INF-201", servicio.consultarPorCodigo("inf-201").codigo());
    }

    @Test
    void consultarPorCodigoInexistente() {
        assertThrows(RecursoNoEncontradoException.class, () -> servicio.consultarPorCodigo("INF-999"));
    }

    @Test
    void consultarTodosFiltraPorCreditos() {
        servicio.crear(new CursoRequest("Calculo II", "MAT-102", 5, EstadoCurso.ACTIVO));

        List<CursoResponse> deCincoCreditos = servicio.consultarTodos(null, 5);

        assertEquals(1, deCincoCreditos.size());
        assertEquals("MAT-102", deCincoCreditos.get(0).codigo());
    }

    @Test
    void actualizarConservaSuCodigo() {
        CursoResponse actualizado = servicio.actualizar(1L,
                new CursoRequest("Programacion Orientada a Objetos II", "INF-201", 5, EstadoCurso.CERRADO));

        assertEquals(5, actualizado.creditos());
        assertEquals(EstadoCurso.CERRADO, actualizado.estado());
    }

    @Test
    void eliminarQuitaElCurso() {
        servicio.eliminar(1L);

        assertTrue(servicio.consultarTodos(null, null).isEmpty());
        assertThrows(RecursoNoEncontradoException.class, () -> servicio.eliminar(1L));
    }

    private static class RepositorioDePrueba implements CursoRepository {

        private final List<Curso> cursos = new ArrayList<>();
        private final AtomicLong secuencia = new AtomicLong(0);

        @Override
        public Curso guardar(Curso curso) {
            if (curso.getId() == null) {
                curso.setId(secuencia.incrementAndGet());
                cursos.add(curso);
            }
            return curso;
        }

        @Override
        public List<Curso> buscarTodos() {
            return List.copyOf(cursos);
        }

        @Override
        public Optional<Curso> buscarPorId(Long id) {
            return cursos.stream().filter(c -> c.getId().equals(id)).findFirst();
        }

        @Override
        public Optional<Curso> buscarPorCodigo(String codigo) {
            return cursos.stream().filter(c -> c.getCodigo().equalsIgnoreCase(codigo)).findFirst();
        }

        @Override
        public boolean eliminarPorId(Long id) {
            return cursos.removeIf(c -> c.getId().equals(id));
        }
    }
}
