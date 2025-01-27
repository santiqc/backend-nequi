package com.nequi.franquicias.repository;

import com.nequi.franquicias.model.Franquicia;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface FranquiciaRepositorio extends R2dbcRepository<Franquicia, Long> {
    Mono<Franquicia> findByNombre(String nombre);
}