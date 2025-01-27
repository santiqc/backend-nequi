package com.nequi.franquicias.service.impl;

import com.nequi.franquicias.exception.BaseException;
import com.nequi.franquicias.exception.NotFoundException;
import com.nequi.franquicias.model.Franquicia;
import com.nequi.franquicias.model.Producto;
import com.nequi.franquicias.model.Sucursal;
import com.nequi.franquicias.repository.FranquiciaRepositorio;
import com.nequi.franquicias.repository.ProductoRepositorio;
import com.nequi.franquicias.repository.SucursalRepositorio;
import com.nequi.franquicias.service.FranquiciaServicio;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class FranquiciaServicioImpl implements FranquiciaServicio {
    private final FranquiciaRepositorio franquiciaRepositorio;
    private final SucursalRepositorio sucursalRepositorio;
    private final ProductoRepositorio productoRepositorio;

    private static Logger LOG = LoggerFactory.getLogger(FranquiciaServicioImpl.class);

    public FranquiciaServicioImpl(FranquiciaRepositorio franquiciaRepositorio, SucursalRepositorio sucursalRepositorio, ProductoRepositorio productoRepositorio) {
        this.franquiciaRepositorio = franquiciaRepositorio;
        this.sucursalRepositorio = sucursalRepositorio;
        this.productoRepositorio = productoRepositorio;
    }

    @Override
    public Mono<Franquicia> crearFranquicia(Franquicia franquicia) {
        LOG.info("Iniciando creación de la franquicia: {}", franquicia);

        return franquiciaRepositorio.save(franquicia)
                .doOnSuccess(franquiciaGuardada -> LOG.info("Franquicia creada exitosamente: {}", franquiciaGuardada))
                .doOnError(error -> LOG.error("Error al crear la franquicia: {}", error.getMessage(), error))
                .onErrorMap(error -> new BaseException("Error al crear la franquicia", error, HttpStatus.INTERNAL_SERVER_ERROR) {
                });
    }


    @Override
    public Mono<Sucursal> agregarSucursal(Long franquiciaId, Long sucursalId) {
        LOG.info("Agregando sucursal con ID: {} a franquicia con ID: {}", sucursalId, franquiciaId);
        return sucursalRepositorio.findById(sucursalId)
                .switchIfEmpty(Mono.error(new NotFoundException("Sucursal no encontrada con ID: " + sucursalId)))
                .flatMap(sucursal -> {
                    sucursal.setFranquiciaId(franquiciaId);
                    LOG.debug("Sucursal actualizada: {}", sucursal);
                    return sucursalRepositorio.save(sucursal);
                });
    }

    @Override
    public Mono<Producto> agregarProducto(Long sucursalId, Long productoId) {
        LOG.info("Agregando produco con ID: {} a una sucursal con ID: {}", productoId, sucursalId);
        return productoRepositorio.findById(productoId)
                .switchIfEmpty(Mono.error(new NotFoundException("Producto no encontrado con ID: " + productoId)))
                .flatMap(producto -> {
                    producto.setSucursalId(sucursalId);
                    LOG.debug("Producto actualizado: {}", producto);
                    return productoRepositorio.save(producto);
                });
    }

    @Override
    public Mono<String> eliminarProducto(Long productoId) {
        log.info("Iniciando eliminación del producto con ID: {}", productoId);

        return productoRepositorio.findById(productoId)
                .flatMap(producto -> {
                    log.info("Producto encontrado: {}", producto);
                    return productoRepositorio.deleteById(productoId)
                            .then(Mono.just("Producto eliminado correctamente"));
                })
                .switchIfEmpty(Mono.error(new NotFoundException("Producto no encontrado con ID: " + productoId)))
                .doOnError(ex -> log.error("Error al eliminar el producto con ID {}: {}", productoId, ex.getMessage(), ex));
    }

    @Override
    public Mono<String> actualizarStockProducto(Long productoId, Integer nuevoStock) {
        LOG.info("Iniciando la actualización del stock para el producto con ID: {}", productoId);

        return productoRepositorio.findById(productoId)
                .switchIfEmpty(Mono.error(new NotFoundException("Producto no encontrado con ID: " + productoId)))
                .flatMap(producto -> {
                    producto.setStock(nuevoStock);
                    LOG.info("Producto actualizado: {} con nuevo stock: {}", producto, nuevoStock);
                    return productoRepositorio.save(producto)
                            .then(Mono.just("Producto actualizado correctamente"));
                })
                .doOnError(error -> LOG.error("Error al actualizar el stock del producto con ID: {}", productoId, error));
    }


}
