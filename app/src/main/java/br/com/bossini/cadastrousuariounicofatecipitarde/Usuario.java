package br.com.bossini.cadastrousuariounicofatecipitarde;

import android.graphics.Bitmap;

import com.google.firebase.database.Exclude;

/**
 * Created by rodrigo on 10/6/17.
 */

public class Usuario {
    private String nome, fone, email;
    private Bitmap foto;

    private static Usuario instance;

    @Exclude
    public static Usuario getInstance (){
        /*if (instance == null){
            instance = new Usuario ();
            return instance;
        }
        return instance;*/
        return instance == null ? instance = new Usuario () : instance;
    }

    private Usuario (){

    }
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getFone() {
        return fone;
    }

    public void setFone(String fone) {
        this.fone = fone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Exclude
    public Bitmap getFoto() {
        return foto;
    }

    public void setFoto(Bitmap foto) {
        this.foto = foto;
    }
}
