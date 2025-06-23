package com.edutech.msvc.profesores.init;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.edutech.msvc.profesores.models.entities.Profesor;
import com.edutech.msvc.profesores.repositories.ProfesorRepository;

import net.datafaker.Faker;

@Profile("dev")
@Component
public class LoadDatabase implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Autowired
    ProfesorRepository profesorRepository;

    @Override
    public void run(String... args) throws Exception {
        Faker faker = new Faker(Locale.of("es","CL"));
        String contrasenia = faker.expression("#{templatify 'A#######!', '#', 'a', 'z', 'A', 'Z', '0', '9', '!', '@', '#', '$', '%'}");

        if(profesorRepository.count() == 0){
            for(int i=0;i<100;i++){
                Profesor profesor = new Profesor();

                String numeroStr = faker.idNumber().valid().replaceAll("-","");
                String ultimo = numeroStr.substring(numeroStr.length()-1);
                String restante = numeroStr.substring(0, numeroStr.length()-1);

                profesor.setNombres(faker.name().firstName());
                profesor.setApellidos(faker.name().lastName());
                profesor.setFechaNacimiento(faker.timeAndDate().birthday());
                profesor.setCorreo(faker.internet().emailAddress());
                profesor.setContraseña(contrasenia);
                profesor.setCuentaActiva(Boolean.TRUE);

                profesor.setRun(restante+"-"+ultimo);
                log.info("El rut que agregas es {}", profesor.getRun());
                profesor = profesorRepository.save(profesor);
                log.info("EL profesor creado es: {}", profesor);
            }
        }
    }
    
}
