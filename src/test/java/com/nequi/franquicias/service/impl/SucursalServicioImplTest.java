package com.nequi.franquicias.service.impl;

import com.nequi.franquicias.dto.NombreNuevoDto;
import com.nequi.franquicias.dto.ProductoDTO;
import com.nequi.franquicias.exception.NotFoundException;
import com.nequi.franquicias.model.Producto;
import com.nequi.franquicias.model.Sucursal;
import com.nequi.franquicias.repository.ProductoRepositorio;
import com.nequi.franquicias.repository.SucursalRepositorio;
import com.nequi.franquicias.util.FranquiciaMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class SucursalServicioImplTest {

    @Mock
    private SucursalRepositorio sucursalRepositorio;

    @Mock
    private ProductoRepositorio productoRepositorio;

    @Mock
    private FranquiciaMapper mapper;

    @InjectMocks
    private SucursalServicioImpl sucursalServicio;

    private Sucursal sucursal;
    private Producto producto;
    private ProductoDTO productoDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sucursal = new Sucursal();
        sucursal.setId(1L);
        sucursal.setNombre("Sucursal Centro");

        producto = new Producto();
        producto.setId(10L);
        producto.setNombre("Café");
        producto.setStock(50);

        productoDTO = new ProductoDTO();
        productoDTO.setNombre("Café");
        productoDTO.setStock(50);
    }

    @Test
    void agregarProducto_ok() {
        when(sucursalRepositorio.findById(1L)).thenReturn(Mono.just(sucursal));
        when(mapper.toEntity(eq(productoDTO), eq(1L))).thenReturn(producto);
        when(productoRepositorio.save(any())).thenReturn(Mono.just(producto));

        StepVerifier.create(sucursalServicio.agregarProducto(1L, productoDTO))
                .expectNext(producto)
                .verifyComplete();

        verify(productoRepositorio).save(any());
    }

    @Test
    void agregarProducto_sucursalNoExiste() {
        when(sucursalRepositorio.findById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(sucursalServicio.agregarProducto(1L, productoDTO))
                .expectError(NotFoundException.class)
                .verify();
    }

    @Test
    void obtenerProductosConMayorStock_ok() {
        when(sucursalRepositorio.findByFranquiciaId(99L)).thenReturn(Flux.just(sucursal));
        when(productoRepositorio.findBySucursalIdOrderByStockDesc(1L))
                .thenReturn(Flux.just(producto));

        StepVerifier.create(sucursalServicio.obtenerProductosConMayorStock(99L))
                .expectNext(producto)
                .verifyComplete();
    }

    @Test
    void actualizarNombreSucursal_ok() {
        when(sucursalRepositorio.findById(1L)).thenReturn(Mono.just(sucursal));
        when(sucursalRepositorio.save(any())).thenReturn(Mono.just(sucursal));

        StepVerifier.create(
                        sucursalServicio.actualizarNombreSucursal(1L,
                                new NombreNuevoDto("Sucursal Norte")))
                .expectNext("Nombre de sucursal actualizado correctamente")
                .verifyComplete();

        verify(sucursalRepositorio).save(any());
    }
}
