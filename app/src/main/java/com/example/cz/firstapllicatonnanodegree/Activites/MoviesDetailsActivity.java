package com.example.cz.firstapllicatonnanodegree.Activites;

import android.content.ContentValues;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.example.cz.firstapllicatonnanodegree.Adapter.Review_adapter;
import com.example.cz.firstapllicatonnanodegree.Adapter.Tailer_adapter;
import com.example.cz.firstapllicatonnanodegree.Model.Movie_model;
import com.example.cz.firstapllicatonnanodegree.Model.Review_model;
import com.example.cz.firstapllicatonnanodegree.Model.Tailer_model;
import com.example.cz.firstapllicatonnanodegree.R;
import com.example.cz.firstapllicatonnanodegree.Utils.Connection;
import com.example.cz.firstapllicatonnanodegree.Utils.DataContract;
import com.example.cz.firstapllicatonnanodegree.Utils.DatabaseHelper;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MoviesDetailsActivity extends AppCompatActivity {
    @BindView(R.id.tv_movie_title_header)
    TextView tv_movie_title_header;
    @BindView(R.id.tv_rlase_date)
    TextView tv_relase_date;

    @BindView(R.id.tv_vote_average)
    TextView tv_vote_average;
    @BindView(R.id.tv_overview)
    TextView tv_overview;
    @BindView(R.id.iv_poster)
    ImageView iv_poster;
    @BindView(R.id.tv_toolbar)
    TextView tv_toolbar;
    @BindView(R.id.iv_left_arrow)
    ImageView iv_left_arrow;
    @BindView(R.id.btn_add_to_favourit)
    Button btn_add_to_favourit;
    @BindView(R.id.tv_trailers)
    TextView tv_tailer;
    @BindView(R.id.tv_reviews)
    TextView tv_reviews;
    @BindView(R.id.tv_author)
    TextView tv_author;
    @BindView(R.id.tv_content)
    TextView tv_content;
    @BindView(R.id.linear_reviews)
    LinearLayout lr_reviwes;
    String VIDEOS = "/videos";
    String REVIEWS = "/reviews";
    String api_key = "__ApiKey___";
    RequestQueue requestQueue;
    RecyclerView rv_tailers, rv_reviews;
    ArrayList<Tailer_model> tailers_array_list;
    ArrayList<Review_model> reviews_array_list;
    Tailer_adapter tailer_adapter;
    Review_adapter review_adapter;
    LinearLayoutManager linearLayoutManager_1;
    LinearLayoutManager linearLayoutManager_2;
    Movie_model movie_model;
    DatabaseHelper dbHelper;
    Intent comingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_details);
        ButterKnife.bind(this);
        tv_toolbar.setText(R.string.movie_details);
        requestQueue = Volley.newRequestQueue(this);
        dbHelper = new DatabaseHelper(this);
        InitEventDriven();
        comingIntent = getIntent();
        if (getIntent() != null) {
            String extra = comingIntent.getStringExtra("intent");
            if (extra.equalsIgnoreCase("movie")) {
                movie_model = comingIntent.getParcelableExtra("movie");
                set_values(movie_model);
            } else if (extra.equalsIgnoreCase("cursor")) {
                String title = comingIntent.getStringExtra("title");
                String vote = comingIntent.getStringExtra("vote");
                String overview = comingIntent.getStringExtra("overview");
                String poster = comingIntent.getStringExtra("poster");
                btn_add_to_favourit.setVisibility(View.GONE);
                cursorData(title, vote, overview, poster);
            }
        }

    }

    void InitEventDriven() {
        rv_tailers = (RecyclerView) findViewById(R.id.rv_trailers);
        rv_reviews = (RecyclerView) findViewById(R.id.rv_reviews);
        tailers_array_list = new ArrayList<>();
        reviews_array_list = new ArrayList<>();
        tailer_adapter = new Tailer_adapter(this, tailers_array_list);
        review_adapter = new Review_adapter(this, reviews_array_list);
        linearLayoutManager_1 = new LinearLayoutManager(this, LinearLayout.VERTICAL, false);
        linearLayoutManager_2 = new LinearLayoutManager(this, LinearLayout.VERTICAL, false);
        rv_tailers.setLayoutManager(linearLayoutManager_1);
        rv_reviews.setLayoutManager(linearLayoutManager_2);

        btn_add_to_favourit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addMovieToFavorite();
            }
        });
    }

    void set_values(Movie_model movie_model) {

        iv_left_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tv_movie_title_header.setText(movie_model.getTitle());
        tv_relase_date.setText(movie_model.getReleaseDate());
        tv_vote_average.setText(movie_model.getVoteAverage() + "/10");
        tv_overview.setText(movie_model.getOverview());
        Picasso.with(this).
                load( movie_model.getPosterPath()).
                into(iv_poster);

        get_reviews(movie_model.getId());
        get_tailers(movie_model.getId());
    }

    void get_tailers(int id) {
        String url = Connection.base_url + id + VIDEOS + "?api_key=" + api_key;
        StringRequest request_get_tailers = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray results = object.optJSONArray("results");
                    for (int i = 0; i < results.length(); i++) {
                        Tailer_model tailer_model = new Tailer_model();
                        JSONObject current_object = results.optJSONObject(i);
                        tailer_model.setKey(current_object.optString("key"));
                        tailer_model.setName(current_object.optString("name"));
                        tailers_array_list.add(tailer_model);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (tailers_array_list.size() == 0) {
                    tv_tailer.setText(getString(R.string.no_trailers));
                    rv_tailers.setVisibility(View.GONE);
                }

                rv_tailers.setAdapter(tailer_adapter);
                tailer_adapter.notifyDataSetChanged();
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
        requestQueue.add(request_get_tailers);
    }

    void get_reviews(int id) {
        String url = Connection.base_url + id + REVIEWS + "?api_key=" + api_key;
        StringRequest request_get_reviwes = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray results = object.optJSONArray("results");
                    for (int i = 0; i < results.length(); i++) {
                        Review_model review_model = new Review_model();
                        JSONObject current_object = results.optJSONObject(i);
                        review_model.setAuthor(current_object.optString("author"));
                        review_model.setContent(current_object.optString("content"));
                        reviews_array_list.add(review_model);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (reviews_array_list.size() == 0) {
                    tv_reviews.setText(getString(R.string.no_reviews));
                    lr_reviwes.setVisibility(View.GONE);
                }

                rv_reviews.setAdapter(review_adapter);
                review_adapter.notifyDataSetChanged();
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
        requestQueue.add(request_get_reviwes);

    }

    public void addMovieToFavorite() {
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(DataContract.TableContract.MOVIE_ID, movie_model.getId());
            contentValues.put(DataContract.TableContract.MOVIE_TITLE, movie_model.getTitle());
            contentValues.put(DataContract.TableContract.MOVIE_POSTER, movie_model.getPosterPath());
            contentValues.put(DataContract.TableContract.MOVIE_OVERVIEW, movie_model.getOverview());
            contentValues.put(DataContract.TableContract.MOVIE_VOTE, movie_model.getVoteAverage());
            this.getContentResolver().insert(DataContract.TableContract.CONTENT_URI_TABLE, contentValues);
            Log.e("content", contentValues.toString());
            Toast.makeText(this, getString(R.string.movie_add_to_fav), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cursorData(String title, String vote, String overview, String poster) {

        rv_tailers.setVisibility(View.GONE);
        tv_tailer.setVisibility(View.GONE);
        rv_reviews.setVisibility(View.GONE);
        tv_reviews.setVisibility(View.GONE);
        tv_content.setVisibility(View.GONE);
        tv_author.setVisibility(View.GONE);
        try {
            tv_movie_title_header.setText(title);
            tv_overview.setText(overview);
            tv_vote_average.setText(vote);
            Picasso.with(this)
                    .load(poster)
                    .error(R.drawable.ic_launcher_foreground)
                    .into(iv_poster);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
    }
}
