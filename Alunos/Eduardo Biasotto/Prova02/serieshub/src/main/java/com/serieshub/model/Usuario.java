package com.serieshub.model;

import java.util.List;
import java.util.ArrayList;

public class Usuario{
    private String nome;
    private List <Serie> seriesFavoritas;
    private List <Serie> seriesAssistidas;
    private List <Serie> seriesParaAssistir;

    public Usuario(String nome) {
        this.nome = nome;
        this.seriesFavoritas = new ArrayList<>();
        this.seriesAssistidas = new ArrayList<>();
        this.seriesParaAssistir = new ArrayList<>();
    }

    // Getters
    public String getNome() {
        return nome;
    }

    public List<Serie> getSeriesFavoritas() {
        return seriesFavoritas;
    }

    public List<Serie> getSeriesAssistidas() {
        return seriesAssistidas;
    }

    public List<Serie> getSeriesParaAssistir() {
        return seriesParaAssistir;
    }

    // Remove & Add methods 

    public void adicionarFavorita(Serie serie) { 
        seriesFavoritas.add(serie); 
    }

    public void removerFavorita(Serie serie) { 
        seriesFavoritas.remove(serie); 
    }

    public void adicionarAssistida(Serie serie) { 
        seriesAssistidas.add(serie); 
    }

    public void removerAssistida(Serie serie) { 
        seriesAssistidas.remove(serie); 
    }

    public void adicionarParaAssistir(Serie serie) { 
        seriesParaAssistir.add(serie); 
    }

    public void removerParaAssistir(Serie serie) { 
        seriesParaAssistir.remove(serie); 
    }
}