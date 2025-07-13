package com.edutech.msvc.inscripcion.services;

import com.edutech.msvc.inscripcion.clients.AlumnoClientRest;
import com.edutech.msvc.inscripcion.clients.CursosClientRest;
import com.edutech.msvc.inscripcion.dtos.InscripcionUpdateDTO;
import com.edutech.msvc.inscripcion.exceptions.InscripcionException;
import com.edutech.msvc.inscripcion.models.Alumnos;
import com.edutech.msvc.inscripcion.models.Cursos;
import com.edutech.msvc.inscripcion.models.entities.Inscripcion;
import com.edutech.msvc.inscripcion.repositories.InscripcionRepository;
import feign.FeignException;
import feign.Request;
import feign.Response;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.charset.Charset;
import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InscripcionServiceTest {

    @Mock
    private InscripcionRepository inscripcionRepository;

    @InjectMocks
    private InscripcionServiceImpl inscripcionService;

    @Mock
    private AlumnoClientRest alumnoClientRest;

    @Mock
    private CursosClientRest cursosClientRest;

    private Inscripcion inscripcionPrueba;

    private List<Inscripcion> inscripciones = new ArrayList<>();

    @BeforeEach
    public void setUp(){
        this.inscripcionPrueba = new Inscripcion(
                LocalDate.parse("2020-06-23"), Long.valueOf(1L), Long.valueOf(1L)
        );
        Faker faker = new Faker(Locale.of("es", "CL"));
        Random rand = new Random();
        for(int i=0;i<100;i++){
            Inscripcion inscripcion = new Inscripcion();
            inscripcion.setFechaInscripcion(faker.timeAndDate().birthday(1, 2));
            inscripcion.setIdAlumno(Long.valueOf(rand.nextLong(100L)));
            inscripcion.setIdCurso(Long.valueOf(rand.nextLong(100L)));

            inscripciones.add(inscripcion);
        }
    }

    @Test
    @DisplayName("Devuelve todas las inscripciones")
    public void shouldFindAllInscripciones(){
        List<Inscripcion> inscripcions = this.inscripciones;
        inscripcions.add(inscripcionPrueba);
        when(inscripcionRepository.findAll()).thenReturn(inscripciones);

        List<Inscripcion> result = inscripcionRepository.findAll();

        assertThat(result).hasSize(101);
        assertThat(result).contains(inscripcionPrueba);

        verify(inscripcionRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Encontrar por ID una inscripcion")
    public void shouldFindInscripcionById(){
        when(inscripcionRepository.findById(Long.valueOf(1L))).thenReturn(Optional.of(inscripcionPrueba));
        Inscripcion result = inscripcionService.findById(Long.valueOf(1L));
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(this.inscripcionPrueba);

        verify(inscripcionRepository, times(1)).findById(Long.valueOf(1L));
    }

    @Test
    @DisplayName("Encontrar por id una inscripcion que no existe")
    public void shouldNotFindInscripcionById(){
        Long idInexisente = Long.valueOf(10067L);
        when(inscripcionRepository.findById(idInexisente)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->{
            inscripcionService.findById(idInexisente);
        }).isInstanceOf(InscripcionException.class)
                .hasMessageContaining("La inscripcion con id: " + idInexisente +
                        " no se encuentra en la base de datos");
        verify(inscripcionRepository, times(1)).findById(idInexisente);
    }


    @Test
    @DisplayName("Debería guardar una inscripcion")
    void shouldSaveInscripcion() {
        Inscripcion inscripcionPrueba = new Inscripcion(null, LocalDate.now(), 2L, 1L);
        Inscripcion inscripcionGuardada = new Inscripcion(1L, LocalDate.now(), 2L, 1L);

        // Simulamos que los clientes Feign encuentran los IDs de Alumno y Curso
        when(alumnoClientRest.findById(1L)).thenReturn(new Alumnos(1L, "Alumno Test"));
        when(cursosClientRest.findById(2L)).thenReturn(new Cursos(2L, "Curso Test"));

        when(inscripcionRepository.save(any(Inscripcion.class))).thenReturn(inscripcionGuardada);

        Inscripcion result = inscripcionService.save(inscripcionPrueba);

        assertNotNull(result);
        assertEquals(inscripcionGuardada, result);

        verify(alumnoClientRest, times(1)).findById(1L);
        verify(cursosClientRest, times(1)).findById(2L);
        verify(inscripcionRepository, times(1)).save(any(Inscripcion.class));
    }

    @Test
    @DisplayName("No debería guardar una inscripcion_alumno")
    void shouldNotSaveInscripcion_AlumnoFeignException() {
        Long alumnoIdQueFalla = 134890L;
        Long cursoIdValido = 200L;
        Inscripcion inscripcionAIntentarGuardar = new Inscripcion(null, LocalDate.now(),  cursoIdValido, alumnoIdQueFalla);


        Request request = Request.create(Request.HttpMethod.GET, "/alumnos/" + alumnoIdQueFalla, new HashMap<>(), null, Charset.defaultCharset(), null);
        FeignException feignException = FeignException.errorStatus("alumno-service", Response.builder()
                .request(request)
                .status(404)
                .headers(Collections.emptyMap())
                .build());
        when(alumnoClientRest.findById(alumnoIdQueFalla)).thenThrow(feignException);

        InscripcionException thrown = assertThrows(InscripcionException.class, () -> {
            inscripcionService.save(inscripcionAIntentarGuardar);
        });

        assertTrue(thrown.getMessage().contains("Existen problemas con la asosiacion Alumno"));

        verify(alumnoClientRest, times(1)).findById(alumnoIdQueFalla);

        verify(cursosClientRest, never()).findById(anyLong());
        verify(inscripcionRepository, never()).save(any(Inscripcion.class));
    }

    @Test
    @DisplayName("No debería guardar una inscripcion_curso")
    void shouldNotSaveInscripcion_CursoFeignException() {
        Long alumnoIdValido = 100L;
        Long cursoIdQueFalla = 20000L;
        Inscripcion inscripcionAIntentarGuardar = new Inscripcion(null, LocalDate.now(), cursoIdQueFalla, alumnoIdValido);

        when(alumnoClientRest.findById(alumnoIdValido)).thenReturn(new Alumnos(alumnoIdValido, "Alumno Válido"));

        Request request = Request.create(Request.HttpMethod.GET, "/cursos/" + cursoIdQueFalla, new HashMap<>(), null, Charset.defaultCharset(), null);
        FeignException feignException = FeignException.errorStatus("curso-service", Response.builder()
                .request(request)
                .status(404)
                .headers(Collections.emptyMap())
                .build());
        when(cursosClientRest.findById(cursoIdQueFalla)).thenThrow(feignException);

        InscripcionException thrown = assertThrows(InscripcionException.class, () -> {
            inscripcionService.save(inscripcionAIntentarGuardar);
        });

        assertTrue(thrown.getMessage().contains("Existen problemas con la asosiacion Curso"));

        verify(alumnoClientRest, times(1)).findById(alumnoIdValido);
        verify(cursosClientRest, times(1)).findById(cursoIdQueFalla);

        verify(inscripcionRepository, never()).save(any(Inscripcion.class));
    }

    @Test
    @DisplayName("Debería borrar una inscripcion")
    void shouldDeleteInscripcion() {
        Long deleteId = 1L;
        when(inscripcionRepository.existsById(deleteId)).thenReturn(true);

        doNothing().when(inscripcionRepository).deleteById(deleteId);

        inscripcionService.delete(deleteId);

        verify(inscripcionRepository, times(1)).existsById(deleteId);

        verify(inscripcionRepository, times(1)).deleteById(deleteId);
    }

    @Test
    @DisplayName("No debería borrar una inscripcion")
    void shouldNotDeleteInscripcion() {
        Long entityId = 113490495L;

        when(inscripcionRepository.existsById(entityId)).thenReturn(false);

        InscripcionException thrown = assertThrows(InscripcionException.class, () -> {
            inscripcionService.delete(entityId);
        });

        assertTrue(thrown.getMessage().contains("Inscripcion con id " + entityId + " no existe"));

        verify(inscripcionRepository, times(1)).existsById(entityId);
        verify(inscripcionRepository, never()).deleteById(entityId);
    }

    @Test
    @DisplayName("Debería updatear una inscripcion")
    void shouldUpdateByIdInscripcion() {
        Long existingId = 1L;
        LocalDate originalDate = LocalDate.of(2023, 1, 15);
        Long originalAlumnoId = 101L;
        Long originalCursoId = 201L;
        Inscripcion existingInscripcion = new Inscripcion(existingId, originalDate, originalAlumnoId, originalCursoId);

        LocalDate updatedDate = LocalDate.of(2024, 2, 20);
        Long updatedAlumnoId = 102L;
        Long updatedCursoId = 202L;
        InscripcionUpdateDTO updateDTO = new InscripcionUpdateDTO(updatedDate, updatedCursoId, updatedAlumnoId);

        Inscripcion updatedInscripcion = new Inscripcion(existingId, updatedDate, updatedCursoId, updatedAlumnoId);

        when(inscripcionRepository.findById(existingId)).thenReturn(Optional.of(existingInscripcion));

        when(inscripcionRepository.save(any(Inscripcion.class))).thenReturn(updatedInscripcion);

        Inscripcion result = inscripcionService.updateById(existingId, updateDTO);


        verify(inscripcionRepository, times(1)).findById(existingId);

        verify(inscripcionRepository, times(1)).save(any(Inscripcion.class));

        assertNotNull(result);
        assertEquals(existingId, result.getIdInscripcion());
        assertEquals(updatedDate, result.getFechaInscripcion());
        assertEquals(updatedAlumnoId, result.getIdAlumno());
        assertEquals(updatedCursoId, result.getIdCurso());
    }

    @Test
    void shouldNotUpdateByIdInscripcion() {
        Long nonExistingId = 9912345L;
        InscripcionUpdateDTO updateDTO = new InscripcionUpdateDTO(LocalDate.now(), 1L, 1L);


        when(inscripcionRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        InscripcionException thrown = assertThrows(InscripcionException.class, () -> {
            inscripcionService.updateById(nonExistingId, updateDTO);
        });

        assertTrue(thrown.getMessage().contains("Inscripcion con id " + nonExistingId + " no encontrada"));

        verify(inscripcionRepository, times(1)).findById(nonExistingId);

        verify(inscripcionRepository, never()).save(any(Inscripcion.class));
    }

}
