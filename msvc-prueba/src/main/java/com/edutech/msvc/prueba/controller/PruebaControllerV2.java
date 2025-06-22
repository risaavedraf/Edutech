package com.edutech.msvc.prueba.controller;

import com.edutech.msvc.prueba.assemblers.PruebaModelAssembler;
import com.edutech.msvc.prueba.dtos.ErrorDTO;
import com.edutech.msvc.prueba.models.entities.Prueba;
import com.edutech.msvc.prueba.services.PruebaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import javax.print.attribute.standard.Media;
import java.awt.*;
import java.util.Collection;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/v2/prueba")
@Validated
@Tag(
        name = "Prueba API HATEOAS",
        description = "Aquí se generar todas las prebas CRUD para prueba"
)
public class PruebaControllerV2 {

    @Autowired
    private PruebaService pruebaService;

    @Autowired
    private PruebaModelAssembler pruebaModelAssembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(
            summary = "Endpoint que obtiene todos los medicos",
            description = "Este endpoint devuelve en un List todos las pruebas que se encuentren " +
                    "en la base de datos"
    )

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Operacion de extración de pruebas exitosa",
                    content = @Content(
                            mediaType = MediaTypes.HAL_JSON_VALUE,
                            schema = @Schema(implementation = Prueba.class)
                    )
            )
    })

    public ResponseEntity<CollectionModel<EntityModel<Prueba>>> findAll(){
        List<EntityModel<Prueba>> entityModels = this.pruebaService.findAll()
                .stream()
                .map(pruebaModelAssembler::toModel)
                .toList();
        CollectionModel<EntityModel<Prueba>> collectionModel = CollectionModel.of(
             entityModels,
             linkTo(methodOn(PruebaControllerV2.class).findAll()).withSelfRel()
        );

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(collectionModel);
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(
            summary = "Endpoint que devuelve una prueba por id",
            description = "Endpoint que va devolver un Prueba.class al momento de buscarlo por id"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Obtención por id correcta",
                    content = @Content(
                            mediaType = MediaTypes.HAL_JSON_VALUE,
                            schema = @Schema(implementation = Prueba.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Error cuando el medico con cierto id no existe",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class)
                            //examples = @ExampleObject(
                            //        name = "ERROR NOT FOUND",
                            //        value = "{\"status\":\"200\", \"error\":\"medico no encontrado\"}"
                            //)
                    )
            )
    })

    @Parameters(value = {
            @Parameter(
                    name = "id",
                    description = "Primary Key - Entidad prueba",
                    required = true
            )
    })
    public ResponseEntity<EntityModel<Prueba>> findById(@PathVariable Long id){
        EntityModel<Prueba> entityModel = this.pruebaModelAssembler.toModel(
                this.pruebaService.findById(id)
        );

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(entityModel);
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(
            summary = "Endpoint guardado por una prueba",
            description = "Endpoint que me permite capturar un elemento Prueba.class y lo guarda " +
                    "dentro de nuestra base de datos"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Creacion correcta",
                    content = @Content(
                            mediaType = MediaTypes.HAL_JSON_VALUE,
                            schema = @Schema(implementation = Prueba.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Algun elemento de un msvc no se encuentra",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class)

                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "El elemento que intentas crear ya existe",
                    content = @Content(
                            mediaType = "applicaction/json",
                            schema = @Schema(implementation = ErrorDTO.class)
                    )
            )
    })

    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Estructura de datos que permite realizar la creación de una Prueba",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Prueba.class)
            )
    )
    public ResponseEntity<EntityModel<Prueba>> create(@Validated @RequestBody Prueba prueba){
        Prueba pruebaNew = this.pruebaService.save(prueba);
        EntityModel<Prueba> entityModel = this.pruebaModelAssembler.toModel(pruebaNew);
        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }


}
