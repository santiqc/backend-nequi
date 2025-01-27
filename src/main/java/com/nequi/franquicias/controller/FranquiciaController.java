package com.nequi.franquicias.controller;

import com.nequi.franquicias.model.Franquicia;
import com.nequi.franquicias.service.FranquiciaServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FranquiciaController {
    private final FranquiciaServicio franquiciaServicio;

    @PostMapping("/franquicias")
    public Mono<Franquicia> crearFranquicia(@RequestBody Franquicia franquicia) {
        return franquiciaServicio.crearFranquicia(franquicia);
    }
}
