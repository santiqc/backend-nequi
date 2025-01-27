package com.nequi.franquicias.controller;

import com.nequi.franquicias.dto.FranquiciaDTO;
import com.nequi.franquicias.dto.ProductoDTO;
import com.nequi.franquicias.dto.SucursalDTO;
import com.nequi.franquicias.model.Franquicia;
import com.nequi.franquicias.model.Producto;
import com.nequi.franquicias.model.Sucursal;
import com.nequi.franquicias.service.FranquiciaServicio;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Slf4j
@RestController
@RequestMapping("/api/v1")
public class FranquiciaController {
    private final FranquiciaServicio franquiciaServicio;

    public FranquiciaController(FranquiciaServicio franquiciaServicio) {
        this.franquiciaServicio = franquiciaServicio;
    }

    @PostMapping("/franquicias")
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

    @PostMapping("/franquicias/{franquiciaId}/sucursales")
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

    @PostMapping("/sucursales/{sucursalId}/productos")
    public Mono<ResponseEntity<Producto>> agregarProducto(
            @PathVariable Long sucursalId,
            @Valid @RequestBody ProductoDTO productoDTO) {
        log.info("Recibida solicitud para agregar producto a sucursal ID {}: {}", sucursalId, productoDTO);
        return franquiciaServicio.agregarProducto(sucursalId, productoDTO)
                .map(producto -> ResponseEntity.status(HttpStatus.CREATED).body(producto))
                .doOnSuccess(p -> log.info("Producto agregado exitosamente"))
                .onErrorResume(ex -> {
                    log.error("Error al agregar producto: {}", ex.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
                });
    }

    @DeleteMapping("/productos/{productoId}")
    public Mono<ResponseEntity<String>> eliminarProducto(@PathVariable Long productoId) {
        log.info("Recibida solicitud para eliminar producto ID: {}", productoId);
        return franquiciaServicio.eliminarProducto(productoId)
                .map(ResponseEntity::ok)
                .doOnSuccess(r -> log.info("Producto eliminado exitosamente"))
                .onErrorResume(ex -> {
                    log.error("Error al eliminar producto: {}", ex.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body("Error al eliminar producto"));
                });
    }

    @PutMapping("/productos/{productoId}/stock")
    public Mono<ResponseEntity<String>> actualizarStockProducto(
            @PathVariable Long productoId,
            @RequestBody Integer nuevoStock) {
        log.info("Recibida solicitud para actualizar stock del producto ID {}: {}", productoId, nuevoStock);
        return franquiciaServicio.actualizarStockProducto(productoId, nuevoStock)
                .map(ResponseEntity::ok)
                .doOnSuccess(r -> log.info("Stock actualizado exitosamente"))
                .onErrorResume(ex -> {
                    log.error("Error al actualizar stock: {}", ex.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body("Error al actualizar stock"));
                });
    }

    @GetMapping("/franquicias/{franquiciaId}/productos/mayor-stock")
    public Mono<ResponseEntity<Flux<Producto>>> obtenerProductosConMayorStock(
            @PathVariable Long franquiciaId) {
        log.info("Recibida solicitud para obtener productos con mayor stock de franquicia ID: {}", franquiciaId);
        return Mono.just(ResponseEntity.ok(franquiciaServicio.obtenerProductosConMayorStock(franquiciaId)))
                .doOnSuccess(r -> log.info("Consulta de productos completada exitosamente"))
                .onErrorResume(ex -> {
                    log.error("Error al consultar productos: {}", ex.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
                });
    }
}
