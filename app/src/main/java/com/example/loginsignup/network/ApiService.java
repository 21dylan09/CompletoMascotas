package com.example.loginsignup.network;

import com.example.loginsignup.network.dto.UsuarioDto;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.DELETE;
import retrofit2.http.Path;

public interface ApiService {
    @GET("usuarios")
    Call<List<UsuarioDto>> getUsuarios();

    @GET("usuarios/{id}")
    Call<UsuarioDto> getUsuarioById(@Path("id") Long id);

    @GET("mascota/{id}")
    Call<UsuarioDto> getMascotaPorDueno(@Path("id") Long id);
    @POST("usuarios")
    Call<UsuarioDto> crearUsuario(@Body UsuarioDto usuario);

    @PUT("usuarios/{id}")
    Call<UsuarioDto> actualizarUsuario(@Path("id") Long id, @Body UsuarioDto usuario);

    @DELETE("usuarios/{id}")
    Call<Void> eliminarUsuario(@Path("id") Long id);
}
