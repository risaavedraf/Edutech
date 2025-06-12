package com.edutech.msvc.alumnos.services;

import com.edutech.msvc.alumnos.exception.AlumnoException;
import com.edutech.msvc.alumnos.models.entities.Alumno;
import com.edutech.msvc.alumnos.repositories.AlumnoRepository;
import net.datafaker.Faker;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class AlumnoServiceTest {
    @Mock
    private AlumnoRepository alumnoRepository;

    @InjectMocks
    private AlumnoServicelmpl alumnoServicelmpl;

    private List<Alumno> alumnoList = new ArrayList<>();

    private Alumno alumnoPrueba;

    @BeforeEach
    public void setUp(){
        Faker faker = new Faker(Locale.of("es","CL"));
        String contrasenia = faker.expression("#{templatify 'A#######!', '#', 'a', 'z', 'A', 'Z', '0', '9', '!', '@', '#', '$', '%'}");
        for(int i=0;i<100;i++){
            Alumno alumno = new Alumno();

            String numeroStr = faker.idNumber().valid().replaceAll("-","");
            String ultimo = numeroStr.substring(numeroStr.length()-1);
            String restante = numeroStr.substring(0, numeroStr.length()-1);

            alumno.setNombres(faker.name().firstName());
            alumno.setApellidos(faker.name().lastName());
            alumno.setFechaNacimiento(faker.timeAndDate().birthday());
            alumno.setCorreo(faker.internet().emailAddress());
            alumno.setContraseña(contrasenia);
            alumno.setCuentaActiva(Boolean.TRUE);
        }
        this.alumnoPrueba = this.alumnoList.get(0);
    }

    @Test
    @DisplayName("Devuleve todos los Alumnos")
    public void shouldFindAllAlumno(){
        when(alumnoRepository.findAll()).thenReturn(this.alumnoList);
        List<Alumno> result = alumnoServicelmpl.findAll();
        assertThat(result).hasSize(200);
        assertThat(result).contains(this.alumnoPrueba);

        verify(alumnoRepository, times(1)).findAll();

    }

    @Test
    @DisplayName("Encontrar por id un medico")
    public void shouldFinAlumnoById(){
        when(alumnoRepository.findById(1L)).thenReturn(Optional.of(alumnoPrueba));
        Alumno result = alumnoServicelmpl.findById(1L);
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(this.alumnoPrueba);

        verify(alumnoRepository,times(1)).findById(1L);
    }

    @Test
    @DisplayName("Encontrar por id un medico que no existe")
    public void shouldNotFindAlumnoById(){
        Long idInexistente = 1L;
        when(alumnoRepository.findById(idInexistente)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> {
            alumnoServicelmpl.findById(idInexistente);
        }).isInstanceOf(AlumnoException.class)
                .hasMessageContaining("El medico con id " + idInexistente
                        + " no se encuentra en la base de datos");
        verify(alumnoRepository,times(1)).findById(idInexistente);
    }

    @Test
    @DisplayName("Deberia guardar un medico")
    public void shouldSaveAlumno(){
        when(alumnoRepository.save(any(Alumno.class))).thenReturn(this.alumnoPrueba);
        Alumno result = alumnoServicelmpl.save(this.alumnoPrueba);
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(this.alumnoPrueba);
        verify(alumnoRepository,times(1)).save(any(Alumno.class));
    }
}
