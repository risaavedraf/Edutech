package com.edutech.msvc.cursos.controllers;


import com.edutech.msvc.cursos.assemblers.CursoModelAssembler;
import com.edutech.msvc.cursos.models.entities.Curso;
import com.edutech.msvc.cursos.services.CursoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/v2/cursos")
@Validated
@Tag(
        name = "Curso API HATEOAS",
        description = "Aqui se generar todos los métodos CRUD para cursos"

)
public class CursoControllerV2 {

    @Autowired
    private CursoService cursoService;

    @Autowired
    private CursoModelAssembler cursoModelAssembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(
            summary = "Endpoint que obtiene todos los cursos",
            description = "Este endpoint devuelve en un List todos los cursos que se encuentren " +
                    "en la base de datos"
    )

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Operacion de extracioón de cursos exitosa",
                    content = @Content(
                            mediaType = MediaTypes.HAL_JSON_VALUE,
                            schema = @Schema(implementation = Curso.class)
                    )
            )
    })

    public ResponseEntity<CollectionModel<EntityModel<Curso>>> findAll(Long idCurso) {
        List<EntityModel<Curso>> entityModels = this.cursoService.findAll()
                .stream()
                .map(cursoModelAssembler::toModel)
                .toList();

        CollectionModel<EntityModel<Curso>> collectionModel = CollectionModel.of(
                entityModels,
                linkto(methodOn(CursoControllerV2.class).findAll(medicoNew.getIdCurso()).withSelfRel())
        );
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(collectionModel);
    }

    @GetMapping(value = "{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(
            summary = "Endpoint que devuelve un curso por id",
            description = "Endpoint que va devolver un Curso.class al momento de buscarla por id"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Obtención por id correcta",
                    content = @Content(
                            mediaType = MediaTypes.HAL_JSON_VALUE,
                            schema = @Schema(implementation = Curso.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Error cuando el curso con cierto id no existe",
                    content = @Content(
                            mediaType = "aplication/json",
                            schema = @Schema(implementation = com.edutech.msvc.cursos.dtos.ErrorDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "El elemento que intentas crear ya existe",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = com.edutech.msvc.cursos.dtos.ErrorDTO.class)
                    )
            )
    })

    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Estructura de datos que me permite realizar la creaciónde un curso",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Curso.class)
            )
    )
    public ResponseEntity<EntityModel<Curso>>  create(@Valid @RequestBody Curso medico) {
        Curso medicoNew = this.cursoService.save(medico);
        EntityModel<Curso> entityModel = this.cursoModelAssembler.toModel(medicoNew);
        return ResponseEntity
                .created(linkTo(methodOn(CursoControllerV2.class).findAll(medicoNew.getIdCurso())).toUri())
                .body(entityModel);
    }



}
