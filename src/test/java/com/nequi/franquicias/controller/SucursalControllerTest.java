package com.nequi.franquicias.controller;

import com.nequi.franquicias.dto.NombreNuevoDto;
import com.nequi.franquicias.dto.ProductoDTO;
import com.nequi.franquicias.model.Producto;
import com.nequi.franquicias.service.SucursalServicio;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@WebFluxTest(SucursalController.class)
class SucursalControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private SucursalServicio sucursalServicio;

    @Test
    void agregarProducto_creaProducto() {
        Producto producto = new Producto();
        producto.setId(10L);
        producto.setNombre("Pan");
        producto.setStock(15);

        when(sucursalServicio.agregarProducto(eq(1L), any(ProductoDTO.class)))
                .thenReturn(Mono.just(producto));

        ProductoDTO dto = new ProductoDTO();
        dto.setNombre("Pan");
        dto.setStock(15);

        webTestClient.post()
                .uri("/api/v1/sucursal/1/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.nombre").isEqualTo("Pan");
    }

    @Test
    void obtenerProductosConMayorStock_ok() {
        Producto producto = new Producto();
        producto.setId(5L);
        producto.setNombre("Leche");
        producto.setStock(100);

        when(sucursalServicio.obtenerProductosConMayorStock(99L))
                .thenReturn(Flux.just(producto));

        webTestClient.get()
                .uri("/api/v1/sucursal/99/productos/mayor-stock")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].nombre").isEqualTo("Leche");
    }

    @Test
    void actualizarNombreSucursal_ok() {
        when(sucursalServicio.actualizarNombreSucursal(eq(1L), any(NombreNuevoDto.class)))
                .thenReturn(Mono.just("Nombre de sucursal actualizado correctamente"));

        webTestClient.put()
                .uri("/api/v1/sucursal/1/nombre")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new NombreNuevoDto("Sucursal Norte"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("Nombre de sucursal actualizado correctamente");
    }
}
