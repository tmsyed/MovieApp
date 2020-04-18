package com.example.movieapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

//Description: Handles first screen where user can search for a movie and select from
//the various options shown
public class MainActivity extends AppCompatActivity {

    //Shared Preference file info
    private SharedPreferences mPreferences;
    private String sharedPrefFile = "com.example.movieapp";
    private final String TITLE_KEY = "title"; //used to restore last searched term when view is
    //destroyed

    private String movieTitle;  //Used as parameter in web request to search for movies

    private RequestQueue requestQueue;  //Volley request queue
    private EditText searchBar; //user enters movie name here
    private Button searchButton;

    private MovieAdapter movieAdapter;  //adapter for movie RecyclerView
    private ArrayList<Movie> moviesList = new ArrayList<>();  //stores the Movie objects
    private RecyclerView recyclerView;

    //Description: RecyclerView set up here
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);

        searchBar = findViewById(R.id.movie_search);
        searchButton = findViewById(R.id.search_button);

        //Restoring search term from shared preferences file
        movieTitle = mPreferences.getString(TITLE_KEY, "");
        searchBar.setText(movieTitle);

        //Setting up the actual RecyclerView
        recyclerView = findViewById(R.id.movie_list);
        movieAdapter = new MovieAdapter(moviesList, this);
        recyclerView.setAdapter(movieAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        //Used as a divider between entries in the RecyclerView
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                recyclerView.getContext(), linearLayoutManager.getOrientation()
        );
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.divider_dec));
        recyclerView.addItemDecoration(dividerItemDecoration);

        //initializing request queue
        requestQueue = Volley.newRequestQueue(this);
    }

    //Used for saving search term to shared preferences file
    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(TITLE_KEY, movieTitle);
        editor.apply();
    }

    //onClick handler for the search button
    public void SearchMovies(View view) {
        movieTitle = searchBar.getText().toString();

        //So long as user actually typed in a search term, request movies from API
        if (!movieTitle.matches("")) {
            moviesList.clear();
            //Web request handled through separate thread
            Thread thread = new Thread(new VolleyRequestThread());
            thread.start();
        } else {
            Toast.makeText(this, "Please enter a movie name", Toast.LENGTH_SHORT).show();
        }

    }

    //Description: Thread that handles the Volley Request to the OMDb API
    class VolleyRequestThread implements Runnable {
        public void run() {
            try {
                JsonObjectRequest jsonObjectRequest =
                        new JsonObjectRequest(
                                Request.Method.GET,
                                "https://www.omdbapi.com/?apikey=ccc94984&s=" + movieTitle,
                                null,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        Log.i("JSON response", response.toString());
                                        try {
                                            JSONArray jsonArray = response.getJSONArray("Search");
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                //The key data here is the movie title and poster
                                                //image which will be displayed
                                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                                String tempURI = jsonObject.getString("Poster");
                                                String tempTitle = jsonObject.getString("Title");

                                                //Movie object stores relevant data for each movie
                                                //The json object for the movie is passed along as well
                                                //as it will be useful in the second activity
                                                moviesList.add(new Movie(tempURI, tempTitle,
                                                        jsonObject.toString()));
                                            }
                                            movieAdapter.notifyDataSetChanged();
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
