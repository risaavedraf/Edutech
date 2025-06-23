package com.edutech.msvc.inscripcion.assemblers;

import com.edutech.msvc.inscripcion.controllers.InscripcionControllerV2;
import com.edutech.msvc.inscripcion.dtos.InscripcionDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class InscripcionModelAssembler implements RepresentationModelAssembler<InscripcionDTO, EntityModel<InscripcionDTO>>{
    @Override
    public EntityModel<InscripcionDTO> toModel(InscripcionDTO entity){
        return EntityModel.of(
                entity,
                linkTo(methodOn(InscripcionControllerV2.class).findById(entity.getIdInscripcion())).withSelfRel(),
                linkTo(methodOn(InscripcionControllerV2.class).findAll()).withRel("Inscripcion")
        );
    }
}
