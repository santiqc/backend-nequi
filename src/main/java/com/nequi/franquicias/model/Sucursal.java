package com.nequi.franquicias.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("sucursales")
public class Sucursal {
    @Id
    private Long id;
    private String nombre;
    private Long franquiciaId;
}