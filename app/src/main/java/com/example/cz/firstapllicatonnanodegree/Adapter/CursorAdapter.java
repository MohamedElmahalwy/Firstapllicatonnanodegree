package com.example.cz.firstapllicatonnanodegree.Adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.cz.firstapllicatonnanodegree.Activites.MoviesDetailsActivity;
import com.example.cz.firstapllicatonnanodegree.Model.Movie_model;
import com.example.cz.firstapllicatonnanodegree.R;
import com.example.cz.firstapllicatonnanodegree.Utils.DataContract;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cz on 26/01/2018.
 */

public class CursorAdapter extends RecyclerView.Adapter<CursorAdapter.ViewHolder> {
    Cursor cursor;
    Context context;
    LayoutInflater layoutInflater;

    public CursorAdapter(Context context, Cursor cursor) {
        this.cursor = cursor;
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
        if (cursor != null) {
            cursor.moveToFirst();
            Picasso.with(context)
                    .load(posterUrl(cursor, position))
                    .error(R.drawable.ic_launcher_foreground)
                    .into(holder.iv_movie);
        }
        holder.iv_movie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cursor.moveToPosition(position);
                Intent intent = new Intent(context, MoviesDetailsActivity.class);
                intent.putExtra("intent", "cursor");
                intent.putExtra("title", cursor.getString(cursor.getColumnIndex(DataContract.TableContract.MOVIE_TITLE)));
                intent.putExtra("overview", cursor.getString(cursor.getColumnIndex(DataContract.TableContract.MOVIE_OVERVIEW)));
                intent.putExtra("vote", cursor.getString(cursor.getColumnIndex(DataContract.TableContract.MOVIE_VOTE)));
                intent.putExtra("poster", cursor.getString(cursor.getColumnIndex(DataContract.TableContract.MOVIE_POSTER)));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (cursor != null) {
            return cursor.getCount();
        } else {
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_movie)
        ImageView iv_movie;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private String posterUrl(Cursor cursor, int position) {
        String poster;
        cursor.moveToPosition(position);
        poster = cursor.getString(cursor.getColumnIndex(DataContract.TableContract.MOVIE_POSTER));
        return poster;
    }
}
