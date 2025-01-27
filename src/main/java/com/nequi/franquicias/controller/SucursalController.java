package com.nequi.franquicias.controller;

import com.nequi.franquicias.dto.NombreNuevoDto;
import com.nequi.franquicias.dto.ProductoDTO;
import com.nequi.franquicias.model.Producto;
import com.nequi.franquicias.service.SucursalServicio;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/api/v1/sucursal")
public class SucursalController {

    private final SucursalServicio sucursalServicio;

    public SucursalController(SucursalServicio sucursalServicio) {
        this.sucursalServicio = sucursalServicio;
    }

    @PostMapping("/{sucursalId}/productos")
    public Mono<ResponseEntity<Producto>> agregarProducto(
            @PathVariable Long sucursalId,
            @Valid @RequestBody ProductoDTO productoDTO) {
        log.info("Recibida solicitud para agregar producto a sucursal ID {}: {}", sucursalId, productoDTO);
        return sucursalServicio.agregarProducto(sucursalId, productoDTO)
                .map(producto -> ResponseEntity.status(HttpStatus.CREATED).body(producto))
                .doOnSuccess(p -> log.info("Producto agregado exitosamente"))
                .onErrorResume(ex -> {
                    log.error("Error al agregar producto: {}", ex.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
                });
    }

    @GetMapping("/{franquiciaId}/productos/mayor-stock")
    public Mono<ResponseEntity<Flux<Producto>>> obtenerProductosConMayorStock(
            @PathVariable Long franquiciaId) {
        log.info("Recibida solicitud para obtener productos con mayor stock de franquicia ID: {}", franquiciaId);
        return Mono.just(ResponseEntity.ok(sucursalServicio.obtenerProductosConMayorStock(franquiciaId)))
                .doOnSuccess(r -> log.info("Consulta de productos completada exitosamente"))
                .onErrorResume(ex -> {
                    log.error("Error al consultar productos: {}", ex.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
                });
    }

    @PutMapping("/{sucursalId}/nombre")
    public Mono<ResponseEntity<String>> actualizarNombreSucursal(
            @PathVariable Long sucursalId,
            @Valid @RequestBody NombreNuevoDto dto) {
        return sucursalServicio.actualizarNombreSucursal(sucursalId, dto)
                .map(ResponseEntity::ok)
                .onErrorResume(ex -> {
                    log.error("Error al actualizar nombre de sucursal: {}", ex.getMessage());
                    return Mono.just(ResponseEntity.internalServerError().body(ex.getMessage()));
                });
    }
}
