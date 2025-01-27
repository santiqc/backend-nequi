package com.nequi.franquicias.controller;

import com.nequi.franquicias.model.Franquicia;
import com.nequi.franquicias.model.Producto;
import com.nequi.franquicias.model.Sucursal;
import com.nequi.franquicias.service.FranquiciaServicio;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/api")
public class FranquiciaController {
    private final FranquiciaServicio franquiciaServicio;

    public FranquiciaController(FranquiciaServicio franquiciaServicio) {
        this.franquiciaServicio = franquiciaServicio;
    }

    @PostMapping("/franquicias")
    public Mono<Franquicia> crearFranquicia(@RequestBody Franquicia franquicia) {
        return franquiciaServicio.crearFranquicia(franquicia);
    }

    @PostMapping("/franquicias/{franquiciaId}/sucursales/{sucursalId}")
    public Mono<Sucursal> agregarSucursal(@PathVariable Long franquiciaId, @PathVariable Long sucursalId) {
        return franquiciaServicio.agregarSucursal(franquiciaId, sucursalId);
    }

    @PostMapping("/sucursales/{sucursalId}/productos/{productoId}")
    public Mono<Producto> agregarProducto(@PathVariable Long sucursalId, @PathVariable Long productoId) {
        return franquiciaServicio.agregarProducto(sucursalId, productoId);
    }

    @DeleteMapping("/productos/{productoId}")
    public Mono<ResponseEntity<String>> eliminarProducto(@PathVariable Long productoId) {
        return franquiciaServicio.eliminarProducto(productoId)
                .map(ResponseEntity::ok)
                .onErrorResume(ex -> {
                    log.error("Error al eliminar el producto con ID {}: {}", productoId, ex.getMessage(), ex);
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar producto"));
                });
    }

    @PutMapping("/productos/{productoId}/stock/{stockId}")
    public Mono<ResponseEntity<String>> actualizarStockProducto(@PathVariable Long productoId, @PathVariable Integer stockId) {
        return franquiciaServicio.actualizarStockProducto(productoId, stockId)
                .map(ResponseEntity::ok)
                .onErrorResume(ex -> {
                    log.error("Error al actualizar el producto con ID {}: {}", productoId, ex.getMessage(), ex);
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar producto"));
                });
    }
}
