package com.edutech.msvc.compra.assemblers;

import com.edutech.msvc.compra.controllers.CompraController;
import com.edutech.msvc.compra.model.entities.Compra;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class CompraModelAssembler implements RepresentationModelAssembler<Compra, EntityModel<Compra>> {

    @Override
    public EntityModel<Compra> toModel(Compra compra) {
        return EntityModel.of(
                compra,
                linkTo(methodOn(CompraController.class).findById(compra.getIdCompra())).withSelfRel(),
                linkTo(methodOn(CompraController.class).findAll()).withRel("compras"),
                linkTo(methodOn(CompraController.class).findByCursoId(compra.getIdCurso())).withRel("curso"),
                linkTo(methodOn(CompraController.class).findByAlumnoId(compra.getIdAlumno())).withRel("alumno")
        );
    }
}
