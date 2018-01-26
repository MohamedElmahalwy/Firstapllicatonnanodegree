package com.example.cz.firstapllicatonnanodegree.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cz.firstapllicatonnanodegree.Model.Movie_model;
import com.example.cz.firstapllicatonnanodegree.Model.Review_model;
import com.example.cz.firstapllicatonnanodegree.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cz on 26/01/2018.
 */

public class Review_adapter extends RecyclerView.Adapter<Review_adapter.ViewHolder> {
    ArrayList<Review_model> reviwes_array_list = new ArrayList<>();
    Context context;
    LayoutInflater layoutInflater;

    public Review_adapter(Context context, ArrayList<Review_model> reviwes_array_list) {
        this.reviwes_array_list = reviwes_array_list;
        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public Review_adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = layoutInflater.from(parent.getContext()).inflate(R.layout.review_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(Review_adapter.ViewHolder holder, int position) {
        holder.tv_auhtor.setText(reviwes_array_list.get(position).getAuthor());
        holder.tv_content.setText(reviwes_array_list.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return reviwes_array_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_author)
        TextView tv_auhtor;
        @BindView(R.id.tv_content)
        TextView tv_content;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
