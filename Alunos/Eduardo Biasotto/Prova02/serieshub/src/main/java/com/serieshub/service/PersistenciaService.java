package com.serieshub.service;
import com.serieshub.model.Usuario;

import com.google.gson.Gson;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.File;

public class PersistenciaService {
    public void salvar(Usuario usuario){ 
        try{
            Gson gson = new Gson();
            String json = gson.toJson(usuario);
            FileWriter writer = new FileWriter("dados.json");
            writer.write(json);
            writer.close();
        } catch (Exception e){
            System.out.println("Erro ao salvar:" + e.getMessage());
        }
    }
    
    public Usuario carregar(){ 
        try {
            File arquivo = new File("dados.json");
            if (!arquivo.exists()) {
                return new Usuario("Usuário");
            }
            Gson gson = new Gson();
            FileReader reader = new FileReader(arquivo);
            Usuario usuario = gson.fromJson(reader, Usuario.class);
            reader.close();
            return usuario;
        } catch (Exception e) {
            System.out.println("Erro ao carregar:" + e.getMessage());
            return new Usuario("Usuário");
        }
    }
}
