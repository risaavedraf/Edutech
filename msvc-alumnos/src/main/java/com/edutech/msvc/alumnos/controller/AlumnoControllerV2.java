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
public class AlumnoControllerV2 {

    @Autowired
    private AlumnoService alumnoService;

    @Autowired
    private AlumnoModelAssembler alumnoModelAssembler;

    @GetMapping
    @Operation(summary = "Obtiene todos los medicos", description = "Devuele un List de Medicos en el Body")
    @ApiResponses( value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Operacion existosa",
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
    @Operation(summary = "Obtiene un medico", description = "A través del id suministrado devuelve el medico con esa id")
    @ApiResponses( value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Operacion existosa",
                    content = @Content(
                            mediaType = MediaTypes.HAL_JSON_VALUE,
                            schema = @Schema(implementation = Alumno.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Medico no encontrado, con el id suministrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema =  @Schema(implementation = ErrorDTO.class)
                    )
            )
    })
    @Parameters(value = {
            @Parameter(name="id", description = "Este es el id unico del medico", required = true)
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
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        alumnoService.delete(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @PostMapping
    @Operation(
            summary = "Guarda un medico",
            description = "Con este método podemos enviar los datos mediante un body y realizar el guardado"
    )
    @ApiResponses( value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Guardado exitoso",
                    content = @Content(
                            mediaType = MediaTypes.HAL_JSON_VALUE,
                            schema = @Schema(implementation = Alumno.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "El medico guardado ya se encuentra en la base de datos",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class)
                    )
            )
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "medico a crear",
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
    public  ResponseEntity<Alumno> estadoCuenta(@PathVariable Long id, @Valid @RequestBody EstadoDTO estadoDTO){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(alumnoService.cambiarEstadoCuenta(id,estadoDTO));
    }

}
