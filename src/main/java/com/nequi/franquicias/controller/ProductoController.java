package com.nequi.franquicias.controller;


import com.nequi.franquicias.dto.NombreNuevoDto;
import com.nequi.franquicias.service.ProductoServicio;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/api/v1/producto")
public class ProductoController {

    private final ProductoServicio productoServicio;

    public ProductoController(ProductoServicio productoServicio) {
        this.productoServicio = productoServicio;
    }

    @DeleteMapping("/{productoId}")
    public Mono<ResponseEntity<String>> eliminarProducto(@PathVariable Long productoId) {
        log.info("Recibida solicitud para eliminar producto ID: {}", productoId);
        return productoServicio.eliminarProducto(productoId)
                .map(ResponseEntity::ok)
                .doOnSuccess(r -> log.info("Producto eliminado exitosamente"))
                .onErrorResume(ex -> {
                    log.error("Error al eliminar producto: {}", ex.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body("Error al eliminar producto"));
                });
    }

    @PutMapping("/{productoId}/stock")
    public Mono<ResponseEntity<String>> actualizarStockProducto(
            @PathVariable Long productoId,
            @RequestBody Integer nuevoStock) {
        log.info("Recibida solicitud para actualizar stock del producto ID {}: {}", productoId, nuevoStock);
        return productoServicio.actualizarStockProducto(productoId, nuevoStock)
                .map(ResponseEntity::ok)
                .doOnSuccess(r -> log.info("Stock actualizado exitosamente"))
                .onErrorResume(ex -> {
                    log.error("Error al actualizar stock: {}", ex.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body("Error al actualizar stock"));
                });
    }

    @PutMapping("/{productoId}/nombre")
    public Mono<ResponseEntity<String>> actualizarNombreProducto(
            @PathVariable Long productoId,
            @Valid @RequestBody NombreNuevoDto dto) {
        return productoServicio.actualizarNombreProducto(productoId, dto)
                .map(ResponseEntity::ok)
                .onErrorResume(ex -> {
                    log.error("Error al actualizar nombre de producto: {}", ex.getMessage());
                    return Mono.just(ResponseEntity.internalServerError().body(ex.getMessage()));
                });
    }
}
