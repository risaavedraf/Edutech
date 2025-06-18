package com.edutech.msvc.cursos.controllers;

import com.edutech.msvc.cursos.assemblers.CursoModelAssembler;
import com.edutech.msvc.cursos.dtos.ErrorDTO;
import com.edutech.msvc.cursos.models.entities.Curso;
import com.edutech.msvc.cursos.services.CursoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Slf4j
@RestController
@RequestMapping("/api/v2/cursos")
@Validated
@Tag(
        name = "Curso API HATEOAS",
        description = "Aquí se generan todos los metódos CRUD para curso"
)
public class CursoControllerV2 {

    @Autowired
    private CursoService cursoService;

    @Autowired
    private CursoModelAssembler cursoModelAssembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(
            summary = "Endpoint que obtiene todos los medicos",
            description = "Este endpoint devuelve en un List todos lo médicos que se encuentren en la vase de datos"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Operacion de extracción de cursos exiosa",
                    content = @Content(
                            mediaType = MediaTypes.HAL_JSON_VALUE,
                            schema = @Schema(implementation = Curso.class)
                    )
            )
    })
    public ResponseEntity<CollectionModel<EntityModel<Curso>>> findAll(){
        List<EntityModel<Curso>> entityModels = this.cursoService.findAll()
                .stream()
                .map(cursoModelAssembler::toModel)
                .toList();

        CollectionModel<EntityModel<Curso>> collectionModel = CollectionModel.of(
                entityModels,
                linkTo(methodOn(CursoControllerV2.class).findAll()).withSelfRel()
        );
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(collectionModel);
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(
            summary = "Endpoint que devuelve un curso por id",
            description = "Endpoint que va devolver un Curso.class al momento de buscarlo por id"
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
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class)
                            //examples = @ExampleObject(
                            //        name = "ERROR NOT FOUND",
                            //        value = "{\"status\":\"200\", \"error\":\"curso no encontrado\"}"
                            //)
                    )
            )
    })
    @Parameters(value = {
            @Parameter(
                    name = "id",
                    description = "Primary Key - Entidad curso",
                    required = true
            )

    })
    public ResponseEntity<EntityModel<Curso>> findById(@PathVariable Long id) {
        EntityModel<Curso> entityModel = this.cursoModelAssembler.toModel(
                this.cursoService.findById(id)
        );

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(entityModel);
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(
            summary = "Endpoint guardado de un Curso",
            description = "Endpoint que me permite capturar un elemento Curso.class y lo guarda " +
                    "dentro de nuestra base de datos"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Creacion exitosa",
                    content = @Content(
                            mediaType = MediaTypes.HAL_JSON_VALUE,
                            schema = @Schema(implementation = Curso.class)
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
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class)
                    )
            )
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Estructura de datos que me permite realizar la creación de un Curso",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Curso.class)
            )
    )
    public ResponseEntity<EntityModel<Curso>>  create(@Valid @RequestBody Curso curso) {
        Curso cursoNew = this.cursoService.save(curso);
        EntityModel<Curso> entityModel = this.cursoModelAssembler.toModel(cursoNew);
        return ResponseEntity
                .created(linkTo(methodOn(CursoControllerV2.class).findById(cursoNew.getIdCurso())).toUri())
                .body(entityModel);
    }
}
