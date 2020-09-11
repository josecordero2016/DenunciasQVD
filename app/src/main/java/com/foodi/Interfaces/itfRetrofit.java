package com.foodi.Interfaces;

import com.foodi.Modelos.DetalleDenuncia;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface itfRetrofit {

    @GET("Denunciasqvd_srv/webresources/rest.detalledenuncia")
    Call<List<DetalleDenuncia>> getDetalleDenuncia();
}
