package com.example.cz.firstapllicatonnanodegree.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.cz.firstapllicatonnanodegree.Activites.MoviesDetailsActivity;
import com.example.cz.firstapllicatonnanodegree.Model.Movie_model;
import com.example.cz.firstapllicatonnanodegree.R;
import com.example.cz.firstapllicatonnanodegree.Utils.Connection;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cz on 25/12/2017.
 */

public class Movie_adpater extends RecyclerView.Adapter<Movie_adpater.ViewHolder> {
    ArrayList<Movie_model> movies_array_list = new ArrayList<>();
    Context context;
    LayoutInflater layoutInflater;

    public Movie_adpater(Context context, ArrayList<Movie_model> movies_array_list) {
        this.movies_array_list = movies_array_list;
        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = layoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Picasso.with(context).
                load( movies_array_list.get(position).getPosterPath()).
                into(holder.iv_movie);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context.getApplicationContext(), MoviesDetailsActivity.class);
//                intent.putExtra("poster", movies_array_list.get(position).getPoster());
//                intent.putExtra("overview", movies_array_list.get(position).getOverview());
//                intent.putExtra("vote_average", movies_array_list.get(position).getVote_average());
//                intent.putExtra("title", movies_array_list.get(position).getTitle());
//                intent.putExtra("realse_date", movies_array_list.get(position).getRelease_date());

                intent.putExtra("movie", (Parcelable) movies_array_list.get(position));
                intent.putExtra("intent","movie");

                context.startActivity(intent);


            }
        });

    }

    @Override
    public int getItemCount() {
        return movies_array_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_movie)
        ImageView iv_movie;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
