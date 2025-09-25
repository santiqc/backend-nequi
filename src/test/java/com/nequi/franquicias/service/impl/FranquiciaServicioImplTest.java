package com.nequi.franquicias.service.impl;

import com.nequi.franquicias.dto.FranquiciaDTO;
import com.nequi.franquicias.dto.NombreNuevoDto;
import com.nequi.franquicias.dto.SucursalDTO;
import com.nequi.franquicias.exception.BaseException;
import com.nequi.franquicias.exception.NotFoundException;
import com.nequi.franquicias.model.Franquicia;
import com.nequi.franquicias.model.Sucursal;
import com.nequi.franquicias.repository.FranquiciaRepositorio;
import com.nequi.franquicias.repository.SucursalRepositorio;
import com.nequi.franquicias.util.FranquiciaMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FranquiciaServicioImplTest {

    @Mock
    private FranquiciaRepositorio franquiciaRepositorio;

    @Mock
    private SucursalRepositorio sucursalRepositorio;

    @Mock
    private FranquiciaMapper mapper;

    @InjectMocks
    private FranquiciaServicioImpl franquiciaServicio;

    private FranquiciaDTO franquiciaDTO;
    private Franquicia franquicia;
    private SucursalDTO sucursalDTO;
    private Sucursal sucursal;
    private NombreNuevoDto nombreNuevoDto;

    @BeforeEach
    void setUp() {
        franquiciaDTO = new FranquiciaDTO();
        franquiciaDTO.setNombre("Franquicia Test");

        franquicia = new Franquicia();
        franquicia.setId(1L);
        franquicia.setNombre("Franquicia Test");

        sucursalDTO = new SucursalDTO();
        sucursalDTO.setNombre("Sucursal Test");

        sucursal = new Sucursal();
        sucursal.setId(1L);
        sucursal.setNombre("Sucursal Test");
        sucursal.setFranquiciaId(1L);

        nombreNuevoDto = new NombreNuevoDto();
        nombreNuevoDto.setNombre("Nuevo Nombre");
    }

    @Test
    void crearFranquicia_ExitosamenteCreada() {

        when(mapper.toEntity(franquiciaDTO)).thenReturn(franquicia);
        when(franquiciaRepositorio.save(any(Franquicia.class))).thenReturn(Mono.just(franquicia));


        StepVerifier.create(franquiciaServicio.crearFranquicia(franquiciaDTO))
                .expectNext(franquicia)
                .verifyComplete();

        verify(mapper).toEntity(franquiciaDTO);
        verify(franquiciaRepositorio).save(any(Franquicia.class));
    }

    @Test
    void crearFranquicia_ErrorAlGuardar_DeberiaLanzarBaseException() {

        RuntimeException originalError = new RuntimeException("Error de base de datos");
        when(mapper.toEntity(franquiciaDTO)).thenReturn(franquicia);
        when(franquiciaRepositorio.save(any(Franquicia.class))).thenReturn(Mono.error(originalError));


        StepVerifier.create(franquiciaServicio.crearFranquicia(franquiciaDTO))
                .expectError(BaseException.class)
                .verify();

        verify(mapper).toEntity(franquiciaDTO);
        verify(franquiciaRepositorio).save(any(Franquicia.class));
    }

    @Test
    void agregarSucursal_FranquiciaExiste_DeberiaAgregarSucursal() {

        Long franquiciaId = 1L;
        when(franquiciaRepositorio.findById(franquiciaId)).thenReturn(Mono.just(franquicia));
        when(mapper.toEntity(sucursalDTO, franquiciaId)).thenReturn(sucursal);
        when(sucursalRepositorio.save(any(Sucursal.class))).thenReturn(Mono.just(sucursal));


        StepVerifier.create(franquiciaServicio.agregarSucursal(franquiciaId, sucursalDTO))
                .expectNext(sucursal)
                .verifyComplete();

        verify(franquiciaRepositorio).findById(franquiciaId);
        verify(mapper).toEntity(sucursalDTO, franquiciaId);
        verify(sucursalRepositorio).save(any(Sucursal.class));
    }

    @Test
    void agregarSucursal_FranquiciaNoExiste_DeberiaLanzarNotFoundException() {

        Long franquiciaId = 999L;
        when(franquiciaRepositorio.findById(franquiciaId)).thenReturn(Mono.empty());


        StepVerifier.create(franquiciaServicio.agregarSucursal(franquiciaId, sucursalDTO))
                .expectErrorMatches(throwable -> throwable instanceof NotFoundException &&
                                                 throwable.getMessage().contains("Franquicia no encontrada con ID: " + franquiciaId))
                .verify();

        verify(franquiciaRepositorio).findById(franquiciaId);
        verifyNoInteractions(mapper);
        verifyNoInteractions(sucursalRepositorio);
    }

    @Test
    void agregarSucursal_ErrorAlGuardarSucursal_DeberiaLanzarError() {

        Long franquiciaId = 1L;
        RuntimeException error = new RuntimeException("Error al guardar sucursal");
        when(franquiciaRepositorio.findById(franquiciaId)).thenReturn(Mono.just(franquicia));
        when(mapper.toEntity(sucursalDTO, franquiciaId)).thenReturn(sucursal);
        when(sucursalRepositorio.save(any(Sucursal.class))).thenReturn(Mono.error(error));


        StepVerifier.create(franquiciaServicio.agregarSucursal(franquiciaId, sucursalDTO))
                .expectError(RuntimeException.class)
                .verify();

        verify(franquiciaRepositorio).findById(franquiciaId);
        verify(mapper).toEntity(sucursalDTO, franquiciaId);
        verify(sucursalRepositorio).save(any(Sucursal.class));
    }

    @Test
    void actualizarNombreFranquicia_FranquiciaExiste_DeberiaActualizarNombre() {

        Long franquiciaId = 1L;
        Franquicia franquiciaActualizada = new Franquicia();
        franquiciaActualizada.setId(franquiciaId);
        franquiciaActualizada.setNombre(nombreNuevoDto.getNombre());

        when(franquiciaRepositorio.findById(franquiciaId)).thenReturn(Mono.just(franquicia));
        when(franquiciaRepositorio.save(any(Franquicia.class))).thenReturn(Mono.just(franquiciaActualizada));


        StepVerifier.create(franquiciaServicio.actualizarNombreFranquicia(franquiciaId, nombreNuevoDto))
                .expectNext("Nombre de franquicia actualizado correctamente")
                .verifyComplete();

        verify(franquiciaRepositorio).findById(franquiciaId);
        verify(franquiciaRepositorio).save(any(Franquicia.class));

        assertEquals(nombreNuevoDto.getNombre(), franquicia.getNombre());
    }

    @Test
    void actualizarNombreFranquicia_FranquiciaNoExiste_DeberiaLanzarNotFoundException() {

        Long franquiciaId = 999L;
        when(franquiciaRepositorio.findById(franquiciaId)).thenReturn(Mono.empty());


        StepVerifier.create(franquiciaServicio.actualizarNombreFranquicia(franquiciaId, nombreNuevoDto))
                .expectErrorMatches(throwable -> throwable instanceof NotFoundException &&
                                                 throwable.getMessage().contains("Franquicia no encontrada con ID: " + franquiciaId))
                .verify();

        verify(franquiciaRepositorio).findById(franquiciaId);
        verify(franquiciaRepositorio, never()).save(any(Franquicia.class));
    }

    @Test
    void actualizarNombreFranquicia_ErrorAlGuardar_DeberiaLanzarError() {

        Long franquiciaId = 1L;
        RuntimeException error = new RuntimeException("Error al guardar");
        when(franquiciaRepositorio.findById(franquiciaId)).thenReturn(Mono.just(franquicia));
        when(franquiciaRepositorio.save(any(Franquicia.class))).thenReturn(Mono.error(error));


        StepVerifier.create(franquiciaServicio.actualizarNombreFranquicia(franquiciaId, nombreNuevoDto))
                .expectError(RuntimeException.class)
                .verify();

        verify(franquiciaRepositorio).findById(franquiciaId);
        verify(franquiciaRepositorio).save(any(Franquicia.class));
    }
}