package com.edutech.msvc.profesores.controller;

import com.edutech.msvc.profesores.assemeblers.ProfesorModelAssembler;
import com.edutech.msvc.profesores.dtos.ErrorDTO;
import com.edutech.msvc.profesores.dtos.EstadoProfesorDTO;
import com.edutech.msvc.profesores.dtos.UpdateProfesorDTO;
import com.edutech.msvc.profesores.models.entities.Profesor;
import com.edutech.msvc.profesores.services.ProfesorService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
@RestController
@RequestMapping("/api/v2/profesor")
@Validated
public class ProfesorControllerV2 {
    @Autowired
    private ProfesorService profesorService;

    @Autowired
    private ProfesorModelAssembler profesorModelAssembler;

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
                                schema = @Schema(implementation = Profesor.class)
                        )
                )
        })
    public ResponseEntity<CollectionModel<EntityModel<Profesor>>> findAll() {
        List<EntityModel<Profesor>> entityModels = this.profesorService.findAll()
                .stream()
                .map(profesorModelAssembler::toModel)
                .toList();

        CollectionModel<EntityModel<Profesor>> collectionModel = CollectionModel.of(
                entityModels,
                linkTo(methodOn(ProfesorControllerV2.class).findAll()).withSelfRel()

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
                                schema = @Schema(implementation = Profesor.class)
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
    public ResponseEntity<EntityModel<Profesor>> findById(@PathVariable Long id) {
        EntityModel<Profesor> entityModel = this.profesorModelAssembler.toModel(
                this.profesorService.findById(id)
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
        profesorService.delete(id);
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
                                schema = @Schema(implementation = Profesor.class)
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
                        schema = @Schema(implementation = Profesor.class)
                )
        )
    public ResponseEntity<EntityModel<Profesor>>  create(@Valid @RequestBody Profesor medico) {
        Profesor medicoNew = this.profesorService.save(medico);
        EntityModel<Profesor> entityModel = this.profesorModelAssembler.toModel(medicoNew);

        return ResponseEntity
                .created(linkTo(methodOn(ProfesorControllerV2.class).findById(medicoNew.getIdProfesor())).toUri())
                .body(entityModel);
    }
    @PutMapping("/{id}")
        @Operation(
            summary = "Cambia el estado de cuenta de un profesor",
            description = "Este metodo cambia el estado de cuenta de un profesor existente"
    )
@ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Estado de cuenta actualizado correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Profesor.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Error de validación",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Profesor no encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class)
                    )
            )
    })
    public  ResponseEntity<Profesor> estadoCuenta(@PathVariable Long id, @Valid @RequestBody EstadoProfesorDTO estadoDTO){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(profesorService.cambiarEstadoCuenta(id,estadoDTO));
    }
}
