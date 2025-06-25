package com.edutech.msvc.prueba.init;
import com.edutech.msvc.prueba.models.entities.Prueba;
import com.edutech.msvc.prueba.repositories.PruebaRepository;
import net.datafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Locale;


@Profile("dev")
@Component
public class LoadDataBase implements CommandLineRunner {

    @Autowired
    private PruebaRepository pruebaRepository;

    private static final Logger logger = LoggerFactory.getLogger(LoadDataBase.class);


    @Override
    public void run(String... args) throws Exception{
        Faker faker = new Faker(Locale.of("es", "CL"));

        if(pruebaRepository.count()==0){
            for(int i=0;i<1000;i++){
                Prueba prueba = new Prueba();
                prueba.setIdCurso(Long.valueOf(1L));
                prueba.setIdNota(Long.valueOf(1L));
            }
        }

    }

}
