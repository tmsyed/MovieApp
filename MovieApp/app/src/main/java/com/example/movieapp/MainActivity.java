package com.example.movieapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
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

public class MainActivity extends AppCompatActivity {

    private String movieTitle;
    private RequestQueue requestQueue;
    private EditText searchBar;
    private JSONArray resultList; //search results

    private MovieAdapter movieAdapter;
    private ArrayList<Movie> moviesList = new ArrayList<>();
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchBar = findViewById(R.id.movie_search);
        recyclerView = findViewById(R.id.movie_list);
        movieAdapter = new MovieAdapter(moviesList, this);
        recyclerView.setAdapter(movieAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                recyclerView.getContext(), linearLayoutManager.getOrientation()
        );
        recyclerView.addItemDecoration(dividerItemDecoration);

        movieTitle = "Spider-man";
        requestQueue = Volley.newRequestQueue(this);
    }

    public void SearchMovies(View view) {
        movieTitle = searchBar.getText().toString();
        if(!movieTitle.matches("")) {
            JsonObjectRequest jsonObjectRequest =
                    new JsonObjectRequest(
                            Request.Method.GET,
                            "https://www.omdbapi.com/?apikey=ccc94984&s=" + movieTitle,
                            null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.i("JSON response", response.toString());
                                    try
                                    {
                                        JSONArray jsonArray = response.getJSONArray("Search");
                                        for(int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                                            String tempURI = jsonObject.getString("Poster");
                                            String tempTitle = jsonObject.getString("Title");

                                            moviesList.add(new Movie(tempURI, tempTitle));
                                        }
                                        movieAdapter.notifyDataSetChanged();
                                    }
                                    catch (JSONException e) {
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
        }
        else {
            Toast.makeText(this, "Please enter a movie name", Toast.LENGTH_SHORT).show();
        }

    }
}
