package com.nequi.franquicias.service.impl;

import com.nequi.franquicias.dto.NombreNuevoDto;
import com.nequi.franquicias.exception.NotFoundException;
import com.nequi.franquicias.repository.ProductoRepositorio;
import com.nequi.franquicias.service.ProductoServicio;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class ProductoServiceImpl implements ProductoServicio {

    private final ProductoRepositorio productoRepositorio;

    public ProductoServiceImpl(ProductoRepositorio productoRepositorio) {
        this.productoRepositorio = productoRepositorio;

    }


    @Override
    public Mono<String> eliminarProducto(Long productoId) {
        log.info("Iniciando eliminación del producto con ID: {}", productoId);

        return productoRepositorio.findById(productoId)
                .switchIfEmpty(Mono.error(new NotFoundException("Producto no encontrado con ID: " + productoId)))
                .flatMap(producto -> productoRepositorio.deleteById(productoId)
                        .then(Mono.just("Producto eliminado correctamente")))
                .doOnSuccess(mensaje -> log.info("Producto eliminado exitosamente"))
                .doOnError(error -> log.error("Error al eliminar producto: {}", error.getMessage()));
    }

    @Override
    public Mono<String> actualizarStockProducto(Long productoId, Integer nuevoStock) {
        log.info("Actualizando stock del producto ID: {} a {}", productoId, nuevoStock);

        return productoRepositorio.findById(productoId)
                .switchIfEmpty(Mono.error(new NotFoundException("Producto no encontrado con ID: " + productoId)))
                .flatMap(producto -> {
                    producto.setStock(nuevoStock);
                    return productoRepositorio.save(producto)
                            .then(Mono.just("Stock actualizado correctamente"));
                })
                .doOnSuccess(mensaje -> log.info("Stock actualizado exitosamente"))
                .doOnError(error -> log.error("Error al actualizar stock: {}", error.getMessage()));
    }


    @Override
    public Mono<String> actualizarNombreProducto(Long productoId, NombreNuevoDto dto) {
        return productoRepositorio.findById(productoId)
                .switchIfEmpty(Mono.error(new NotFoundException("Producto no encontrado con ID: " + productoId)))
                .flatMap(producto -> {
                    producto.setNombre(dto.getNombre());
                    return productoRepositorio.save(producto);
                })
                .map(p -> "Nombre de producto actualizado correctamente")
                .doOnSuccess(result -> log.info("Nombre de producto {} actualizado a: {}", productoId, dto.getNombre()))
                .doOnError(error -> log.error("Error actualizando nombre de producto: {}", error.getMessage()));
    }

}
