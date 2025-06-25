package com.edutech.msvc.boleta.assemblers;

import com.edutech.msvc.boleta.controllers.BoletaController;
import com.edutech.msvc.boleta.dtos.BoletaDTO;
import com.edutech.msvc.boleta.models.entities.Boleta;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class BoletaModelAssembler implements RepresentationModelAssembler<BoletaDTO, EntityModel<BoletaDTO>> {

    @Override
    public EntityModel<BoletaDTO> toModel(BoletaDTO boletaDTO) {
        return EntityModel.of(
                boletaDTO,
                linkTo(methodOn(BoletaController.class).findBy(boletaDTO.getFechaBoleta())).withSelfRel(),
                linkTo(methodOn(BoletaController.class).findAll()).withRel("boletas"),
                linkTo(methodOn(BoletaController.class).findByAlumnoId(boletaDTO.getAlumno())).withRel("alumno"),
                linkTo(methodOn(BoletaController.class).findByProfesorId(boletaDTO.getProfesor())).withRel("profesor"),
                linkTo(methodOn(BoletaController.class).findByCursoId(boletaDTO.getCurso())).withRel("curso")
        );
    }
}
