package com.edutech.msvc.profesores.controller;

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
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/v1/profesor")
@Validated
@Tag(name = "Profesor", description = "Esta sección contiene los CRUD de profesor")
public class ProfesorController {
    @Autowired
    private ProfesorService profesorService;

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
                            schema = @Schema(implementation = Profesor.class)
                    )
            )
    })
    public ResponseEntity<List<Profesor>> findAll() {
        List<Profesor> profesor = this.profesorService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(profesor);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Obtiene un profesor por su id",
            description = "Este metodo retorna un profesor por su id, si no existe retorna un error 404"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profesor encontrado correctamente"),
            @ApiResponse(responseCode = "404", description = "Profesor no encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class)
                    )
            )
    })
    public ResponseEntity<Profesor> findById(@PathVariable Long id) {
        Profesor profesor = this.profesorService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(profesor);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Elimina un profesor por su id",
            description = "Este metodo elimina un profesor por su id, si no existe retorna un error 404"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Profesor eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Profesor no encontrado",
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
            summary = "Crea un nuevo profesor",
            description = "Este metodo crea un nuevo profesor en la base de datos"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Profesor creado correctamente",
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
            )
    })
    public ResponseEntity<Profesor> save(@Valid @RequestBody Profesor profesor) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(profesorService.save(profesor));
    }
    @PutMapping("/{id}")
@Operation(
            summary = "Actualiza un profesor por su id",
            description = "Este metodo actualiza un profesor existente en la base de datos"
    )
@ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Profesor actualizado correctamente",
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
    public  ResponseEntity<Profesor> update(@PathVariable Long id, @Valid @RequestBody UpdateProfesorDTO updateProfesorDTO){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(profesorService.updateById(id,updateProfesorDTO));
    }
    @PutMapping("/{id}/")
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
