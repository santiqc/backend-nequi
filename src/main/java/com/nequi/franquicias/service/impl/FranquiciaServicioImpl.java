package com.nequi.franquicias.service.impl;

import com.nequi.franquicias.dto.FranquiciaDTO;
import com.nequi.franquicias.dto.NombreNuevoDto;
import com.nequi.franquicias.dto.SucursalDTO;
import com.nequi.franquicias.exception.BaseException;
import com.nequi.franquicias.exception.NotFoundException;
import com.nequi.franquicias.model.Franquicia;
import com.nequi.franquicias.model.Sucursal;
import com.nequi.franquicias.repository.FranquiciaRepositorio;
import com.nequi.franquicias.repository.ProductoRepositorio;
import com.nequi.franquicias.repository.SucursalRepositorio;
import com.nequi.franquicias.service.FranquiciaServicio;
import com.nequi.franquicias.util.FranquiciaMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class FranquiciaServicioImpl implements FranquiciaServicio {
    private final FranquiciaRepositorio franquiciaRepositorio;
    private final SucursalRepositorio sucursalRepositorio;
    private final FranquiciaMapper mapper;

    public FranquiciaServicioImpl(
            FranquiciaRepositorio franquiciaRepositorio,
            SucursalRepositorio sucursalRepositorio,
            FranquiciaMapper mapper) {
        this.franquiciaRepositorio = franquiciaRepositorio;
        this.sucursalRepositorio = sucursalRepositorio;
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
    public Mono<String> actualizarNombreFranquicia(Long franquiciaId, NombreNuevoDto dto) {
        return franquiciaRepositorio.findById(franquiciaId)
                .switchIfEmpty(Mono.error(new NotFoundException("Franquicia no encontrada con ID: " + franquiciaId)))
                .flatMap(franquicia -> {
                    franquicia.setNombre(dto.getNombre());
                    return franquiciaRepositorio.save(franquicia);
                })
                .map(f -> "Nombre de franquicia actualizado correctamente")
                .doOnSuccess(result -> log.info("Nombre de franquicia {} actualizado a: {}", franquiciaId, dto.getNombre()))
                .doOnError(error -> log.error("Error actualizando nombre de franquicia: {}", error.getMessage()));
    }
}