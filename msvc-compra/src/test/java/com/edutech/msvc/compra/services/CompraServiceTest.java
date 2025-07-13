package com.edutech.msvc.compra.services;

import com.edutech.msvc.compra.dtos.CompraDTO;
import com.edutech.msvc.compra.exceptions.CompraException;
import com.edutech.msvc.compra.model.entity.Compra;
import com.edutech.msvc.compra.repositories.CompraRepository;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CompraServiceTest {

    @Mock
    private CompraRepository compraRepository;

    @InjectMocks
    private CompraServiceImpl compraService;

    private Compra compraPrueba;
    private List<Compra> compras = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        this.compraPrueba = new Compra();
        compraPrueba.setIdCompra(1L);
        compraPrueba.setHoraCompra(LocalDateTime.now());
        compraPrueba.setTotal(15000);
        compraPrueba.setIdCurso(111L);
        compraPrueba.setIdAlumno(222L);

        Faker faker = new Faker(new Locale("es", "CL"));
        for (int i = 0; i < 100; i++) {
            Compra c = new Compra();
            c.setIdCompra((long) i + 2);
            c.setHoraCompra(LocalDateTime.now().minusDays(faker.number().numberBetween(1, 30)));
            c.setTotal(faker.number().numberBetween(5000, 30000));
            c.setIdCurso((long) faker.number().numberBetween(100, 200));
            c.setIdAlumno((long) faker.number().numberBetween(200, 300));
            compras.add(c);
        }
    }

    @Test
    @DisplayName("Devuelve todas las compras como DTO")
    public void shouldFindAllCompras() {
        compras.add(compraPrueba);
        when(compraRepository.findAll()).thenReturn(compras);

        List<CompraDTO> result = compraService.findAll();

        assertThat(result).hasSize(101);
        assertThat(result)
                .extracting(CompraDTO::getCurso)
                .contains(compraPrueba.getIdCompra()); // comparación indirecta

        verify(compraRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Encontrar por ID una compra")
    public void shouldFindCompraById() {
        when(compraRepository.findById(1L)).thenReturn(Optional.of(compraPrueba));
        Compra result = compraService.findById(1L);
        assertThat(result).isNotNull();
        assertThat(result.getIdCompra()).isEqualTo(compraPrueba.getIdCompra());
        verify(compraRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Compra no encontrada por ID")
    public void shouldNotFindCompraById() {
        Long idInexistente = 9999L;
        when(compraRepository.findById(idInexistente)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> compraService.findById(idInexistente))
                .isInstanceOf(CompraException.class)
                .hasMessageContaining("La compra con id: " + idInexistente + " no se encuentra en la base de datos");

        verify(compraRepository, times(1)).findById(idInexistente);
    }

    @Test
    @DisplayName("Debería guardar una compra")
    public void shouldSaveCompra() {
        when(compraRepository.save(any(Compra.class))).thenReturn(compraPrueba);
        Compra result = compraService.save(compraPrueba);
        assertThat(result).isNotNull();
        assertThat(result.getTotal()).isEqualTo(15000);
        verify(compraRepository, times(1)).save(any(Compra.class));
    }
}
