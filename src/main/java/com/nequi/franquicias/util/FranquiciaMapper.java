package com.nequi.franquicias.util;

import com.nequi.franquicias.dto.FranquiciaDTO;
import com.nequi.franquicias.dto.ProductoDTO;
import com.nequi.franquicias.dto.SucursalDTO;
import com.nequi.franquicias.model.Franquicia;
import com.nequi.franquicias.model.Producto;
import com.nequi.franquicias.model.Sucursal;
import org.springframework.stereotype.Component;

@Component
public class FranquiciaMapper {

    public Franquicia toEntity(FranquiciaDTO dto) {
        Franquicia franquicia = new Franquicia();
        franquicia.setNombre(dto.getNombre());
        return franquicia;
    }

    public Sucursal toEntity(SucursalDTO dto, Long franquiciaId) {
        Sucursal sucursal = new Sucursal();
        sucursal.setNombre(dto.getNombre());
        sucursal.setFranquiciaId(franquiciaId);
        return sucursal;
    }

    public Producto toEntity(ProductoDTO dto, Long sucursalId) {
        Producto producto = new Producto();
        producto.setNombre(dto.getNombre());
        producto.setStock(dto.getStock());
        producto.setSucursalId(sucursalId);
        return producto;
    }
}