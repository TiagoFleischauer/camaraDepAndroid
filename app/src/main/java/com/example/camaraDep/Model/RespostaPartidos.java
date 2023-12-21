package com.example.camaraDep.Model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class RespostaPartidos {
    @SerializedName("dados")
    private List<Partido> partidos;

    public List<Partido> getPartidos() {
        return partidos;
    }
}

