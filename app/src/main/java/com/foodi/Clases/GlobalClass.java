package com.foodi.Clases;

import android.app.Application;

import androidx.appcompat.app.AppCompatActivity;

public class GlobalClass extends Application {
    private String id_usuario_actual;

    public String getId_usuario_actual() {
        return id_usuario_actual;
    }

    public void setId_usuario_actual(String id_usuario_actual) {
        this.id_usuario_actual = id_usuario_actual;
    }
}
