package com.example.movieapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

//Description: Adapter class for RecyclerView in MainActivity
//Follows standard best practice for creating RecyclerView Adapters
public class MovieAdapter extends
        RecyclerView.Adapter<MovieAdapter.MovieHolder> {
    //List to store movies
    private ArrayList<Movie> moviesList;
    private Context mContext;  //Activity context referencing which activity this is bound to

    public MovieAdapter(ArrayList<Movie> moviesList, Context context) {
        this.moviesList = moviesList;
        this.mContext = context;
    }

    //Creating the View
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

    //Binding the data with the View
    @Override
    public void onBindViewHolder(MovieAdapter.MovieHolder holder, int position) {
        final Movie movie = moviesList.get(position);

        holder.setMovieTitle(movie.getMovieTitle());
        holder.setMoviePoster(movie.getMoviePosterResource());
    }

    //Description: Inner class View Holder for the adapter
    //View based on row.xml file for each row symbolising a RecyclerView entry
    public class MovieHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        private ImageView moviePoster;
        private TextView movieTitle;

        //Used to pass information to second activity via an intent
        public static final String EXTRA_MESSAGE = "com.example.movieapp.extra.MESSAGE";


        public MovieHolder(View itemView) {
            super(itemView);

            moviePoster = itemView.findViewById(R.id.movie_poster);
            movieTitle = itemView.findViewById(R.id.movie_title);

            itemView.setOnClickListener(this);
        }

        public void setMovieTitle(String name) {
            movieTitle.setText(name);
        }

        //Using Picasso Library, the ImageView resource is set to an online image
        //URI is provided by OMDb API
        public void setMoviePoster(String uri) {
            Picasso.get().load(uri).into(moviePoster);
        }

        //When user clicks on an entry, a second activity with that movie's specific
        //details are shown
        @Override
        public void onClick(View v) {
            int position = getLayoutPosition();
            Movie movie = moviesList.get(position);  //get selected movie
            Context context = v.getContext();  //Activity context
            Intent intent = new Intent(context, MovieDetailsActivity.class);
            intent.putExtra(EXTRA_MESSAGE, movie.getJsonObject());  //parameter for activity
            context.startActivity(intent);  //starting next activity MovieDetailsActivity
        }
    }

}
