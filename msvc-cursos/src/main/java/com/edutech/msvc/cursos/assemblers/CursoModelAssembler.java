package com.edutech.msvc.cursos.assemblers;

import com.edutech.msvc.cursos.controllers.CursoControllerV2;
import com.edutech.msvc.cursos.models.entities.Curso;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class CursoModelAssembler implements RepresentationModelAssembler<Curso, EntityModel<Curso>> {
    @Override
    public EntityModel<Curso> toModel(Curso entity) {
        return EntityModel.of(
                entity,
                linkTo(methodOn(CursoControllerV2.class).findById(entity.getIdCurso())).withSelfRel(),
                linkTo(methodOn(CursoControllerV2.class).findAll()).withRel("cursos")
        );
    }
}
