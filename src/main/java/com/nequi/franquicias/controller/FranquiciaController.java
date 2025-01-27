package com.nequi.franquicias.controller;

import com.nequi.franquicias.model.Franquicia;
import com.nequi.franquicias.model.Sucursal;
import com.nequi.franquicias.service.FranquiciaServicio;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
public class FranquiciaController {
    private final FranquiciaServicio franquiciaServicio;

    public FranquiciaController(FranquiciaServicio franquiciaServicio) {
        this.franquiciaServicio = franquiciaServicio;
    }

    @PostMapping("/franquicias")
    public Mono<Franquicia> crearFranquicia(@RequestBody Franquicia franquicia) {
        return franquiciaServicio.crearFranquicia(franquicia);
    }

    @PostMapping("/franquicias/{franquiciaId}/sucursales/{sucursalId}")
    public Mono<Sucursal> agregarSucursal(@PathVariable Long franquiciaId, @PathVariable Long sucursalId) {
        return franquiciaServicio.agregarSucursal(franquiciaId, sucursalId);
    }

}
