package com.edutech.msvc.compra.dtos;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ProfesorDTO {

    private String runProfesor;
    private LocalDate fechaNacimiento;
    private String nombreCompleto;
    private boolean estadoCuenta;

    public boolean isEstadoCuenta() {
        return estadoCuenta;
    }

    public void setEstadoCuenta(boolean estadoCuenta){
        this.estadoCuenta= estadoCuenta;

    }
}
