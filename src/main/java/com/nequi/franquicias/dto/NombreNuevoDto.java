package com.nequi.franquicias.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NombreNuevoDto {
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

}