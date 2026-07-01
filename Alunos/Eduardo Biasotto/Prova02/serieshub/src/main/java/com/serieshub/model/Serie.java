package com.serieshub.model;

import java.util.List;

public class Serie {
    private String nome;
    private String idioma;
    private List<String> generos;
    private double nota;
    private String estado;
    private String dataEstreia;
    private String dataTermino;
    private String emissora;
    private String imageUrl;
    private String descricao;

    // Construtor
    public Serie(String nome, String idioma, List<String> generos,
                 double nota, String estado, String dataEstreia,
                 String dataTermino, String emissora, String imageUrl, String descricao) {
        this.nome = nome;
        this.idioma = idioma;
        this.generos = generos;
        this.nota = nota;
        this.estado = estado;
        this.dataEstreia = dataEstreia;
        this.dataTermino = dataTermino;
        this.emissora = emissora;
        this.imageUrl = imageUrl;
        this.descricao = descricao;
    }

    // Getters
    public String getNome() { return nome; }
    public String getIdioma() { return idioma; }
    public List<String> getGeneros() { return generos; }
    public double getNota() { return nota; }
    public String getEstado() { return estado; }
    public String getDataEstreia() { return dataEstreia; }
    public String getDataTermino() { return dataTermino; }
    public String getEmissora() { return emissora; }
    public String getImageUrl() { return imageUrl; }
    public String getDescricao() { return descricao; }
}