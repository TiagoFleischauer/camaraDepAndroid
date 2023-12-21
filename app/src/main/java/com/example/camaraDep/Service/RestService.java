package com.example.camaraDep.Service;

import com.example.camaraDep.Model.RespostaDeputado;
import com.example.camaraDep.Model.RespostaDeputados;
import com.example.camaraDep.Model.RespostaPartido;
import com.example.camaraDep.Model.RespostaPartidos;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RestService {
    @GET("partidos")
    Call<RespostaPartidos> listarPartidos();

    @GET("partidos/{id}/")
    Call<RespostaPartido> listarPartidoPorId(@Path("id") int id);

    @GET("deputados")
    Call<RespostaDeputados> listarDeputados();

    @GET("deputados/{id}/")
    Call<RespostaDeputado> listarDeputadoPorId(@Path("id") int id);

}