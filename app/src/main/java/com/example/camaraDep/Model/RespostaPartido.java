package com.example.camaraDep.Model;

import com.google.gson.annotations.SerializedName;

public class RespostaPartido {
    @SerializedName("dados")
    private Partido partido;

    public Partido getPartido() {
        return partido;
    }
}

