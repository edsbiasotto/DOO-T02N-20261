package com.serieshub.service;

import java.util.List;
import java.util.ArrayList;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import com.serieshub.model.Serie;

public class TVMazeService {
    public List<Serie> buscarSeries(String nome) {
        List<Serie> series = new ArrayList<>();
        String url = "https://api.tvmaze.com/search/shows?q=" + nome.replace(" ", "+");

        try{
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            JsonArray array = JsonParser.parseString(response.body()).getAsJsonArray();

            for (int i = 0; i < array.size(); i++){
                JsonObject show = array.get(i).getAsJsonObject().getAsJsonObject("show");

                String nomeSerie = show.get("name").getAsString();
                String idioma = show.has("language") && !show.get("language").isJsonNull() ? show.get("language").getAsString() : "N/A";
                String estado = show.has("status") && !show.get("status").isJsonNull() ? show.get("status").getAsString() : "N/A";
                String dataEstreia = show.has("premiered") && !show.get("premiered").isJsonNull() ? show.get("premiered").getAsString() : "N/A";
                String dataTermino = show.has("ended") && !show.get("ended").isJsonNull() ? show.get("ended").getAsString() : "N/A";
                String emissora = show.has("network") && !show.get("network").isJsonNull() ? show.getAsJsonObject("network").get("name").getAsString() : "N/A";
                double nota = show.has("rating") && !show.getAsJsonObject("rating").get("average").isJsonNull() ? show.getAsJsonObject("rating").get("average").getAsDouble() : 0.0;
                String imageUrl = show.has("image") && !show.get("image").isJsonNull() ? show.getAsJsonObject("image").get("medium").getAsString() : "";
                String descricao = show.has("summary") && !show.get("summary").isJsonNull() ? show.get("summary").getAsString().replaceAll("<[^>]*>", "") : "Sem descricao disponivel.";
                List<String> generos = new ArrayList<>();
                if (show.has("genres")){
                    show.getAsJsonArray("genres").forEach(g -> generos.add(g.getAsString()));
                }

                series.add(new Serie(nomeSerie, idioma, generos, nota, estado, dataEstreia, dataTermino, emissora, imageUrl, descricao));
            }

        } catch (Exception e) {
            System.out.println("Erro ao buscar séries: " + e.getMessage());
        }

        return series;
    }
}