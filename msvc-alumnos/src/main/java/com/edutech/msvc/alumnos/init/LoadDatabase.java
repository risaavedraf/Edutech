package com.edutech.msvc.alumnos.init;

import com.edutech.msvc.alumnos.models.entities.Alumno;
import com.edutech.msvc.alumnos.repositories.AlumnoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import net.datafaker.Faker;
import org.w3c.dom.Text;

import java.util.Locale;

@Component

public class LoadDatabase implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Autowired
    AlumnoRepository alumnoRepository;

    @Override
    public void run(String... args) throws Exception {
        Faker faker = new Faker(Locale.of("es","cl"));

        if(alumnoRepository.count() == 0){
            for(int i=0;i<100;i++){
                Alumno alumno = new Alumno();

                String numeroStr = faker.idNumber().valid().replaceAll("-","");
                String ultimo = numeroStr.substring(numeroStr.length()-1);
                String restante = numeroStr.substring(0, numeroStr.length()-1);

                alumno.setNombres(faker.name().firstName());
                alumno.setApellidos(faker.name().lastName());
                alumno.setFechaNacimiento(faker.timeAndDate().birthday());
                alumno.setCorreo(faker.internet().emailAddress());
                alumno.setContraseña()

                alumno.setRun(restante+"-"+ultimo);

                alumno = alumnoRepository.save(alumno);
                log.info("El alumno creado es {}", alumno);
            }
        }

    }
}
