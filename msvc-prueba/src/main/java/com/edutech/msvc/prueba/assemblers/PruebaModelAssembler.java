package com.edutech.msvc.prueba.assemblers;

import com.edutech.msvc.prueba.controller.PruebaControllerV2;
import com.edutech.msvc.prueba.models.entities.Prueba;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilderDslKt.withRel;

@Component
public class PruebaModelAssembler implements RepresentationModelAssembler<Prueba, EntityModel<Prueba>> {

    @Override
    public EntityModel<Prueba> toModel(Prueba entity){
        return EntityModel.of(
                entity,
                linkTo(methodOn(PruebaControllerV2.class).findById(entity.getIdPrueba())).withSelfRel(),
                Link.of("http://localhost:8006/profesor/"+entity.getIdPrueba()).withRel("Profesor")
        );

    }
}
