package com.edutech.msvc.alumnos.controller;

import com.edutech.msvc.alumnos.assemeblers.AlumnoModelAssembler;
import com.edutech.msvc.alumnos.dtos.ErrorDTO;
import com.edutech.msvc.alumnos.dtos.EstadoDTO;
import com.edutech.msvc.alumnos.models.entities.Alumno;
import com.edutech.msvc.alumnos.services.AlumnoService;
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
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v2/alumno")
@Validated
@Tag(name = "AlumnoV2", description = "Esta sección contiene los CRUD de alumno")
public class AlumnoControllerV2 {

    @Autowired
    private AlumnoService alumnoService;

    @Autowired
    private AlumnoModelAssembler alumnoModelAssembler;

    @GetMapping
        @Operation(
                summary = "Obtiene todos los alumnos",
                description = "Este metodo retorna una lista de todos los alumnos registrados"
        )
        @ApiResponses(value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Se retornaron todos los alumnos correctamente",
                        content = @Content(
                                mediaType = MediaTypes.HAL_JSON_VALUE,
                                schema = @Schema(implementation = Alumno.class)
                        )
                )
        })
    public ResponseEntity<CollectionModel<EntityModel<Alumno>>> findAll() {
        List<EntityModel<Alumno>> entityModels = this.alumnoService.findAll()
                .stream()
                .map(alumnoModelAssembler::toModel)
                .toList();

        CollectionModel<EntityModel<Alumno>> collectionModel = CollectionModel.of(
                entityModels,
                linkTo(methodOn(AlumnoControllerV2.class).findAll()).withSelfRel()

        );

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(collectionModel);
    }

    @GetMapping("/{id}")
        @Operation(
                summary = "Obtiene un alumno por su id",
                description = "Este metodo retorna un alumno cuando es consultado mediante su id"
        )
        @ApiResponses(value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Se retorna el alumno encontrado",
                        content = @Content(
                                mediaType = MediaTypes.HAL_JSON_VALUE,
                                schema = @Schema(implementation = Alumno.class)
                        )
                ),
                @ApiResponse(
                        responseCode = "404",
                        description = "Error - Alumno con ID no existe",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = ErrorDTO.class)
                        )
                )
        })
    public ResponseEntity<EntityModel<Alumno>> findById(@PathVariable Long id) {
        EntityModel<Alumno> entityModel = this.alumnoModelAssembler.toModel(
                this.alumnoService.findById(id)
        );
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(entityModel);
    }

    @DeleteMapping("/{id}")
        @Operation(
                summary = "Elimina un alumno por su id",
                description = "Este metodo elimina un alumno de la base de datos, " +
                        "si el id no existe retorna un error 404"
        )
        @ApiResponses(value = {
                @ApiResponse(responseCode = "204", description = "Alumno eliminado correctamente"),
                @ApiResponse(
                        responseCode = "404",
                        description = "Error - Alumno con ID no existe",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = ErrorDTO.class)
                        )
                )
        })
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        alumnoService.delete(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @PostMapping
        @Operation(
                summary = "Crea un nuevo alumno",
                description = "Este endpoint permite crear un nuevo alumno en el sistema"
        )
        @ApiResponses(value = {
                @ApiResponse(
                        responseCode = "201",
                        description = "Alumno creado correctamente",
                        content = @Content(
                                mediaType = MediaTypes.HAL_JSON_VALUE,
                                schema = @Schema(implementation = Alumno.class)
                        )
                ),
                @ApiResponse(
                        responseCode = "400",
                        description = "Error de validación en los datos del alumno",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = ErrorDTO.class)
                        )
                )
        })
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
                required = true,
                description = "Este debe ser Json con los datos de alumno",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = Alumno.class)
                )
        )
    public ResponseEntity<EntityModel<Alumno>>  create(@Valid @RequestBody Alumno medico) {
        Alumno medicoNew = this.alumnoService.save(medico);
        EntityModel<Alumno> entityModel = this.alumnoModelAssembler.toModel(medicoNew);

        return ResponseEntity
                .created(linkTo(methodOn(AlumnoControllerV2.class).findById(medicoNew.getIdUsuario())).toUri())
                .body(entityModel);
    }
    @PutMapping("/{id}")
        @Operation(
                summary = "Actualiza un alumno",
                description = "Este endpoint permite actualizar los datos de un alumno existente"
        )
        @ApiResponses(value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Alumno actualizado correctamente",
                        content = @Content(
                                mediaType = MediaTypes.HAL_JSON_VALUE,
                                schema = @Schema(implementation = Alumno.class)
                        )
                ),
                @ApiResponse(
                        responseCode = "404",
                        description = "Alumno no encontrado con el id suministrado",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = ErrorDTO.class)
                        )
                )
        })
    public  ResponseEntity<Alumno> estadoCuenta(@PathVariable Long id, @Valid @RequestBody EstadoDTO estadoDTO){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(alumnoService.cambiarEstadoCuenta(id,estadoDTO));
    }

}
