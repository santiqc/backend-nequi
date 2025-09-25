package com.nequi.franquicias.controller;

import com.nequi.franquicias.dto.NombreNuevoDto;
import com.nequi.franquicias.exception.NotFoundException;
import com.nequi.franquicias.service.ProductoServicio;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductoControllerTest {

    @Mock
    private ProductoServicio productoServicio;

    @InjectMocks
    private ProductoController productoController;

    private NombreNuevoDto nombreNuevoDto;

    @BeforeEach
    void setUp() {
        nombreNuevoDto = new NombreNuevoDto();
        nombreNuevoDto.setNombre("Nuevo Nombre Producto");
    }

    @Test
    void eliminarProducto_Exitoso_DeberiaRetornarOk() {
        Long productoId = 1L;
        when(productoServicio.eliminarProducto(productoId))
                .thenReturn(Mono.just("Producto eliminado correctamente"));

        StepVerifier.create(productoController.eliminarProducto(productoId))
                .assertNext(resp -> {
                    assertEquals(HttpStatus.OK, resp.getStatusCode());
                    assertEquals("Producto eliminado correctamente", resp.getBody());
                })
                .verifyComplete();

        verify(productoServicio).eliminarProducto(productoId);
    }

    @Test
    void eliminarProducto_NoEncontrado_DeberiaRetornarInternalServerError() {
        Long productoId = 99L;
        when(productoServicio.eliminarProducto(productoId))
                .thenReturn(Mono.error(new NotFoundException("Producto no encontrado con ID: " + productoId)));

        StepVerifier.create(productoController.eliminarProducto(productoId))
                .assertNext(resp -> {
                    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, resp.getStatusCode());
                    assertEquals("Error al eliminar producto", resp.getBody());
                })
                .verifyComplete();

        verify(productoServicio).eliminarProducto(productoId);
    }

    @Test
    void eliminarProducto_ErrorGenerico_DeberiaRetornarInternalServerError() {
        Long productoId = 1L;
        when(productoServicio.eliminarProducto(productoId))
                .thenReturn(Mono.error(new RuntimeException("Error genérico")));

        StepVerifier.create(productoController.eliminarProducto(productoId))
                .assertNext(resp -> {
                    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, resp.getStatusCode());
                    assertEquals("Error al eliminar producto", resp.getBody());
                })
                .verifyComplete();

        verify(productoServicio).eliminarProducto(productoId);
    }

    @Test
    void actualizarStockProducto_Exitoso_DeberiaRetornarOk() {
        Long productoId = 1L;
        Integer nuevoStock = 10;
        when(productoServicio.actualizarStockProducto(productoId, nuevoStock))
                .thenReturn(Mono.just("Stock actualizado correctamente"));

        StepVerifier.create(productoController.actualizarStockProducto(productoId, nuevoStock))
                .assertNext(resp -> {
                    assertEquals(HttpStatus.OK, resp.getStatusCode());
                    assertEquals("Stock actualizado correctamente", resp.getBody());
                })
                .verifyComplete();

        verify(productoServicio).actualizarStockProducto(productoId, nuevoStock);
    }

    @Test
    void actualizarStockProducto_NoEncontrado_DeberiaRetornarInternalServerError() {
        Long productoId = 2L;
        Integer nuevoStock = 5;
        when(productoServicio.actualizarStockProducto(productoId, nuevoStock))
                .thenReturn(Mono.error(new NotFoundException("Producto no encontrado")));

        StepVerifier.create(productoController.actualizarStockProducto(productoId, nuevoStock))
                .assertNext(resp -> {
                    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, resp.getStatusCode());
                    assertEquals("Error al actualizar stock", resp.getBody());
                })
                .verifyComplete();

        verify(productoServicio).actualizarStockProducto(productoId, nuevoStock);
    }

    @Test
    void actualizarStockProducto_ErrorGenerico_DeberiaRetornarInternalServerError() {
        Long productoId = 1L;
        Integer nuevoStock = 5;
        when(productoServicio.actualizarStockProducto(productoId, nuevoStock))
                .thenReturn(Mono.error(new RuntimeException("Fallo inesperado")));

        StepVerifier.create(productoController.actualizarStockProducto(productoId, nuevoStock))
                .assertNext(resp -> {
                    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, resp.getStatusCode());
                    assertEquals("Error al actualizar stock", resp.getBody());
                })
                .verifyComplete();

        verify(productoServicio).actualizarStockProducto(productoId, nuevoStock);
    }

    @Test
    void actualizarNombreProducto_Exitoso_DeberiaRetornarOk() {
        Long productoId = 1L;
        when(productoServicio.actualizarNombreProducto(productoId, nombreNuevoDto))
                .thenReturn(Mono.just("Nombre de producto actualizado correctamente"));

        StepVerifier.create(productoController.actualizarNombreProducto(productoId, nombreNuevoDto))
                .assertNext(resp -> {
                    assertEquals(HttpStatus.OK, resp.getStatusCode());
                    assertEquals("Nombre de producto actualizado correctamente", resp.getBody());
                })
                .verifyComplete();

        verify(productoServicio).actualizarNombreProducto(productoId, nombreNuevoDto);
    }

    @Test
    void actualizarNombreProducto_NoEncontrado_DeberiaRetornarInternalServerError() {
        Long productoId = 10L;
        when(productoServicio.actualizarNombreProducto(productoId, nombreNuevoDto))
                .thenReturn(Mono.error(new NotFoundException("Producto no encontrado con ID: " + productoId)));

        StepVerifier.create(productoController.actualizarNombreProducto(productoId, nombreNuevoDto))
                .assertNext(resp -> {
                    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, resp.getStatusCode());
                    assertTrue(resp.getBody().contains("Producto no encontrado"));
                })
                .verifyComplete();

        verify(productoServicio).actualizarNombreProducto(productoId, nombreNuevoDto);
    }

    @Test
    void actualizarNombreProducto_ErrorGenerico_DeberiaRetornarInternalServerError() {
        Long productoId = 1L;
        when(productoServicio.actualizarNombreProducto(productoId, nombreNuevoDto))
                .thenReturn(Mono.error(new RuntimeException("Error inesperado")));

        StepVerifier.create(productoController.actualizarNombreProducto(productoId, nombreNuevoDto))
                .assertNext(resp -> {
                    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, resp.getStatusCode());
                    assertEquals("Error inesperado", resp.getBody());
                })
                .verifyComplete();

        verify(productoServicio).actualizarNombreProducto(productoId, nombreNuevoDto);
    }

    @Test
    void actualizarStockProducto_ConParametrosValidos_DeberiaInvocarServicioCorrectamente() {
        Long productoId = 123L;
        Integer nuevoStock = 7;
        when(productoServicio.actualizarStockProducto(eq(productoId), eq(nuevoStock)))
                .thenReturn(Mono.just("ok"));

        StepVerifier.create(productoController.actualizarStockProducto(productoId, nuevoStock))
                .expectNextCount(1)
                .verifyComplete();

        verify(productoServicio).actualizarStockProducto(productoId, nuevoStock);
    }

    @Test
    void actualizarNombreProducto_ConParametrosValidos_DeberiaInvocarServicioCorrectamente() {
        Long productoId = 456L;
        when(productoServicio.actualizarNombreProducto(eq(productoId), eq(nombreNuevoDto)))
                .thenReturn(Mono.just("Actualizado"));

        StepVerifier.create(productoController.actualizarNombreProducto(productoId, nombreNuevoDto))
                .assertNext(resp -> {
                    assertEquals(HttpStatus.OK, resp.getStatusCode());
                    assertEquals("Actualizado", resp.getBody());
                })
                .verifyComplete();

        verify(productoServicio).actualizarNombreProducto(productoId, nombreNuevoDto);
    }
}
