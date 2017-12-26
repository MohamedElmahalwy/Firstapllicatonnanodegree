package com.example.cz.firstapllicatonnanodegree.Activites;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.cz.firstapllicatonnanodegree.Adapter.Movie_adpater;
import com.example.cz.firstapllicatonnanodegree.EndlessRecyclerViewScrollListener;
import com.example.cz.firstapllicatonnanodegree.Model.Movie_model;
import com.example.cz.firstapllicatonnanodegree.R;
import com.example.cz.firstapllicatonnanodegree.Utils.Connection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    String api_key = "aaa42517df175590be081931c26affa8";
    RequestQueue requestQueue;
    RecyclerView rv_movies;
    ArrayList<Movie_model> movies_array_list;
    Movie_adpater movie_adpater;
    GridLayoutManager gridLayoutManager;
    LinearLayoutManager linearLayoutManager;
    int page = 1;
    @BindView(R.id.tv_toolbar)
    TextView tv_toolbar;
    @BindView(R.id.iv_left_arrow)
    ImageView iv_left_arrow;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestQueue = Volley.newRequestQueue(this);
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        InitEventDriven();
        get_popular_movies(page);

    }
//
    private void InitEventDriven() {
        tv_toolbar.setText(R.string.popular);
        iv_left_arrow.setVisibility(View.GONE);
        rv_movies = (RecyclerView) findViewById(R.id.rv_movies);
        gridLayoutManager = new GridLayoutManager(this, 2);
        rv_movies.setLayoutManager(gridLayoutManager);

        movies_array_list = new ArrayList<>();
        movie_adpater = new Movie_adpater(this, movies_array_list);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        id = item.getItemId();

        switch (item.getItemId()) {
            case R.id.popular:
                tv_toolbar.setText(R.string.popular);

                movies_array_list.clear();
                movie_adpater.notifyDataSetChanged();
                get_popular_movies(page);
                rv_movies.addOnScrollListener(new EndlessRecyclerViewScrollListener(gridLayoutManager) {
                    @Override
                    public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                        page++;

                        get_popular_movies(page);
                    }
                });
                Toast.makeText(this, "pop", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.top_reated:
                tv_toolbar.setText(R.string.top_rated);
                movies_array_list.clear();
                movie_adpater.notifyDataSetChanged();
                get_top_reated_movies(page);
                rv_movies.addOnScrollListener(new EndlessRecyclerViewScrollListener(gridLayoutManager) {
                    @Override
                    public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                        page++;

                        get_popular_movies(page);
                    }
                });
                Toast.makeText(this, "top", Toast.LENGTH_SHORT).show();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    void get_popular_movies(int page) {
        String url = Connection.base_url + "popular?api_key=" + api_key + "&&page=" + page;
        StringRequest request_get_popular_movies = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.e("rsponse", response);
                    JSONObject object_response = new JSONObject(response);
                    JSONArray results = object_response.getJSONArray("results");
                    for (int i = 0; i < results.length(); i++) {
                        Movie_model movie_model = new Movie_model();
                        JSONObject current_object = results.getJSONObject(i);
                        movie_model.setPoster(current_object.getString("poster_path"));
                        movie_model.setOverview(current_object.getString("overview"));
                        movie_model.setRelease_date(current_object.getString("release_date"));
                        movie_model.setTitle(current_object.getString("title"));
                        movie_model.setVote_average(current_object.getString("vote_average"));
                        movies_array_list.add(movie_model);
                        Log.e("path", movies_array_list.get(i).getPoster());


                    }
                    Log.e("sizzze", movies_array_list.size() + "");
                    rv_movies.setAdapter(movie_adpater);
                    movie_adpater.notifyItemRangeInserted(movie_adpater.getItemCount(), movies_array_list.size() - 1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null) {
                    Log.e("Volley", "Error. HTTP Status Code:" + networkResponse.statusCode);
                }
                if (error instanceof TimeoutError) {
                    Log.e("Volley", "TimeoutError");
                } else if (error instanceof NoConnectionError) {
                    Log.e("Volley", "NoConnectionError");
                } else if (error instanceof AuthFailureError) {
                    Log.e("Volley", "AuthFailureError");
                } else if (error instanceof ServerError) {
                    Log.e("Volley", "ServerError");
                } else if (error instanceof NetworkError) {
                    Log.e("Volley", "NetworkError");
                } else if (error instanceof ParseError) {
                    Log.e("Volley", "ParseError");
                }
                Toast.makeText(getApplicationContext(), "Something error...!", Toast.LENGTH_SHORT).show();
                Log.e("Response is: ", "That didn't work!");
            }
        });
        requestQueue.add(request_get_popular_movies);
    }

    void get_top_reated_movies(int page) {
        String url = Connection.base_url + "top_rated?api_key=" + api_key + "&&page=" + page;
        StringRequest request_get_top_reated_movies = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object_response = new JSONObject(response);
                    JSONArray results = object_response.getJSONArray("results");
                    for (int i = 0; i < results.length(); i++) {
                        Movie_model movie_model = new Movie_model();
                        JSONObject current_object = results.getJSONObject(i);
                        movie_model.setPoster(current_object.getString("poster_path"));
                        movie_model.setOverview(current_object.getString("overview"));
                        movie_model.setRelease_date(current_object.getString("release_date"));
                        movie_model.setTitle(current_object.getString("title"));
                        movie_model.setVote_average(current_object.getString("vote_average"));
                        movies_array_list.add(movie_model);
                        Log.e("path", movies_array_list.get(i).getPoster());


                    }
                    Log.e("sizzze", movies_array_list.size() + "");
                    rv_movies.setAdapter(movie_adpater);
                    movie_adpater.notifyItemRangeInserted(movie_adpater.getItemCount(), movies_array_list.size() - 1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null) {
                    Log.e("Volley", "Error. HTTP Status Code:" + networkResponse.statusCode);
                }
                if (error instanceof TimeoutError) {
                    Log.e("Volley", "TimeoutError");
                } else if (error instanceof NoConnectionError) {
                    Log.e("Volley", "NoConnectionError");
                } else if (error instanceof AuthFailureError) {
                    Log.e("Volley", "AuthFailureError");
                } else if (error instanceof ServerError) {
                    Log.e("Volley", "ServerError");
                } else if (error instanceof NetworkError) {
                    Log.e("Volley", "NetworkError");
                } else if (error instanceof ParseError) {
                    Log.e("Volley", "ParseError");
                }
                Toast.makeText(getApplicationContext(), "Something error...!", Toast.LENGTH_SHORT).show();
                Log.e("Response is: ", "That didn't work!");
            }
        });
        requestQueue.add(request_get_top_reated_movies);
    }
}
