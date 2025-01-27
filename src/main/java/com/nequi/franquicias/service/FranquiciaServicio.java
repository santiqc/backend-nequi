package com.nequi.franquicias.service;

import com.nequi.franquicias.dto.FranquiciaDTO;

import com.nequi.franquicias.dto.SucursalDTO;
import com.nequi.franquicias.model.Franquicia;
import com.nequi.franquicias.model.Sucursal;
import reactor.core.publisher.Mono;

public interface FranquiciaServicio {

    Mono<Franquicia> crearFranquicia(FranquiciaDTO franquiciaDTO);
    Mono<Sucursal> agregarSucursal(Long franquiciaId, SucursalDTO sucursalDTO);

}
