package com.nequi.franquicias.controller;

import com.nequi.franquicias.dto.FranquiciaDTO;
import com.nequi.franquicias.dto.NombreNuevoDto;
import com.nequi.franquicias.dto.SucursalDTO;
import com.nequi.franquicias.exception.BaseException;
import com.nequi.franquicias.exception.NotFoundException;
import com.nequi.franquicias.model.Franquicia;
import com.nequi.franquicias.model.Sucursal;
import com.nequi.franquicias.service.FranquiciaServicio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FranquiciaControllerTest {

    @Mock
    private FranquiciaServicio franquiciaServicio;

    @InjectMocks
    private FranquiciaController franquiciaController;

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
    void crearFranquicia_ExitosamenteCreada_DeberiaRetornarCreated() {

        when(franquiciaServicio.crearFranquicia(any(FranquiciaDTO.class)))
                .thenReturn(Mono.just(franquicia));


        StepVerifier.create(franquiciaController.crearFranquicia(franquiciaDTO))
                .assertNext(responseEntity -> {
                    assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
                    assertNotNull(responseEntity.getBody());
                    assertEquals(franquicia.getNombre(), responseEntity.getBody().getNombre());
                    assertEquals(franquicia.getId(), responseEntity.getBody().getId());
                })
                .verifyComplete();

        verify(franquiciaServicio).crearFranquicia(franquiciaDTO);
    }

    @Test
    void crearFranquicia_ErrorEnServicio_DeberiaRetornarInternalServerError() {

        BaseException error = new BaseException("Error al crear franquicia", new RuntimeException(), HttpStatus.INTERNAL_SERVER_ERROR) {};
        when(franquiciaServicio.crearFranquicia(any(FranquiciaDTO.class)))
                .thenReturn(Mono.error(error));


        StepVerifier.create(franquiciaController.crearFranquicia(franquiciaDTO))
                .assertNext(responseEntity -> {
                    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
                    assertNull(responseEntity.getBody());
                })
                .verifyComplete();

        verify(franquiciaServicio).crearFranquicia(franquiciaDTO);
    }

    @Test
    void crearFranquicia_ErrorGenerico_DeberiaRetornarInternalServerError() {

        RuntimeException error = new RuntimeException("Error genérico");
        when(franquiciaServicio.crearFranquicia(any(FranquiciaDTO.class)))
                .thenReturn(Mono.error(error));


        StepVerifier.create(franquiciaController.crearFranquicia(franquiciaDTO))
                .assertNext(responseEntity -> {
                    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
                    assertNull(responseEntity.getBody());
                })
                .verifyComplete();

        verify(franquiciaServicio).crearFranquicia(franquiciaDTO);
    }

    @Test
    void agregarSucursal_ExitosamenteAgregada_DeberiaRetornarCreated() {

        Long franquiciaId = 1L;
        when(franquiciaServicio.agregarSucursal(anyLong(), any(SucursalDTO.class)))
                .thenReturn(Mono.just(sucursal));


        StepVerifier.create(franquiciaController.agregarSucursal(franquiciaId, sucursalDTO))
                .assertNext(responseEntity -> {
                    assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
                    assertNotNull(responseEntity.getBody());
                    assertEquals(sucursal.getNombre(), responseEntity.getBody().getNombre());
                    assertEquals(sucursal.getId(), responseEntity.getBody().getId());
                    assertEquals(sucursal.getFranquiciaId(), responseEntity.getBody().getFranquiciaId());
                })
                .verifyComplete();

        verify(franquiciaServicio).agregarSucursal(franquiciaId, sucursalDTO);
    }

    @Test
    void agregarSucursal_FranquiciaNoEncontrada_DeberiaRetornarInternalServerError() {

        Long franquiciaId = 999L;
        NotFoundException error = new NotFoundException("Franquicia no encontrada");
        when(franquiciaServicio.agregarSucursal(anyLong(), any(SucursalDTO.class)))
                .thenReturn(Mono.error(error));


        StepVerifier.create(franquiciaController.agregarSucursal(franquiciaId, sucursalDTO))
                .assertNext(responseEntity -> {
                    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
                    assertNull(responseEntity.getBody());
                })
                .verifyComplete();

        verify(franquiciaServicio).agregarSucursal(franquiciaId, sucursalDTO);
    }

    @Test
    void agregarSucursal_ErrorGenerico_DeberiaRetornarInternalServerError() {

        Long franquiciaId = 1L;
        RuntimeException error = new RuntimeException("Error al agregar sucursal");
        when(franquiciaServicio.agregarSucursal(anyLong(), any(SucursalDTO.class)))
                .thenReturn(Mono.error(error));


        StepVerifier.create(franquiciaController.agregarSucursal(franquiciaId, sucursalDTO))
                .assertNext(responseEntity -> {
                    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
                    assertNull(responseEntity.getBody());
                })
                .verifyComplete();

        verify(franquiciaServicio).agregarSucursal(franquiciaId, sucursalDTO);
    }

    @Test
    void actualizarNombreFranquicia_ExitosamenteActualizado_DeberiaRetornarOk() {

        Long franquiciaId = 1L;
        String mensajeExito = "Nombre de franquicia actualizado correctamente";
        when(franquiciaServicio.actualizarNombreFranquicia(anyLong(), any(NombreNuevoDto.class)))
                .thenReturn(Mono.just(mensajeExito));


        StepVerifier.create(franquiciaController.actualizarNombreFranquicia(franquiciaId, nombreNuevoDto))
                .assertNext(responseEntity -> {
                    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
                    assertNotNull(responseEntity.getBody());
                    assertEquals(mensajeExito, responseEntity.getBody());
                })
                .verifyComplete();

        verify(franquiciaServicio).actualizarNombreFranquicia(franquiciaId, nombreNuevoDto);
    }

    @Test
    void actualizarNombreFranquicia_FranquiciaNoEncontrada_DeberiaRetornarInternalServerError() {

        Long franquiciaId = 999L;
        NotFoundException error = new NotFoundException("Franquicia no encontrada con ID: " + franquiciaId);
        when(franquiciaServicio.actualizarNombreFranquicia(anyLong(), any(NombreNuevoDto.class)))
                .thenReturn(Mono.error(error));


        StepVerifier.create(franquiciaController.actualizarNombreFranquicia(franquiciaId, nombreNuevoDto))
                .assertNext(responseEntity -> {
                    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
                    assertNotNull(responseEntity.getBody());
                    assertTrue(responseEntity.getBody().contains("Franquicia no encontrada"));
                })
                .verifyComplete();

        verify(franquiciaServicio).actualizarNombreFranquicia(franquiciaId, nombreNuevoDto);
    }

    @Test
    void actualizarNombreFranquicia_ErrorGenerico_DeberiaRetornarInternalServerError() {

        Long franquiciaId = 1L;
        RuntimeException error = new RuntimeException("Error interno del servidor");
        when(franquiciaServicio.actualizarNombreFranquicia(anyLong(), any(NombreNuevoDto.class)))
                .thenReturn(Mono.error(error));


        StepVerifier.create(franquiciaController.actualizarNombreFranquicia(franquiciaId, nombreNuevoDto))
                .assertNext(responseEntity -> {
                    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
                    assertNotNull(responseEntity.getBody());
                    assertEquals("Error interno del servidor", responseEntity.getBody());
                })
                .verifyComplete();

        verify(franquiciaServicio).actualizarNombreFranquicia(franquiciaId, nombreNuevoDto);
    }

    @Test
    void crearFranquicia_ConDTONull_DeberiaFuncionar() {

        when(franquiciaServicio.crearFranquicia(isNull()))
                .thenReturn(Mono.just(franquicia));


        StepVerifier.create(franquiciaController.crearFranquicia(null))
                .assertNext(responseEntity -> {
                    assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
                    assertNotNull(responseEntity.getBody());
                })
                .verifyComplete();

        verify(franquiciaServicio).crearFranquicia(null);
    }

    @Test
    void agregarSucursal_ConParametrosValidos_DeberiaLlamarServicioConParametrosCorrectos() {

        Long franquiciaId = 123L;
        when(franquiciaServicio.agregarSucursal(eq(franquiciaId), eq(sucursalDTO)))
                .thenReturn(Mono.just(sucursal));


        StepVerifier.create(franquiciaController.agregarSucursal(franquiciaId, sucursalDTO))
                .expectNextCount(1)
                .verifyComplete();


        verify(franquiciaServicio).agregarSucursal(franquiciaId, sucursalDTO);
    }

    @Test
    void actualizarNombreFranquicia_ConParametrosValidos_DeberiaLlamarServicioCorrectamente() {

        Long franquiciaId = 456L;
        String mensaje = "Actualizado exitosamente";
        when(franquiciaServicio.actualizarNombreFranquicia(eq(franquiciaId), eq(nombreNuevoDto)))
                .thenReturn(Mono.just(mensaje));


        StepVerifier.create(franquiciaController.actualizarNombreFranquicia(franquiciaId, nombreNuevoDto))
                .assertNext(responseEntity -> {
                    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
                    assertEquals(mensaje, responseEntity.getBody());
                })
                .verifyComplete();

        verify(franquiciaServicio).actualizarNombreFranquicia(franquiciaId, nombreNuevoDto);
    }
}