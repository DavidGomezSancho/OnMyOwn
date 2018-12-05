package com.tfg.david.appconversacional;

/**
 * Created by david on 29/03/2018.
 */

public class Mensaje {
    private String contenido;
    private String usuario;

    public Mensaje(){}

    public Mensaje(String contenido, String usuario){
        this.contenido=contenido;
        this.usuario=usuario;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
}
