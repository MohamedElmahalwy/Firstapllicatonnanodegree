package com.example.cz.firstapllicatonnanodegree.Activites;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.example.cz.firstapllicatonnanodegree.R;
import com.example.cz.firstapllicatonnanodegree.Utils.Connection;
import com.squareup.picasso.Picasso;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_details);
        ButterKnife.bind(this);

        set_values();

    }

    void set_values() {
        tv_movie_title_header.setText(getIntent().getStringExtra("title"));
        tv_relase_date.setText(getIntent().getStringExtra("realse_date"));
        tv_vote_average.setText(getIntent().getStringExtra("vote_average") + "/10");
        tv_overview.setText(getIntent().getStringExtra("overview"));
        Picasso.with(this).
                load((Connection.image_url) + getIntent().getStringExtra("poster")).
                into(iv_poster);
    }
}
