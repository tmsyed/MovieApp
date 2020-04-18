package com.example.movieapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

//Description: Second Activity that displays the specific details for the movie selected
public class MovieDetailsActivity extends AppCompatActivity {
    private TextView headerText; //Movie title serving as page header
    private String jsonString;  //The json object sent in from previous activity
    private RequestQueue requestQueue;  //Volley request queue
    private String imdbID;  //This ID from jsonString will be used to access the API for
    //the specific details for the chosen movie

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        //Setting up initial view and getting intent data
        headerText = findViewById(R.id.header);
        Intent intent = getIntent();
        jsonString = intent.getStringExtra(MovieAdapter.MovieHolder.EXTRA_MESSAGE);

        //Destringifying the json object to extract the JSON data within
        JSONObject jsonObject;
        try {
            if (!jsonString.equals("")) {
                jsonObject = new JSONObject(jsonString);
                headerText.setText(jsonObject.toString());

                imdbID = jsonObject.getString("imdbID");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        requestQueue = Volley.newRequestQueue(this);

        //Seperate thread used to query the API for movie details
        Thread thread = new Thread(new VolleyRequestThread());
        thread.start();
    }

    class VolleyRequestThread implements Runnable {

        //The eight view elements used for portraying the movie details
        private TextView titleText;
        private TextView runtimeText;
        private TextView ratedText;
        private TextView genreText;
        private TextView actorsText;
        private TextView imdbRatingText;
        private ImageView moviePoster;
        private TextView overviewText;

        //This method is to bind the view elements with the class fields
        public void GetReferencesForViews() {
            titleText = findViewById(R.id.header);
            runtimeText = findViewById(R.id.runtime);
            ratedText = findViewById(R.id.rated);
            genreText = findViewById(R.id.genre);
            actorsText = findViewById(R.id.actors);
            imdbRatingText = findViewById(R.id.imdb_score);
            moviePoster = findViewById(R.id.poster);
            overviewText = findViewById(R.id.overview);
        }

        public void run() {
            GetReferencesForViews();

            //Actual request handled by Volley
            try {
                JsonObjectRequest jsonObjectRequest =
                        new JsonObjectRequest(
                                Request.Method.GET,
                                "https://www.omdbapi.com/?apikey=ccc94984&i=" + imdbID,
                                null,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        Log.i("JSON Details response", response.toString());
                                        try {
                                            //Parsing JSON response for relevant data
                                            String title = response.getString("Title") +
                                                    " - " + response.getString("Year");
                                            String runtime = response.getString("Runtime");
                                            String rated = response.getString("Rated");
                                            String genre = response.getString("Genre");
                                            String actors = response.getString("Actors");
                                            String imdbRating = response.getString("imdbRating");
                                            String poster = response.getString("Poster");
                                            String plot = response.getString("Plot");

                                            //Setting the data in the view elements
                                            titleText.setText(title);
                                            runtimeText.append(runtime);
                                            ratedText.append(rated);
                                            genreText.append(genre);
                                            actorsText.append("\n" + actors);
                                            imdbRatingText.append(imdbRating);
                                            overviewText.append("\n" + plot);

                                            //Picasso Library used as a way to set ImageView resource
                                            //to an online image
                                            if(poster.equals("N/A")){
                                                moviePoster.setImageResource(R.drawable.not_avail);
                                            }else{
                                                Picasso.get().load(poster).into(moviePoster);
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.e("Request Error", error.toString());
                                    }
                                }
                        );
                requestQueue.add(jsonObjectRequest);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
