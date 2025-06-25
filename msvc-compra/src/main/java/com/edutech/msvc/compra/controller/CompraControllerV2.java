package com.edutech.msvc.compra.controller;

import com.edutech.msvc.compra.assemblers.CompraModelAssembler;
import com.edutech.msvc.compra.dtos.ErrorDTO;
import com.edutech.msvc.compra.model.entity.Compra;
import com.edutech.msvc.compra.services.CompraService;
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
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Slf4j
@RestController
@RequestMapping("/api/v2/compras")
@Validated
@Tag(
        name = "Compra API HATEOAS",
        description = "CRUD para la entidad Compra"
)
public class CompraControllerV2 {

    @Autowired
    private CompraService compraService;

    @Autowired
    private CompraModelAssembler compraModelAssembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(
            summary = "Listar todas las compras",
            description = "Este endpoint retorna todas las compras registradas"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Listado completo de compras",
                    content = @Content(mediaType = MediaTypes.HAL_JSON_VALUE, schema = @Schema(implementation = Compra.class))
            )
    })
    public ResponseEntity<CollectionModel<EntityModel<Compra>>> findAll() {
        List<EntityModel<Compra>> entityModels = this.compraService.findAll()
                .stream()
                .map(compraModelAssembler::toModel)
                .toList();

        CollectionModel<EntityModel<Compra>> collectionModel = CollectionModel.of(
                entityModels,
                linkTo(methodOn(CompraControllerV2.class).findAll()).withSelfRel()
        );
        return ResponseEntity.status(HttpStatus.OK).body(collectionModel);
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(
            summary = "Obtener una compra por ID",
            description = "Retorna una compra específica a partir de su ID"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Compra encontrada",
                    content = @Content(mediaType = MediaTypes.HAL_JSON_VALUE, schema = @Schema(implementation = Compra.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Compra no encontrada",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))
            )
    })
    @Parameters({
            @Parameter(name = "id", description = "ID de la compra", required = true)
    })
    public ResponseEntity<EntityModel<Compra>> findById(@PathVariable Long id) {
        EntityModel<Compra> entityModel = this.compraModelAssembler.toModel(
                this.compraService.findById(id)
        );
        return ResponseEntity.status(HttpStatus.OK).body(entityModel);
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(
            summary = "Registrar una nueva compra",
            description = "Guarda una nueva compra en la base de datos"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Compra creada exitosamente",
                    content = @Content(mediaType = MediaTypes.HAL_JSON_VALUE, schema = @Schema(implementation = Compra.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Error en recursos relacionados",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "La compra ya existe",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))
            )
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos de la compra a registrar",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Compra.class))
    )
    public ResponseEntity<EntityModel<Compra>> create(@Valid @RequestBody Compra compra) {
        Compra nuevaCompra = this.compraService.save(compra);
        EntityModel<Compra> entityModel = this.compraModelAssembler.toModel(nuevaCompra);
        return ResponseEntity
                .created(linkTo(methodOn(CompraControllerV2.class).findById(nuevaCompra.getIdCompra())).toUri())
                .body(entityModel);
    }
}
