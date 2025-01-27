package com.nequi.franquicias.service.impl;

import com.nequi.franquicias.dto.ProductoDTO;
import com.nequi.franquicias.exception.NotFoundException;
import com.nequi.franquicias.model.Producto;
import com.nequi.franquicias.repository.FranquiciaRepositorio;
import com.nequi.franquicias.repository.ProductoRepositorio;
import com.nequi.franquicias.repository.SucursalRepositorio;
import com.nequi.franquicias.service.SucursalServicio;
import com.nequi.franquicias.util.FranquiciaMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class SucursalServicioImpl implements SucursalServicio {

    private final SucursalRepositorio sucursalRepositorio;
    private final ProductoRepositorio productoRepositorio;
    private final FranquiciaMapper mapper;

    public SucursalServicioImpl(SucursalRepositorio sucursalRepositorio, ProductoRepositorio productoRepositorio, FranquiciaMapper mapper) {;
        this.sucursalRepositorio = sucursalRepositorio;
        this.productoRepositorio = productoRepositorio;
        this.mapper = mapper;
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
    public Flux<Producto> obtenerProductosConMayorStock(Long franquiciaId) {
        log.info("Consultando productos con mayor stock para franquicia ID: {}", franquiciaId);

        return sucursalRepositorio.findByFranquiciaId(franquiciaId)
                .flatMap(sucursal -> productoRepositorio.findBySucursalIdOrderByStockDesc(sucursal.getId()))
                .doOnComplete(() -> log.info("Consulta de productos completada"))
                .doOnError(error -> log.error("Error al consultar productos: {}", error.getMessage()));
    }
}
