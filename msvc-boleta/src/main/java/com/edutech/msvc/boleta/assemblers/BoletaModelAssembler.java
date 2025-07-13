package com.edutech.msvc.boleta.assemblers;

import com.edutech.msvc.boleta.controllers.BoletaController;
import com.edutech.msvc.boleta.controllers.BoletaControllerV2;
import com.edutech.msvc.boleta.dtos.BoletaDTO;
import com.edutech.msvc.boleta.models.entities.Boleta;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class BoletaModelAssembler implements RepresentationModelAssembler<BoletaDTO, EntityModel<BoletaDTO>> {

    @Override
    public EntityModel<BoletaDTO> toModel(BoletaDTO entity) {
        return EntityModel.of(
                entity,
                    linkTo(methodOn(BoletaControllerV2.class).findById(entity.getIdBoleta())).withSelfRel(),
                    linkTo(methodOn(BoletaControllerV2.class).findAll()).withRel("Boleta")

        );
    }
}
