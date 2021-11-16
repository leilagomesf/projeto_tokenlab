package com.example.projetotokenlabjava;

import java.util.ArrayList;

//classe baseada na resposta do https request para facilitar no parsing da resposta da lista de filmes
public class Movie {
    private int id;
    private float vote_average;
    private String title;
    private String poster_url;
    private ArrayList<String> genres;
    private String release_date;

    public Movie(int id, float vote_average, String title, String poster_url, ArrayList<String> genres, String release_date) {
        this.id = id;
        this.vote_average = vote_average;
        this.title = title;
        this.poster_url = poster_url;
        this.genres = genres;
        this.release_date = release_date;
    }

    @Override
    public String toString() {
        return "SimplifiedMovie{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", poster_url='" + poster_url + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public float getVote_average() {
        return vote_average;
    }

    public void setVote_average(float vote_average) {
        this.vote_average = vote_average;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPoster_url() {
        return poster_url;
    }

    public void setPoster_url(String poster_url) {
        this.poster_url = poster_url;
    }

    public ArrayList<String> getGenres() {
        return genres;
    }

    public void setGenres(ArrayList<String> genres) {
        this.genres = genres;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

}
