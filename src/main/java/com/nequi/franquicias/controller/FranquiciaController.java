package com.nequi.franquicias.controller;

import com.nequi.franquicias.dto.FranquiciaDTO;
import com.nequi.franquicias.dto.NombreNuevoDto;
import com.nequi.franquicias.dto.SucursalDTO;
import com.nequi.franquicias.model.Franquicia;
import com.nequi.franquicias.model.Sucursal;
import com.nequi.franquicias.service.FranquiciaServicio;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;


@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/franquicia")
public class FranquiciaController {
    private final FranquiciaServicio franquiciaServicio;

    public FranquiciaController(FranquiciaServicio franquiciaServicio) {
        this.franquiciaServicio = franquiciaServicio;
    }

    @PostMapping
    public Mono<ResponseEntity<Franquicia>> crearFranquicia(@Valid @RequestBody FranquiciaDTO franquiciaDTO) {
        log.info("Recibida solicitud para crear franquicia: {}", franquiciaDTO);
        return franquiciaServicio.crearFranquicia(franquiciaDTO)
                .map(franquicia -> ResponseEntity.status(HttpStatus.CREATED).body(franquicia))
                .doOnSuccess(f -> log.info("Franquicia creada exitosamente"))
                .onErrorResume(ex -> {
                    log.error("Error al crear franquicia: {}", ex.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
                });
    }

    @PostMapping("/{franquiciaId}/sucursales")
    public Mono<ResponseEntity<Sucursal>> agregarSucursal(
            @PathVariable Long franquiciaId,
            @Valid @RequestBody SucursalDTO sucursalDTO) {
        log.info("Recibida solicitud para agregar sucursal a franquicia ID {}: {}", franquiciaId, sucursalDTO);
        return franquiciaServicio.agregarSucursal(franquiciaId, sucursalDTO)
                .map(sucursal -> ResponseEntity.status(HttpStatus.CREATED).body(sucursal))
                .doOnSuccess(s -> log.info("Sucursal agregada exitosamente"))
                .onErrorResume(ex -> {
                    log.error("Error al agregar sucursal: {}", ex.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
                });
    }

    @PutMapping("/{franquiciaId}/nombre")
    public Mono<ResponseEntity<String>> actualizarNombreFranquicia(
            @PathVariable Long franquiciaId,
            @Valid @RequestBody NombreNuevoDto dto) {
        return franquiciaServicio.actualizarNombreFranquicia(franquiciaId, dto)
                .map(ResponseEntity::ok)
                .onErrorResume(ex -> {
                    log.error("Error al actualizar nombre de franquicia: {}", ex.getMessage());
                    return Mono.just(ResponseEntity.internalServerError().body(ex.getMessage()));
                });
    }

}
