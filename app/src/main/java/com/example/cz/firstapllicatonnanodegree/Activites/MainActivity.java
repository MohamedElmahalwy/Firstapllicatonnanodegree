package com.example.cz.firstapllicatonnanodegree.Activites;

import android.database.Cursor;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
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
import com.example.cz.firstapllicatonnanodegree.Adapter.CursorAdapter;
import com.example.cz.firstapllicatonnanodegree.Adapter.Movie_adpater;
import com.example.cz.firstapllicatonnanodegree.EndlessRecyclerViewScrollListener;
import com.example.cz.firstapllicatonnanodegree.Model.Movie_model;
import com.example.cz.firstapllicatonnanodegree.R;
import com.example.cz.firstapllicatonnanodegree.Utils.Connection;
import com.example.cz.firstapllicatonnanodegree.Utils.DataContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Object> {
    String api_key = "__ApiKey__";
    final static String CATEGORY_KEY = "category_key";
    final static String FAVORITES_CATEGORY = "favorites";
    final static String POPULAR_CATEGORY = "popular";
    final static String TOP_RATED_CATEGORY = "top_rated";
    final static String CURRENT_SCROLLING_STATE = "scrolling";
    static String CURRENT_STATE = "current";
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

    private Parcelable layoutManagerSavedState;
    CursorLoader loader;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestQueue = Volley.newRequestQueue(this);
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        InitEventDriven();



        getFavoriteMovies();

        if (savedInstanceState != null) {
            String current = savedInstanceState.getString(CATEGORY_KEY);
            layoutManagerSavedState = savedInstanceState.getParcelable(CURRENT_SCROLLING_STATE);
            if (current != null) {
                if (current.equalsIgnoreCase(TOP_RATED_CATEGORY)) {
                    get_top_reated_movies(page);
                } else if (current.equalsIgnoreCase(FAVORITES_CATEGORY)) {
                    getFavoriteMovies();
                } else {
                    get_popular_movies(page);
                }
            }
        } else {
            get_popular_movies(page);
        }

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
                CURRENT_STATE = POPULAR_CATEGORY;
                tv_toolbar.setText(R.string.popular);

                movies_array_list.clear();
                movie_adpater.notifyDataSetChanged();
                get_popular_movies(page);
//                rv_movies.addOnScrollListener(new EndlessRecyclerViewScrollListener(gridLayoutManager) {
//                    @Override
//                    public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
//                        page++;
//
//                        get_popular_movies(page);
//                    }
//                });
                Toast.makeText(this, "pop", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.top_reated:
                CURRENT_STATE = TOP_RATED_CATEGORY;
                tv_toolbar.setText(R.string.top_rated);
                movies_array_list.clear();
                movie_adpater.notifyDataSetChanged();
                get_top_reated_movies(page);
//                rv_movies.addOnScrollListener(new EndlessRecyclerViewScrollListener(gridLayoutManager) {
//                    @Override
//                    public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
//                        page++;
//
//                        get_top_reated_movies(page);
//                    }
//                });
//                Toast.makeText(this, "top", Toast.LENGTH_SHORT).show();

                return true;

            case R.id.favourit:
                CURRENT_STATE = FAVORITES_CATEGORY;
                tv_toolbar.setText(R.string.favourit);
//                movies_array_list.clear();
                getFavoriteMovies();

                Toast.makeText(this, "fav", Toast.LENGTH_SHORT).show();

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
                        movie_model.setId(current_object.optInt("id"));
                        movie_model.setPosterPath("http://image.tmdb.org/t/p/w500/" + current_object.getString("poster_path"));
                        movie_model.setOverview(current_object.getString("overview"));
                        movie_model.setReleaseDate(current_object.getString("release_date"));
                        movie_model.setTitle(current_object.getString("title"));
                        movie_model.setVoteAverage(current_object.getDouble("vote_average"));
                        movies_array_list.add(movie_model);
                        Log.e("path", movies_array_list.get(i).getPosterPath());


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
                        movie_model.setId(current_object.optInt("id"));
                        movie_model.setPosterPath("http://image.tmdb.org/t/p/w500/" + current_object.getString("poster_path"));
                        movie_model.setOverview(current_object.getString("overview"));
                        movie_model.setReleaseDate(current_object.getString("release_date"));
                        movie_model.setTitle(current_object.getString("title"));
                        movie_model.setVoteAverage(current_object.getDouble("vote_average"));
                        movies_array_list.add(movie_model);
                        Log.e("path", movies_array_list.get(i).getPosterPath());


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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(CATEGORY_KEY, CURRENT_STATE);
        outState.putParcelable(CURRENT_SCROLLING_STATE, rv_movies.getLayoutManager().onSaveInstanceState());

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        savedInstanceState.getString(CATEGORY_KEY);
        layoutManagerSavedState = savedInstanceState.getParcelable(CURRENT_SCROLLING_STATE);

    }

    protected void getFavoriteMovies() {
        getSupportLoaderManager().initLoader(1, null, this);
    }


    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        Uri uri = DataContract.TableContract.CONTENT_URI_TABLE;
        loader = new CursorLoader(this, uri, null, null, null, null);
        return loader;
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        cursor = (Cursor) data;
        cursor.moveToFirst();
        CursorAdapter adapter = new CursorAdapter(this, cursor);
        rv_movies.setAdapter(adapter);
        movie_adpater.notifyDataSetChanged();
        restoreLayoutManagerPosition();


    }

    @Override
    public void onLoaderReset(Loader loader) {

    }

    private void restoreLayoutManagerPosition() {
        if (layoutManagerSavedState != null) {
            rv_movies.getLayoutManager().onRestoreInstanceState(layoutManagerSavedState);
        }
    }

}
