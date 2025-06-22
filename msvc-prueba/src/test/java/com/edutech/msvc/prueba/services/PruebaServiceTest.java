package com.edutech.msvc.prueba.services;

import com.edutech.msvc.prueba.clients.ProfesorClientRest;
import com.edutech.msvc.prueba.dtos.PruebaDTO;
import com.edutech.msvc.prueba.exceptions.PruebaException;
import com.edutech.msvc.prueba.models.Profesores;
import com.edutech.msvc.prueba.models.entities.Prueba;
import com.edutech.msvc.prueba.repositories.PruebaRepository;
import net.datafaker.Faker;
import org.aspectj.lang.annotation.Before;
import org.assertj.core.api.ListAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class})
public class PruebaServiceTest {
    @Mock
    private PruebaRepository pruebaRepository;

    @Mock
    private ProfesorClientRest profesorClientRest;

    @InjectMocks
    private PruebaServiceImpl pruebaService;

    private List<Prueba> pruebaList = new ArrayList<>();

    private Prueba profesorPrueba;

    @BeforeEach
    public void setUp() {
        Faker faker = new Faker(Locale.of("es", "CL"));
        for (int i = 0; i < 100; i++) {
            Prueba prueba = new Prueba();
            prueba.setIdPrueba((long) i+1);
            prueba.setIdProfesor((long) i+1);
            prueba.setIdCurso((long) i+1);
            prueba.setIdNota((long) i+1);

            String numeroString = faker.idNumber().valid().replaceAll("-","");
            String ultimo = numeroString.substring(numeroString.length()-1);
            String restante = numeroString.substring(0,numeroString.length()-1);

        }
    }

    @Test
    @DisplayName("Devuelve todos las pruebas")
    public void shouldfindAllPruebas() {
        when(pruebaRepository.findAll()).thenReturn(this.pruebaList);
        List<PruebaDTO> result = pruebaService.findAll();
        assertThat(result).hasSize(200);

        verify(pruebaRepository, times(1)).findAll();

    }

    @Test
    @DisplayName("Encontrar por id una prueba que no exista")
    public void shouldFindPruebaById() {
        Long idInexistente = 1L;
        when(pruebaRepository.findById(idInexistente)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->{
            pruebaService.findById(idInexistente);
        }).isInstanceOf(PruebaException.class)
                .hasMessageContaining("La prueba con id " + idInexistente + " no se encuentra en la base de datos");
        verify(pruebaRepository, times(1)).findById(idInexistente);
    }

    @Test
    @DisplayName("Deberia guardar una prueba")
    public void shouldSavePrueba(){
        when(pruebaRepository.save(any(Prueba.class))).thenReturn(this.profesorPrueba);
        Prueba result = pruebaService.save(this.profesorPrueba);
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(this.profesorPrueba);
        verify(pruebaRepository, times(1)).save(any(Prueba.class));
    }
}
