// MascotaDto.java
package com.example.loginsignup.network.dto;

import com.google.gson.annotations.SerializedName;

public class MascotaDto {
    @SerializedName("id_mascota")
    public int id;

    @SerializedName("nombre")
    public String nombre;

    @SerializedName("tipo")
    public String tipo;

    @SerializedName("peso")
    public double peso;

    @SerializedName("especie")
    public String especie;

    @SerializedName("raza")
    public String raza;

    @SerializedName("sexo")
    public String sexo;

    @SerializedName("edad")
    public int edad;

    @SerializedName("fecha_registro")
    public long fechaRegistro;

    @SerializedName("id_dueño")
    public int idDueno;

    @SerializedName("id_enfermedad")
    public Integer idEnfermedad;
}

