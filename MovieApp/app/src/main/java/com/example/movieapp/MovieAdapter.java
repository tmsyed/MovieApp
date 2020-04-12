package com.example.movieapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieAdapter extends
        RecyclerView.Adapter<MovieAdapter.MovieHolder> {
    //List to store movies
    private ArrayList<Movie> moviesList;
    private Context mContext;

    public MovieAdapter(ArrayList<Movie> moviesList, Context context) {
        this.moviesList = moviesList;
        this.mContext = context;
    }

    @Override
    public MovieAdapter.MovieHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.row, parent, false);
        return new MovieHolder(view);
    }

    @Override
    public int getItemCount() {
        return moviesList == null ? 0 : moviesList.size();
    }

    @Override
    public void onBindViewHolder(MovieAdapter.MovieHolder holder, int position) {
        final Movie movie = moviesList.get(position);

        holder.setMovieTitle(movie.getMovieTitle());
        holder.setMoviePoster(movie.getMoviePosterResource());
    }

    public class MovieHolder extends RecyclerView.ViewHolder {
        private ImageView moviePoster;
        private TextView movieTitle;


        public MovieHolder(View itemView) {
            super(itemView);

            moviePoster = itemView.findViewById(R.id.movie_poster);
            movieTitle = itemView.findViewById(R.id.movie_title);
        }

        public void setMovieTitle(String name) {
            movieTitle.setText(name);
        }

        public void setMoviePoster(String uri) {
            Picasso.get().load(uri).into(moviePoster);
        }
    }

}
