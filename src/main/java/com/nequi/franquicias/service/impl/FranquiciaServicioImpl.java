package com.nequi.franquicias.service.impl;

import com.nequi.franquicias.model.Franquicia;
import com.nequi.franquicias.repository.FranquiciaRepositorio;
import com.nequi.franquicias.repository.ProductoRepositorio;
import com.nequi.franquicias.repository.SucursalRepositorio;
import com.nequi.franquicias.service.FranquiciaServicio;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class FranquiciaServicioImpl implements FranquiciaServicio {
    private final FranquiciaRepositorio franquiciaRepositorio;
    private final SucursalRepositorio sucursalRepositorio;
    private final ProductoRepositorio productoRepositorio;

    public FranquiciaServicioImpl(FranquiciaRepositorio franquiciaRepositorio, SucursalRepositorio sucursalRepositorio, ProductoRepositorio productoRepositorio) {
        this.franquiciaRepositorio = franquiciaRepositorio;
        this.sucursalRepositorio = sucursalRepositorio;
        this.productoRepositorio = productoRepositorio;
    }

    @Override
    public Mono<Franquicia> crearFranquicia(Franquicia franquicia) {
        return franquiciaRepositorio.save(franquicia);
    }
}
