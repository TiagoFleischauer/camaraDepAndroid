package com.example.camaraDep.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Deputado {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName(value="nome", alternate={"nomeCivil"})
    @Expose
    private String nome;

    @SerializedName("siglaPartido")
    @Expose
    private String siglaPartido;

    @SerializedName("siglaUf")
    @Expose
    private String siglaUf;

    @SerializedName("ultimoStatus")
    @Expose
    private UltimoStatus ultimoStatus;

    public static class UltimoStatus {
        private String nome;
        private String siglaPartido;
        private String siglaUf;
        private String urlFoto;
        private String email;
        private String condicaoEleitoral;

        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }

        public String getSiglaPartido() {
            return siglaPartido;
        }

        public void setSiglaPartido(String siglaPartido) {
            this.siglaPartido = siglaPartido;
        }

        public String getSiglaUf() {
            return siglaUf;
        }

        public void setSiglaUf(String siglaUf) {
            this.siglaUf = siglaUf;
        }

        public String getUrlFoto() {
            return urlFoto;
        }

        public void setUrlFoto(String urlFoto) {
            this.urlFoto = urlFoto;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getCondicaoEleitoral() {
            return condicaoEleitoral;
        }

        public void setCondicaoEleitoral(String condicaoEleitoral) {
            this.condicaoEleitoral = condicaoEleitoral;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSiglaPartido() {
        return siglaPartido;
    }

    public void setSiglaPartido(String siglaPartido) {
        this.siglaPartido = siglaPartido;
    }

    public String getSiglaUf() {
        return siglaUf;
    }

    public void setSiglaUf(String siglaUf) {
        this.siglaUf = siglaUf;
    }

    public UltimoStatus getUltimoStatus() {
        return ultimoStatus;
    }

    public void setUltimoStatus(UltimoStatus ultimoStatus) {
        this.ultimoStatus = ultimoStatus;
    }
}

