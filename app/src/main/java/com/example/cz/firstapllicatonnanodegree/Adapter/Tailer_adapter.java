package com.example.cz.firstapllicatonnanodegree.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cz.firstapllicatonnanodegree.Model.Review_model;
import com.example.cz.firstapllicatonnanodegree.Model.Tailer_model;
import com.example.cz.firstapllicatonnanodegree.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cz on 26/01/2018.
 */

public class Tailer_adapter extends RecyclerView.Adapter<Tailer_adapter.ViewHolder> {
    ArrayList<Tailer_model> tailers_array_list = new ArrayList<>();
    Context context;
    LayoutInflater layoutInflater;

    public Tailer_adapter(Context context, ArrayList<Tailer_model> tailers_array_list) {
        this.tailers_array_list = tailers_array_list;
        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public Tailer_adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = layoutInflater.from(parent.getContext()).inflate(R.layout.tailer_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(Tailer_adapter.ViewHolder holder, final int position) {
        holder.tv_tailer_name.setText(tailers_array_list.get(position).getName());
        holder.iv_tailer_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("http://www.youtube.com/watch?v=" + tailers_array_list.get(position).getKey());
                context.startActivity(new Intent(Intent.ACTION_VIEW, uri));
            }
        });
    }

    @Override
    public int getItemCount() {
        return tailers_array_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_trailer_name)
        TextView tv_tailer_name;
        @BindView(R.id.iv_trailer_play)
        ImageView iv_tailer_play;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
