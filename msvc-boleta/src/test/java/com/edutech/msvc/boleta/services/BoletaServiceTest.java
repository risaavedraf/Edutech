package com.edutech.msvc.boleta.services;

import com.edutech.msvc.boleta.dtos.BoletaDTO;
import com.edutech.msvc.boleta.exceptions.BoletaException;
import com.edutech.msvc.boleta.models.entities.Boleta;
import com.edutech.msvc.boleta.repositories.BoletaRepository;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BoletaServiceTest {

    @Mock
    private BoletaRepository boletaRepository;

    @InjectMocks
    private BoletaServiceImpl boletaService;

    private Boleta boletaPrueba;
    private final List<Boleta> boletas = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        this.boletaPrueba = new Boleta();
        boletaPrueba.setIdBoleta(1L);
        boletaPrueba.setHoraBoleta(LocalDateTime.now());
        boletaPrueba.setTotal(12000);
        boletaPrueba.setIdCurso(101L);
        boletaPrueba.setIdProfesor(202L);
        boletaPrueba.setIdAlumno(303L);

        Faker faker = new Faker(new Locale("es", "CL"));
        for (int i = 0; i < 100; i++) {
            Boleta b = new Boleta();
            b.setIdBoleta((long) i + 2);
            b.setHoraBoleta(LocalDateTime.now().minusDays(faker.number().numberBetween(1, 30)));
            b.setTotal(faker.number().numberBetween(10000, 50000));
            b.setIdCurso((long) faker.number().numberBetween(100, 200));
            b.setIdProfesor((long) faker.number().numberBetween(200, 300));
            b.setIdAlumno((long) faker.number().numberBetween(300, 400));
            boletas.add(b);
        }
    }

    @Test
    @DisplayName("Devuelve todas las boletas")
    public void shouldFindAllBoletas() {
        boletas.add(boletaPrueba);
        when(boletaRepository.findAll()).thenReturn(boletas);

        List<BoletaDTO> result = boletaService.findAll();

        assertThat(result).hasSize(101);
        verify(boletaRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Encontrar por ID una boleta")
    public void shouldFindBoletaById() {
        when(boletaRepository.findById(1L)).thenReturn(Optional.of(boletaPrueba));

        Boleta result = boletaService.findById(1L);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(boletaPrueba);
        verify(boletaRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Boleta no encontrada por ID")
    public void shouldNotFindBoletaById() {
        Long idInexistente = 9999L;
        when(boletaRepository.findById(idInexistente)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> boletaService.findById(idInexistente))
                .isInstanceOf(BoletaException.class)
                .hasMessageContaining("La boleta con id: " + idInexistente + " no se encuentra en la base de datos");

        verify(boletaRepository, times(1)).findById(idInexistente);
    }

    @Test
    @DisplayName("Debería guardar una boleta")
    public void shouldSaveBoleta() {
        when(boletaRepository.save(any(Boleta.class))).thenReturn(boletaPrueba);

        Boleta result = boletaService.save(boletaPrueba);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(boletaPrueba);
        verify(boletaRepository, times(1)).save(any(Boleta.class));
    }

    @Test
    @DisplayName("Buscar boletas por ID de curso")
    public void shouldFindBoletasByCursoId() {
        Long cursoId = 101L;
        List<Boleta> boletasCurso = boletas.stream()
                .filter(b -> b.getIdCurso().equals(cursoId))
                .toList();

        when(boletaRepository.findByIdCurso(cursoId)).thenReturn(boletasCurso);

        List<Boleta> result = boletaService.findByCursoId(cursoId);

        assertThat(result).isNotNull();
        assertThat(result).allMatch(b -> b.getIdCurso().equals(cursoId));
        verify(boletaRepository, times(1)).findByIdCurso(cursoId);
    }

    @Test
    @DisplayName("Buscar boletas por ID de alumno")
    public void shouldFindBoletasByAlumnoId() {
        Long alumnoId = 303L;
        List<Boleta> boletasAlumno = boletas.stream()
                .filter(b -> b.getIdAlumno().equals(alumnoId))
                .toList();

        when(boletaRepository.findByIdAlumno(alumnoId)).thenReturn(boletasAlumno);

        List<Boleta> result = boletaService.findByAlumnoId(alumnoId);

        assertThat(result).isNotNull();
        assertThat(result).allMatch(b -> b.getIdAlumno().equals(alumnoId));
        verify(boletaRepository, times(1)).findByIdAlumno(alumnoId);
    }

    @Test
    @DisplayName("Buscar boletas por ID de profesor")
    public void shouldFindBoletasByProfesorId() {
        Long profesorId = 202L;
        List<Boleta> boletasProfesor = boletas.stream()
                .filter(b -> b.getIdProfesor().equals(profesorId))
                .toList();

        when(boletaRepository.findByIdProfesor(profesorId)).thenReturn(boletasProfesor);

        List<Boleta> result = boletaService.findByProfesorId(profesorId);

        assertThat(result).isNotNull();
        assertThat(result).allMatch(b -> b.getIdProfesor().equals(profesorId));
        verify(boletaRepository, times(1)).findByIdProfesor(profesorId);
    }

    @Test
    @DisplayName("Buscar boletas por rango de fechas")
    public void shouldFindBoletasByFechaEmisionBetween() {
        LocalDateTime desde = LocalDateTime.now().minusDays(20);
        LocalDateTime hasta = LocalDateTime.now();

        List<Boleta> boletasEnRango = boletas.stream()
                .filter(b -> b.getHoraBoleta().isAfter(desde) && b.getHoraBoleta().isBefore(hasta))
                .toList();

        when(boletaRepository.findByHoraBoletaBetween(desde, hasta)).thenReturn(boletasEnRango);

        List<BoletaDTO> result = boletaService.findByFechaEmisionBetween(desde, hasta);

        assertThat(result).isNotEmpty();
        verify(boletaRepository, times(1)).findByHoraBoletaBetween(desde, hasta);
    }

    @Test
    @DisplayName("Buscar boletas con monto mínimo")
    public void shouldFindBoletasWithMinimumAmount() {
        int montoMinimo = 20000;

        List<Boleta> boletasFiltradas = boletas.stream()
                .filter(b -> b.getTotal() >= montoMinimo)
                .toList();

        when(boletaRepository.findByTotalGreaterThanEqual(montoMinimo)).thenReturn(boletasFiltradas);

        List<Boleta> result = boletaService.findByTotalGreaterThanEqual(montoMinimo);

        assertThat(result).isNotEmpty();
        assertThat(result).allMatch(b -> b.getTotal() >= montoMinimo);
        verify(boletaRepository, times(1)).findByTotalGreaterThanEqual(montoMinimo);
    }
}
