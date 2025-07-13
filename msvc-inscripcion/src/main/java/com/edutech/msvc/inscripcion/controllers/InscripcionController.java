package com.edutech.msvc.inscripcion.controllers;

import com.edutech.msvc.inscripcion.dtos.ErrorDTO;
import com.edutech.msvc.inscripcion.dtos.InscripcionDTO;
import com.edutech.msvc.inscripcion.dtos.InscripcionUpdateDTO;
import com.edutech.msvc.inscripcion.models.entities.Inscripcion;
import com.edutech.msvc.inscripcion.services.InscripcionService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inscripciones")
@Validated
@Tag(
        name = "Inscripciones API",
        description = "Aquí se generan todos los metodos CRUD para Inscripción"
)
public class InscripcionController {

    @Autowired
    private InscripcionService inscripcionService;

    @GetMapping
    @Operation(
            summary = "Endpoint que obtiene todas las inscripciones",
            description = "Este endpoint devuelve un List de todas las inscripciones que se encuentren en la base de datos"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Operacion de extacción de Inscripciones exitosa"
            )
    })
    public ResponseEntity<List<InscripcionDTO>> findAll() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.inscripcionService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Endpoint que devuelve una inscripcion por id",
            description = "Endpoint que va a devolver un Inscripcion.class al momento de buscarlo po id"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Obtencion por id correcta"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Error cuando la inscripcion con cierto id no existe",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class)
                    )
            )
    })
    @Parameters(value = {
            @Parameter(
                    name = "id",
                    description = "Primary Key - Entidad inscripcion",
                    required = true
            )
    })
    public ResponseEntity<Inscripcion> findById(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.inscripcionService.findById(id));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Elimina una inscripcion por su id",
            description = "Este metodo elimina un inscripcion de la base de datos, " +
                    "si el id no existe retorna un error 404"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "inscripcion eliminada correctamente"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Error - inscripcion con ID no existe",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class)
                    )
            )
    })
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        inscripcionService.delete(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Actualiza una inscripcion",
            description = "Este endpoint permite actualizar una inscripcion existente"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inscripcion actualizado correctamente"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Inscripcion no encontrado con el id suministrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class)
                    )
            )
    })
    public ResponseEntity<Inscripcion> updateById(@PathVariable Long id, @Valid @RequestBody InscripcionUpdateDTO inscripcionUpdateDTO){
        Inscripcion inscripcionActualizado = inscripcionService.updateById(id, inscripcionUpdateDTO);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(inscripcionActualizado);
    }

    @PostMapping
    @Operation(
            summary = "Endpoint guardado de una inscripcion",
            description = "Endpoint que me permite capturar un elemento Inscripcion.class y lo guarda " +
                    "dentro de nuestra base de datos"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Creacion exitosa"),
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
            description = "Estructura de datos que me permite realizar la creación de una Inscripcion",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Inscripcion.class)
            )
    )
    public ResponseEntity<Inscripcion> save(@RequestBody @Valid Inscripcion inscripcion) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(this.inscripcionService.save(inscripcion));
    }

    // Estos metodos permiten mostrar las Inscripciones filtradas para un Alumno

    @GetMapping("/alumno/{id}")
    public ResponseEntity<List<Inscripcion>> findByIdAlumno(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.inscripcionService.findByAlumnoId(id));
    }

    // Estos metodos permiten mostrar las inscripciones filtradas para un curso

    @GetMapping("/cursos/{id}")
    public ResponseEntity<List<Inscripcion>> findByIdCurso(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.inscripcionService.findByCursoId(id));
    }
}
