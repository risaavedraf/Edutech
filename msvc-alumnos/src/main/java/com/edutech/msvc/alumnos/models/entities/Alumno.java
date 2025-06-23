package com.edutech.msvc.alumnos.models.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "usuario")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Alumno {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long idUsuario;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "El campo run no puede ser vacio")
    @Pattern(regexp = "\\d{1,8}-[\\dKk]", message = "El formato del run paciente debe ser XXXXXXXX-X")
    private String run;

    @Column(nullable = false)
    @NotBlank(message = "El campo no puede ser vacio")
    private String nombres;

    @Column(nullable = false)
    @NotBlank(message = "El campo no puede ser vacio")
    private String apellidos;

    @Column(nullable = false)
    @NotNull(message = "El campo no puede ser vacio")
    private LocalDate fechaNacimiento;

    private String correo;

    @Column(nullable = false)
    @NotNull(message = "El campo no puede ser vacio")
    private String contraseña;

    private Boolean cuentaActiva;

    public Alumno(String run , String nombres, String apellidos, LocalDate fechaNacimiento, String correo, String contraseña, Boolean cuentaActiva) {
        this.run = run;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.fechaNacimiento = fechaNacimiento;
        this.correo = correo;
        this.contraseña = contraseña;
        this.cuentaActiva = cuentaActiva;
    }
}

