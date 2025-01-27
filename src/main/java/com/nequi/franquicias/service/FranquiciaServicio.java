package com.nequi.franquicias.service;

import com.nequi.franquicias.dto.FranquiciaDTO;
import com.nequi.franquicias.dto.ProductoDTO;
import com.nequi.franquicias.dto.SucursalDTO;
import com.nequi.franquicias.model.Franquicia;
import com.nequi.franquicias.model.Producto;
import com.nequi.franquicias.model.Sucursal;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FranquiciaServicio {

    Mono<Franquicia> crearFranquicia(FranquiciaDTO franquiciaDTO);
    Mono<Sucursal> agregarSucursal(Long franquiciaId, SucursalDTO sucursalDTO);
    Mono<Producto> agregarProducto(Long sucursalId, ProductoDTO productoDTO);
    Mono<String> eliminarProducto(Long productoId);
    Mono<String> actualizarStockProducto(Long productoId, Integer nuevoStock);
    Flux<Producto> obtenerProductosConMayorStock(Long franquiciaId);
}
