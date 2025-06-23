package com.edutech.msvc.inscripcion.services;

import com.edutech.msvc.inscripcion.dtos.InscripcionDTO;
import com.edutech.msvc.inscripcion.dtos.InscripcionUpdateDTO;
import com.edutech.msvc.inscripcion.models.Alumnos;
import com.edutech.msvc.inscripcion.models.Cursos;
import com.edutech.msvc.inscripcion.models.entities.Inscripcion;

import java.util.List;

public interface InscripcionService {

    List<InscripcionDTO> findAll();
    Inscripcion findById(Long id);
    Inscripcion save(Inscripcion inscripcion);
    void delete(Long id);
    Inscripcion updateById(Long id, InscripcionUpdateDTO inscripcionUpdateDTO);
    List<Inscripcion> findByAlumnoId(Long alumnoId);
    List<Inscripcion> findByCursoId(Long cursoId);

    InscripcionDTO fillDTO(Alumnos alumno, Cursos curso, Inscripcion inscripcion);
    InscripcionDTO findByIdToDTO(Long id);
}
