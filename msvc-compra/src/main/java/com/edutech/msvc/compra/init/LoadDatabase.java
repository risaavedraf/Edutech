package com.edutech.msvc.compra.init;

import com.edutech.msvc.compra.model.entity.Compra;
import com.edutech.msvc.compra.repositories.CompraRepository;
import net.datafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Locale;

@Profile("dev")
@Component
public class LoadDatabase implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Autowired
    private CompraRepository compraRepository;

    @Override
    public void run(String... args) throws Exception {
        Faker faker = new Faker(new Locale("es", "CL"));

        if (compraRepository.count() == 0) {
            for (int i = 0; i < 100; i++) {
                Compra compra = new Compra();
                compra.setFechaCompra(LocalDateTime.now().minusDays(faker.number().numberBetween(1, 30)));
                compra.setTotal(faker.number().numberBetween(10000, 50000));
                compra.setIdProducto((long) faker.number().numberBetween(100, 200));
                compra.setIdUsuario((long) faker.number().numberBetween(200, 300));
                compra.setEstado(true); // Asumiendo que tiene un campo estado tipo boolean

                compra = compraRepository.save(compra);
                log.info("La compra creada es {}", compra);
            }
        }
    }
}
