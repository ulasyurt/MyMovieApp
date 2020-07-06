package com.example.mymovieapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        String title=intent.getExtras().getString("title");
        String poster_path=intent.getExtras().getString("poster_path");
        String overview=intent.getExtras().getString("overview");
        Double popularity=intent.getExtras().getDouble("popularity");
        Double vote_average = intent.getExtras().getDouble("vote_average");

        setTitle(title);

        Glide.with(this)
                .load("https://image.tmdb.org/t/p/w500/"+poster_path)
                .into((ImageView) findViewById(R.id.iv_movie_poster));

        TextView tv = findViewById(R.id.movie_title);
        tv.setText(title);

        TextView tv2=findViewById(R.id.movie_overview);
        tv2.setText(overview);

        TextView tv3 = findViewById(R.id.movie_popularity);
        tv3.setText(String.valueOf(popularity));

        TextView tv4 = findViewById(R.id.movie_vote);
        tv4.setText(String.valueOf(vote_average));

    }
}