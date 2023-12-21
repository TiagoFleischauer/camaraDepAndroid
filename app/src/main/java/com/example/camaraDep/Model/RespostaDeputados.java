package com.example.camaraDep.Model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class RespostaDeputados {
    @SerializedName("dados")
    private List<Deputado> deputados;

    public List<Deputado> getDeputados() {
        return deputados;
    }
}

