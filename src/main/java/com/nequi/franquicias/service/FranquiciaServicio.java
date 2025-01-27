package com.nequi.franquicias.service;

import com.nequi.franquicias.model.Franquicia;
import reactor.core.publisher.Mono;

public interface FranquiciaServicio {

    Mono<Franquicia> crearFranquicia(Franquicia franquicia);

}
