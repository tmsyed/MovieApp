package com.example.movieapp;

import android.widget.ImageView;
import android.widget.TextView;

public class Movie {
    private String moviePosterResource;
    private String movieTitle;

    public Movie(String moviePosterResource, String movieTitle) {
        this.moviePosterResource = moviePosterResource;
        this.movieTitle = movieTitle;
    }


    public String getMoviePosterResource() {
        return moviePosterResource;
    }

    public String getMovieTitle() {
        return movieTitle;
    }
}
