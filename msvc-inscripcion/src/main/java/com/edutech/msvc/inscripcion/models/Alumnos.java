package com.edutech.msvc.inscripcion.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
@Getter @Setter @ToString
@NoArgsConstructor @AllArgsConstructor
public class Alumnos {

    private Long idUsuario;
    private String run;
    private String nombres;
    private String apellidos;
    private LocalDate fechaNacimiento;
    private String correo;
    private String contraseña;
    private Boolean cuentaActiva;

    public Alumnos(Long idUsuario, String nombre){
        this.idUsuario = idUsuario;
        this.nombres = nombre;
    }
}
