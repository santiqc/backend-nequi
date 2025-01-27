package com.nequi.franquicias.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;


@Data
@Table("franquicias")
public class Franquicia {
    @Id
    private Long id;
    private String nombre;
}