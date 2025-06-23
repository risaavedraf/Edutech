package com.edutech.msvc.profesores.services;

import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.edutech.msvc.profesores.exception.ProfesorException;
import com.edutech.msvc.profesores.models.entities.Profesor;
import com.edutech.msvc.profesores.repositories.ProfesorRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProfesorServiceTest {
    @Mock
    private ProfesorRepository profesorRepository;

    @InjectMocks
    private ProfesorServicelmpl profesorServicelmpl;

    private List<Profesor> profesorList = new ArrayList<>();

    private Profesor profesorPrueba;

    @BeforeEach
    public void setUp(){
        Faker faker = new Faker(Locale.of("es","CL"));
        String contrasenia = faker.expression("#{templatify 'A#######!', '#', 'a', 'z', 'A', 'Z', '0', '9', '!', '@', '#', '$', '%'}");
        for(int i=0;i<100;i++){
            Profesor profesor = new Profesor();
            profesor.setIdProfesor((long) (i + 1));
            String numeroStr = faker.idNumber().valid().replaceAll("-","");
            String ultimo = numeroStr.substring(numeroStr.length()-1);
            String restante = numeroStr.substring(0, numeroStr.length()-1);
            profesor.setRun(restante+"-"+ultimo);
            profesor.setNombres(faker.name().firstName());
            profesor.setApellidos(faker.name().lastName());
            profesor.setFechaNacimiento(faker.timeAndDate().birthday());
            profesor.setCorreo(faker.internet().emailAddress());
            profesor.setContraseña(contrasenia);
            profesor.setCuentaActiva(Boolean.TRUE);

            this.profesorList.add(profesor);
        }
        this.profesorPrueba = new Profesor(1l,"12345678-K", "Juan", "Pérez",
                                        LocalDate.of(2000, 1, 1), "asdasdasd@gmail.com", "contrasenia123", Boolean.TRUE);
    }

    @Test
    @DisplayName("Debe listar todos los profesors")
    public void shouldFindAllMedicos(){

        this.profesorList.add(this.profesorPrueba);

        when(profesorRepository.findAll()).thenReturn(this.profesorList);

        List<Profesor> result = profesorServicelmpl.findAll();

        assertThat(result).hasSize(101);
        assertThat(result).contains(this.profesorPrueba);

        verify(profesorRepository, times(1)).findAll();

    }

    @Test
    @DisplayName("Encontrar por id un profesor")
    public void shouldFinprofesorById(){
        when(profesorRepository.findById(1L)).thenReturn(Optional.of(profesorPrueba));
        Profesor result = profesorServicelmpl.findById(1L);
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(this.profesorPrueba);

        verify(profesorRepository,times(1)).findById(1L);
    }

    @Test
    @DisplayName("Encontrar por id un profesor que no existe")
    public void shouldNotFindprofesorById(){
        Long idInexistente = 1L;
        when(profesorRepository.findById(idInexistente)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> {
            profesorServicelmpl.findById(idInexistente);
        }).isInstanceOf(ProfesorException.class)
                .hasMessageContaining("El profesor con Id " + idInexistente
                        + " no se encuentra el la base de datos");
        verify(profesorRepository,times(1)).findById(idInexistente);
    }

    @Test
    @DisplayName("Deberia guardar un profesor")
    public void shouldSaveprofesor(){
        when(profesorRepository.save(any(Profesor.class))).thenReturn(this.profesorPrueba);
        Profesor result = profesorServicelmpl.save(this.profesorPrueba);
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(this.profesorPrueba);
        verify(profesorRepository,times(1)).save(any(Profesor.class));
    }
    
}
