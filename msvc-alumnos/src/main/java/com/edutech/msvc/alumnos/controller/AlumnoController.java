package com.edutech.msvc.alumnos.controller;

import com.edutech.msvc.alumnos.dtos.AlumnoDTO;
import com.edutech.msvc.alumnos.dtos.ErrorDTO;
import com.edutech.msvc.alumnos.dtos.EstadoDTO;
import com.edutech.msvc.alumnos.dtos.InscripcionAlumnoDTO;
import com.edutech.msvc.alumnos.models.entities.Alumno;
import com.edutech.msvc.alumnos.services.AlumnoService;

import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("/api/v1/alumno")
@Validated
@Tag(name = "Alumno", description = "Esta sección contiene los CRUD de alumno")
public class AlumnoController {

    @Autowired
    private AlumnoService alumnoService;

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
                            mediaType = "application/json",
                            schema = @Schema(implementation = Alumno.class)
                    )
            )
    })
    public ResponseEntity<List<Alumno>> findAll() {
        List<Alumno> alumnos = this.alumnoService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(alumnos);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Obtiene un alumno por su id",
            description = "Este metodo retorna un alumno por su id, si no existe retorna un error 404"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Alumno encontrado correctamente"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Error - Alumno con ID no existe",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class)
                    )
            )
    })
    public ResponseEntity<Alumno> findById(@PathVariable Long id) {
        Alumno alumno = this.alumnoService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(alumno);
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
                            mediaType = "application/json",
                            schema = @Schema(implementation = AlumnoDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Error de validación - Datos del alumno no válidos",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class)
                    )
            )
    })
    public ResponseEntity<Alumno> save(@Valid @RequestBody Alumno alumno) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(alumnoService.save(alumno));
    }
    @PutMapping("/{id}")
    @Operation(
            summary = "Actualiza el estado de cuenta de un alumno",
            description = "Este endpoint permite actualizar el estado de cuenta de un alumno existente"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estado de cuenta actualizado correctamente"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Alumno no encontrado con el id suministrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class)
                    )
            )
    })
    public ResponseEntity<Alumno> actualizarEstadoCuenta(@PathVariable Long id, @Valid @RequestBody EstadoDTO estadoDTO) {
        Alumno alumnoActualizado = alumnoService.cambiarEstadoCuenta(id, estadoDTO);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(alumnoActualizado);
    }
    @GetMapping("/{id}/inscripciones")
    public ResponseEntity<List<InscripcionAlumnoDTO>> findInscripcionesById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(this.alumnoService.findInscripcionesById(id));
    }

}
