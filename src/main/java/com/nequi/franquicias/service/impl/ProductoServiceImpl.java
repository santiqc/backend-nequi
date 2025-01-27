package com.nequi.franquicias.service.impl;

import com.nequi.franquicias.dto.ProductoDTO;
import com.nequi.franquicias.exception.NotFoundException;
import com.nequi.franquicias.model.Producto;
import com.nequi.franquicias.repository.FranquiciaRepositorio;
import com.nequi.franquicias.repository.ProductoRepositorio;
import com.nequi.franquicias.repository.SucursalRepositorio;
import com.nequi.franquicias.service.ProductoServicio;
import com.nequi.franquicias.util.FranquiciaMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class ProductoServiceImpl implements ProductoServicio {

    private final FranquiciaRepositorio franquiciaRepositorio;
    private final SucursalRepositorio sucursalRepositorio;
    private final ProductoRepositorio productoRepositorio;
    private final FranquiciaMapper mapper;

    public ProductoServiceImpl(FranquiciaRepositorio franquiciaRepositorio, SucursalRepositorio sucursalRepositorio, ProductoRepositorio productoRepositorio, FranquiciaMapper mapper) {
        this.franquiciaRepositorio = franquiciaRepositorio;
        this.sucursalRepositorio = sucursalRepositorio;
        this.productoRepositorio = productoRepositorio;
        this.mapper = mapper;
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


}
