package com.edutech.msvc.boleta.dtos;

import com.edutech.msvc.boleta.models.entities.Boleta;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BoletaDTO extends Boleta {

    private LocalDateTime fechaBoleta;
    private Integer total;
    private ProfesorDTO profesor;
    private AlumnoDTO alumno;
    private CursoDTO curso;

}
