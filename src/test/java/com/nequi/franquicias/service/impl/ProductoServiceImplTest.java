package com.nequi.franquicias.service.impl;

import com.nequi.franquicias.dto.NombreNuevoDto;
import com.nequi.franquicias.exception.NotFoundException;
import com.nequi.franquicias.model.Producto;
import com.nequi.franquicias.repository.ProductoRepositorio;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductoServiceImplTest {

    @Mock
    private ProductoRepositorio productoRepositorio;

    @InjectMocks
    private ProductoServiceImpl productoService;

    private Producto producto;
    private NombreNuevoDto nombreNuevoDto;

    @BeforeEach
    void setUp() {
        producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Producto Test");
        producto.setStock(5);

        nombreNuevoDto = new NombreNuevoDto();
        nombreNuevoDto.setNombre("Nuevo Nombre");
    }

    @Test
    void eliminarProducto_Exitoso_DeberiaRetornarMensajeCorrecto() {
        when(productoRepositorio.findById(1L)).thenReturn(Mono.just(producto));
        when(productoRepositorio.deleteById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(productoService.eliminarProducto(1L))
                .assertNext(mensaje -> assertEquals("Producto eliminado correctamente", mensaje))
                .verifyComplete();

        verify(productoRepositorio).findById(1L);
        verify(productoRepositorio).deleteById(1L);
    }

    @Test
    void eliminarProducto_NoEncontrado_DeberiaRetornarError() {
        when(productoRepositorio.findById(99L)).thenReturn(Mono.empty());

        StepVerifier.create(productoService.eliminarProducto(99L))
                .expectErrorSatisfies(error -> {
                    assertTrue(error instanceof NotFoundException);
                    assertTrue(error.getMessage().contains("Producto no encontrado"));
                })
                .verify();

        verify(productoRepositorio).findById(99L);
        verify(productoRepositorio, never()).deleteById(anyLong());
    }

    @Test
    void actualizarStockProducto_Exitoso_DeberiaRetornarMensajeCorrecto() {
        when(productoRepositorio.findById(1L)).thenReturn(Mono.just(producto));
        when(productoRepositorio.save(any(Producto.class))).thenReturn(Mono.just(producto));

        StepVerifier.create(productoService.actualizarStockProducto(1L, 10))
                .assertNext(mensaje -> assertEquals("Stock actualizado correctamente", mensaje))
                .verifyComplete();

        verify(productoRepositorio).findById(1L);
        verify(productoRepositorio).save(any(Producto.class));
        assertEquals(10, producto.getStock());
    }

    @Test
    void actualizarStockProducto_NoEncontrado_DeberiaRetornarError() {
        when(productoRepositorio.findById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(productoService.actualizarStockProducto(1L, 10))
                .expectErrorSatisfies(error -> assertTrue(error instanceof NotFoundException))
                .verify();

        verify(productoRepositorio).findById(1L);
        verify(productoRepositorio, never()).save(any());
    }


    @Test
    void actualizarNombreProducto_Exitoso_DeberiaRetornarMensajeCorrecto() {
        when(productoRepositorio.findById(1L)).thenReturn(Mono.just(producto));
        when(productoRepositorio.save(any(Producto.class))).thenReturn(Mono.just(producto));

        StepVerifier.create(productoService.actualizarNombreProducto(1L, nombreNuevoDto))
                .assertNext(mensaje -> assertEquals("Nombre de producto actualizado correctamente", mensaje))
                .verifyComplete();

        verify(productoRepositorio).findById(1L);
        verify(productoRepositorio).save(any(Producto.class));
        assertEquals("Nuevo Nombre", producto.getNombre());
    }

    @Test
    void actualizarNombreProducto_NoEncontrado_DeberiaRetornarError() {
        when(productoRepositorio.findById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(productoService.actualizarNombreProducto(1L, nombreNuevoDto))
                .expectErrorSatisfies(error -> assertTrue(error instanceof NotFoundException))
                .verify();

        verify(productoRepositorio).findById(1L);
        verify(productoRepositorio, never()).save(any());
    }


    @Test
    void actualizarStockProducto_ErrorGenerico_PropagaExcepcion() {
        when(productoRepositorio.findById(1L)).thenReturn(Mono.error(new RuntimeException("DB error")));

        StepVerifier.create(productoService.actualizarStockProducto(1L, 10))
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    void eliminarProducto_ErrorEnDelete_PropagaExcepcion() {
        when(productoRepositorio.findById(1L)).thenReturn(Mono.just(producto));
        when(productoRepositorio.deleteById(1L)).thenReturn(Mono.error(new RuntimeException("Fallo en delete")));

        StepVerifier.create(productoService.eliminarProducto(1L))
                .expectErrorSatisfies(err -> assertTrue(err.getMessage().contains("Fallo en delete")))
                .verify();
    }
}
