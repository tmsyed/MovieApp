package com.example.movieapp;

import android.widget.ImageView;
import android.widget.TextView;

//Description: Each movie returned from the OMDb API has the important info stored here as the core
//data object supplied to the RecyclerView's adapter class
public class Movie {
    private String moviePosterResource; //URI for movie poster
    private String movieTitle;
    private String jsonObject;  //JSON data for specific movie

    public Movie(String moviePosterResource, String movieTitle, String jsonObject) {
        this.moviePosterResource = moviePosterResource;
        this.movieTitle = movieTitle;
        this.jsonObject = jsonObject;
    }


    public String getMoviePosterResource() {
        return moviePosterResource;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public String getJsonObject() {
        return jsonObject;
    }
}
