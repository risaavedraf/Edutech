package com.edutech.msvc.cursos.controllers;

import com.edutech.msvc.cursos.dtos.CursoUpdateDTO;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cursos")
@Validated
@Tag(
        name = "Curso API",
        description = "Aquí se generan todos los metodos CRUD para curso"
)
public class CursoController {

    @Autowired
    private CursoService cursoService;

    @GetMapping
    @Operation(
            summary = "Endpoint que obtiene todos los cursos",
            description = "Este endpoint devuelve un List de todos los cursos que se encuentren en la base de datos"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Operacion de esctacción de cursos exitosa"
            )
    })
    public ResponseEntity<List<Curso>> findAll(){
        List<Curso> cursos = this.cursoService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(cursos);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Endpoint que devuelve un curso por id",
            description = "Endpoint que va a devolver un Curso.class al momento de buscarlo po id"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Obtencion por id correcta"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Error cuando el curso con cierto id no existe",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class)
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
    public ResponseEntity<Curso> findById(@PathVariable Long id){
        Curso curso = this.cursoService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(curso);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Elimina un curso por su id",
            description = "Este metodo elimina un Curso de la base de datos, " +
                    "si el id no existe retorna un error 404"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Curso eliminado correctamente"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Error - Curso con ID no existe",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class)
                    )
            )
    })
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        cursoService.delete(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }


    @PutMapping("/{id}")
    @Operation(
            summary = "Actualiza un Curso",
            description = "Este endpoint permite actualizar un Curso existente"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Curso actualizado correctamente"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Curso no encontrado con el id suministrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class)
                    )
            )
    })
    public ResponseEntity<Curso> updateById(@PathVariable Long id, @Valid @RequestBody CursoUpdateDTO cursoUpdateDTO){
        Curso cursoActualizado = cursoService.updateById(id, cursoUpdateDTO);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(cursoActualizado);
    }


    @PostMapping
    @Operation(
            summary = "Endpoint guardado de un curso",
            description = "Endpoint que me permite capturar un elemento Curso.class y lo guarda " +
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
            description = "Estructura de datos que me permite realizar la creación de un Curso",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Curso.class)
            )
    )
    public ResponseEntity<Curso> save(@Valid @RequestBody Curso curso){
        Curso saved = this.cursoService.save(curso);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}
