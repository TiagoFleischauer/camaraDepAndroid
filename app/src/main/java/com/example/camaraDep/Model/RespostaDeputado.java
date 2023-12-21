package com.example.camaraDep.Model;

import com.google.gson.annotations.SerializedName;

public class RespostaDeputado {
    @SerializedName("dados")
    private Deputado deputado;

    public Deputado getDeputado() {
        return deputado;
    }
}
