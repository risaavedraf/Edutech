package com.edutech.msvc.inscripcion.services;

import com.edutech.msvc.inscripcion.exceptions.InscripcionException;
import com.edutech.msvc.inscripcion.models.entities.Inscripcion;
import com.edutech.msvc.inscripcion.repositories.InscripcionRepository;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InscripcionServiceTest {

    @Mock
    private InscripcionRepository inscripcionRepository;

    @InjectMocks
    private InscripcionServiceImpl inscripcionService;

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


    // Falla el clientRest de alumno dice que es nulo, tiene que ser por que no esta la base de datos cargada
    @Test
    @DisplayName("Debería guardar una inscripcion")
    public void shouldSaveInscripcion(){
        when(inscripcionRepository.save(any(Inscripcion.class))).thenReturn(this.inscripcionPrueba);
        Inscripcion result = inscripcionService.save((this.inscripcionPrueba));
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(this.inscripcionPrueba);
        verify(inscripcionRepository,times(1)).save(any(Inscripcion.class));
    }

}
