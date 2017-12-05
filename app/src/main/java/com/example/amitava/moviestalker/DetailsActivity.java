package com.example.amitava.moviestalker;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by Amitava on 03-Dec-17.
 */

public class DetailsActivity extends AppCompatActivity {
    private TextView mTitle;
    private ImageView mPoster;
    private TextView mReleaseDate;
    private TextView mRating;
    private TextView mSynopsis;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_details_layout);

        String title = getIntent().getStringExtra("title");
        String image = getIntent().getStringExtra("image");
        String releaseDate = getIntent().getStringExtra("release_date");
        String rating = getIntent().getStringExtra("rating");
        String overview = getIntent().getStringExtra("synopsis");

        mTitle = (TextView) findViewById(R.id.tv_movie_title);
        mPoster = (ImageView) findViewById(R.id.iv_movie_poster);
        mReleaseDate = (TextView) findViewById(R.id.tv_movie_release_date);
        mRating = (TextView) findViewById(R.id.tv_movie_rating);
        mSynopsis = (TextView) findViewById(R.id.tv_movie_synopsis);

        mTitle.setText(title);
        Picasso.with(this).load(image).into(mPoster);
        mReleaseDate.setText(releaseDate);
        mRating.setText(rating);
        mSynopsis.setText(overview);
    }
}
