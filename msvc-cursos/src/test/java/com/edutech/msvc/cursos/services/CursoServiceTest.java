package com.edutech.msvc.cursos.services;

import com.edutech.msvc.cursos.models.entities.Curso;
import com.edutech.msvc.cursos.repositories.CursoRepository;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CursoServiceTest {

    @Mock
    private CursoRepository cursoRepository;

    @InjectMocks
    private CursoServiceImpl cursoService;

    private Curso cursoPrueba;

    private List<Curso> cursos = new ArrayList<>();

    @BeforeEach
    public void setUp(){
        this.cursoPrueba = new Curso(
                "Curso_python", 123L, LocalDate.parse("2025-06-11"), 123L, Boolean.TRUE
        );
        Faker faker = new Faker(Locale.of("es","CL"));
        Random rand = new Random();
        int uno = 1;
        int dos = 2;
        for(int i=0;i<100;i++){
            Curso curso = new Curso();
            curso.setNombre(faker.educator().course());
            curso.setDuracion(rand.nextLong());
            curso.setFechaCreacion(faker.timeAndDate().birthday(uno, dos));
            curso.setPrecio(rand.nextLong());
            curso.setEstado(Boolean.TRUE);

            cursos.add(curso);
        }
    }

    @Test
    @DisplayName("Debo listar todos los cursos")
    public void shouldFindAllCursos(){
        List<Curso> cursos = this.cursos;
        cursos.add(cursoPrueba);
        when(cursoRepository.findAll()).thenReturn(cursos);

        List<Curso> result = cursoService.findAll();

        assertThat(result).hasSize(101);
        assertThat(result).contains(cursoPrueba);

        verify(cursoRepository, times(1)).findAll();

    }
}
