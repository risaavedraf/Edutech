package com.edutech.msvc.cursos.init;

import com.edutech.msvc.cursos.models.entities.Curso;
import com.edutech.msvc.cursos.repositories.CursoRepository;
import net.datafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Locale;
import java.util.Random;

@Profile("dev")
@Component
public class LoadDatabase implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Autowired
    CursoRepository cursoRepository;

    @Override
    public void run(String... args) throws Exception {
        Faker faker = new Faker(Locale.of("es","CL"));
        Random rand = new Random();
        int uno = 1;
        int dos = 2;

        if(cursoRepository.count() == 0){
            for(int i=0;i<100;i++){
                Curso curso = new Curso();
                curso.setNombre(faker.educator().course());
                curso.setDuracion(rand.nextLong());
                curso.setFechaCreacion(faker.timeAndDate().birthday(uno, dos));
                curso.setPrecio(rand.nextLong());
                curso.setEstado(Boolean.TRUE);

                curso = cursoRepository.save(curso);
                log.info("El curso creado es {}", curso);
            }
        }

    }
}
