package com.nequi.franquicias.service.impl;

import com.nequi.franquicias.dto.FranquiciaDTO;
import com.nequi.franquicias.dto.ProductoDTO;
import com.nequi.franquicias.dto.SucursalDTO;
import com.nequi.franquicias.exception.BaseException;
import com.nequi.franquicias.exception.NotFoundException;
import com.nequi.franquicias.model.Franquicia;
import com.nequi.franquicias.model.Producto;
import com.nequi.franquicias.model.Sucursal;
import com.nequi.franquicias.repository.FranquiciaRepositorio;
import com.nequi.franquicias.repository.ProductoRepositorio;
import com.nequi.franquicias.repository.SucursalRepositorio;
import com.nequi.franquicias.service.FranquiciaServicio;
import com.nequi.franquicias.util.FranquiciaMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class FranquiciaServicioImpl implements FranquiciaServicio {
    private final FranquiciaRepositorio franquiciaRepositorio;
    private final SucursalRepositorio sucursalRepositorio;
    private final ProductoRepositorio productoRepositorio;
    private final FranquiciaMapper mapper;

    public FranquiciaServicioImpl(
            FranquiciaRepositorio franquiciaRepositorio,
            SucursalRepositorio sucursalRepositorio,
            ProductoRepositorio productoRepositorio,
            FranquiciaMapper mapper) {
        this.franquiciaRepositorio = franquiciaRepositorio;
        this.sucursalRepositorio = sucursalRepositorio;
        this.productoRepositorio = productoRepositorio;
        this.mapper = mapper;
    }

    @Override
    public Mono<Franquicia> crearFranquicia(FranquiciaDTO franquiciaDTO) {
        log.info("Iniciando creación de la franquicia con nombre: {}", franquiciaDTO.getNombre());

        return Mono.just(mapper.toEntity(franquiciaDTO))
                .flatMap(franquiciaRepositorio::save)
                .doOnSuccess(franquicia -> log.info("Franquicia creada exitosamente: {}", franquicia))
                .doOnError(error -> log.error("Error al crear la franquicia: {}", error.getMessage()))
                .onErrorMap(error -> new BaseException("Error al crear la franquicia", error, HttpStatus.INTERNAL_SERVER_ERROR) {

                });
    }

    @Override
    public Mono<Sucursal> agregarSucursal(Long franquiciaId, SucursalDTO sucursalDTO) {
        log.info("Agregando sucursal a franquicia ID: {}", franquiciaId);

        return franquiciaRepositorio.findById(franquiciaId)
                .switchIfEmpty(Mono.error(new NotFoundException("Franquicia no encontrada con ID: " + franquiciaId)))
                .map(franquicia -> mapper.toEntity(sucursalDTO, franquiciaId))
                .flatMap(sucursalRepositorio::save)
                .doOnSuccess(sucursal -> log.info("Sucursal agregada exitosamente: {}", sucursal))
                .doOnError(error -> log.error("Error al agregar sucursal: {}", error.getMessage()));
    }

    @Override
    public Mono<Producto> agregarProducto(Long sucursalId, ProductoDTO productoDTO) {
        log.info("Agregando producto a sucursal ID: {}", sucursalId);

        return sucursalRepositorio.findById(sucursalId)
                .switchIfEmpty(Mono.error(new NotFoundException("Sucursal no encontrada con ID: " + sucursalId)))
                .map(sucursal -> mapper.toEntity(productoDTO, sucursalId))
                .flatMap(productoRepositorio::save)
                .doOnSuccess(producto -> log.info("Producto agregado exitosamente: {}", producto))
                .doOnError(error -> log.error("Error al agregar producto: {}", error.getMessage()));
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
    public Flux<Producto> obtenerProductosConMayorStock(Long franquiciaId) {
        log.info("Consultando productos con mayor stock para franquicia ID: {}", franquiciaId);

        return sucursalRepositorio.findByFranquiciaId(franquiciaId)
                .flatMap(sucursal -> productoRepositorio.findBySucursalIdOrderByStockDesc(sucursal.getId()))
                .doOnComplete(() -> log.info("Consulta de productos completada"))
                .doOnError(error -> log.error("Error al consultar productos: {}", error.getMessage()));
    }
}