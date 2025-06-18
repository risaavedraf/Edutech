package com.edutech.msvc.alumnos.dtos;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AlumnoDTO {
    @JsonIgnore
    private Long idUsuario;
    private String run;
    private String nombres;
    private String apellidos;
    private LocalDate fechaNacimiento;
    private String correo;
    private String contraseña;
    private Boolean cuentaActiva;
}
