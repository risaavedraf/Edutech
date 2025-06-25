package com.edutech.msvc.profesores.assemeblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.edutech.msvc.profesores.controller.ProfesorControllerV2;
import com.edutech.msvc.profesores.models.entities.Profesor;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
@Component
public class ProfesorModelAssembler implements RepresentationModelAssembler<Profesor, EntityModel<Profesor>> {
    @Override
    public EntityModel<Profesor> toModel(Profesor entity) {
        return EntityModel.of(
                entity,
                linkTo(methodOn(ProfesorControllerV2.class).findById(entity.getIdProfesor())).withSelfRel(),
                linkTo(methodOn(ProfesorControllerV2.class).findAll()).withRel("profesor")
        );
    }
}
