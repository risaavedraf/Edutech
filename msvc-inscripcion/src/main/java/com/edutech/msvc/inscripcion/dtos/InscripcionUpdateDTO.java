package com.edutech.msvc.inscripcion.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter @Setter @ToString
@NoArgsConstructor @AllArgsConstructor
public class InscripcionUpdateDTO {

    private LocalDate fechaInscripcion;
    private Long idCurso;
    private Long idAlumno;
}