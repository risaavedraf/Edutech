package com.edutech.msvc.inscripcion.dtos;

import com.edutech.msvc.inscripcion.models.entities.Inscripcion;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter @Setter @ToString
@NoArgsConstructor @AllArgsConstructor
public class InscripcionDTO extends Inscripcion {

    private LocalDate fechaInscripcion;
    private CursoDTO curso;
    private AlumnoDTO alumno;
}
