// UsuarioDto.java
package com.example.loginsignup.network.dto;

import com.google.gson.annotations.SerializedName;

public class UsuarioDto {
    @SerializedName("id")
    public Long id;

    @SerializedName("nombre")
    public String nombre;

    @SerializedName("correo")
    public String correo;
}
