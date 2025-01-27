package com.nequi.franquicias.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class NombreNuevoDto {
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

}