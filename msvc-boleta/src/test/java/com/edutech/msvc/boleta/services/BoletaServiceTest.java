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
    private List<Boleta> boletas = new ArrayList<>();

    @BeforeEach
    public void setUp(){
        this.boletaPrueba = new Boleta();
        boletaPrueba.setIdBoleta(Long.valueOf(1L));
        boletaPrueba.setHoraBoleta(LocalDateTime.now());
        boletaPrueba.setTotal(Integer.valueOf(12000));
        boletaPrueba.setIdCurso(Long.valueOf(101L));
        boletaPrueba.setIdProfesor(Long.valueOf(202L));
        boletaPrueba.setIdAlumno(Long.valueOf(303L));

        Faker faker = new Faker(new Locale("es", "CL"));
        for(int i = 0; i < 100; i++){
            Boleta b = new Boleta();
            b.setIdBoleta(Long.valueOf((long) i + 2));
            b.setHoraBoleta(LocalDateTime.now().minusDays(faker.number().numberBetween(1, 30)));
            b.setTotal(Integer.valueOf(faker.number().numberBetween(10000, 50000)));
            b.setIdCurso(Long.valueOf((long) faker.number().numberBetween(100, 200)));
            b.setIdProfesor(Long.valueOf((long) faker.number().numberBetween(200, 300)));
            b.setIdAlumno(Long.valueOf((long)faker.number().numberBetween(300, 400)));
            boletas.add(b);
        }
    }


    @Test
    @DisplayName("Devuelve todas las boletas")
    public void shouldFindAllBoletas(){
        boletas.add(boletaPrueba);
        when(boletaRepository.findAll()).thenReturn(boletas);

        List<Boleta> result = boletaRepository.findAll();

        assertThat(result).hasSize(101);
        assertThat(result).contains(boletaPrueba);
        verify(boletaRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Encontrar por ID una boleta")
    public void shouldFindBoletaById(){
        when(boletaRepository.findById(Long.valueOf(1L))).thenReturn(Optional.of(boletaPrueba));
        Boleta result = boletaService.findById(Long.valueOf(1L));
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(boletaPrueba);
        verify(boletaRepository, times(1)).findById(Long.valueOf(1L));
    }

    @Test
    @DisplayName("Boleta no encontrada por ID")
    public void shouldNotFindBoletaById(){
        Long idInexistente = (Long) 9999L;
        when(boletaRepository.findById(idInexistente)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> boletaService.findById(idInexistente))
                .isInstanceOf(BoletaException.class)
                .hasMessageContaining("La boleta con id: " + idInexistente + " no se encuentra en la base de datos");

        verify(boletaRepository, times(1)).findById(idInexistente);
    }

    @Test
    @DisplayName("Debería guardar una boleta")
    public void shouldSaveBoleta(){
        when(boletaRepository.save(any(Boleta.class))).thenReturn(boletaPrueba);
        Boleta result = boletaService.save(boletaPrueba);
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(boletaPrueba);
        verify(boletaRepository, times(1)).save(any(Boleta.class));
    }
}
