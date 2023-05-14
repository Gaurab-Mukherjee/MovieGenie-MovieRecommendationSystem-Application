package com.binarybrainiac.moviegenie;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.LauncherActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MovieDetailsActivity extends AppCompatActivity {
    ImageView imgPoster;
    TextView txtOriginalTitle_title, txtOverview, txtOriginalLanguage, txtVoteCount, txtReleaseDate, txtVoteAverage;
    Button btn_genre_1, btn_genre_2, btn_genre_3;
    RatingBar strRating;
    // top panel
    View top_include;
    ImageView back;
    TextView txtTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        // top panel
        top_include = findViewById(R.id.top_include);
        back = findViewById(R.id.back);
        txtTitle = findViewById(R.id.txtTitle);
        txtTitle.setText("Recommendations");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MovieDetailsActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        // -=--------------
        imgPoster = findViewById(R.id.imgPoster);
        txtOriginalTitle_title = findViewById(R.id.txtOriginalTitle_title);
        txtOverview = findViewById(R.id.txtOverview);
        txtOriginalLanguage = findViewById(R.id.txtOriginalLanguage);
        txtVoteCount = findViewById(R.id.txtVoteCount);
        txtReleaseDate = findViewById(R.id.txtReleaseDate);
        txtVoteAverage = findViewById(R.id.txtVoteAverage);
        strRating = findViewById(R.id.strRating);
        btn_genre_1 = findViewById(R.id.btn_genre_1);
        btn_genre_2 = findViewById(R.id.btn_genre_2);
        btn_genre_3 = findViewById(R.id.btn_genre_3);
        String poster_path = getIntent().getStringExtra("poster_path");
        String adult = getIntent().getStringExtra("adult");
        String overview = getIntent().getStringExtra("overview");
        String release_date = getIntent().getStringExtra("release_date");
        String id = getIntent().getStringExtra("id");
        String genre_ids = getIntent().getStringExtra("genre_ids");
        String original_title = getIntent().getStringExtra("original_title");
        String original_language = getIntent().getStringExtra("original_language");
        String title = getIntent().getStringExtra("title");
        String backdrop_path = getIntent().getStringExtra("backdrop_path");
        String popularity = getIntent().getStringExtra("popularity");
        String vote_count = getIntent().getStringExtra("vote_count");
        String video = getIntent().getStringExtra("video");
        String vote_average = getIntent().getStringExtra("vote_average");

        display_movieDetails(poster_path, adult, overview, release_date, id, genre_ids, original_title, original_language, title, backdrop_path, popularity, vote_count, video, vote_average);
    }

    @SuppressLint("SetTextI18n")
    private void display_movieDetails(String poster_path, String adult, String overview, String release_date, String id, String genre_ids, String original_title, String original_language, String title, String backdrop_path, String popularity, String vote_count, String video, String vote_average) {
        Picasso.get().load(poster_path).into(imgPoster);
        txtOriginalTitle_title.setText(original_title + "(" + title + ")");
        txtOverview.setText(overview);
        if (original_language.equals("hi")) {
            txtOriginalLanguage.setText("Hindi");
        } else {
            txtOriginalLanguage.setText("English");
        }
        txtVoteCount.setText(vote_count);
        txtReleaseDate.setText(release_date.substring(0, 4));
        txtVoteAverage.setText(vote_average);
        strRating.setRating(Float.parseFloat(popularity));
        Log.d("=====--->>", "display_movieDetails: " + genre_ids);
        String[] genre = genre_ids.substring(1, genre_ids.length() - 1).split(",");
        Log.d("---===>", "display_movieDetails: " + Arrays.toString(genre));

        for (int i = 0; i < genre.length; i++) {
            String gen = genre[i];
            if (gen.equalsIgnoreCase(String.valueOf(28))) {
                // action
                btn_genre_1.setText("Action");
                btn_genre_1.setVisibility(View.VISIBLE);
                btn_genre_2.setVisibility(View.GONE);
                btn_genre_3.setVisibility(View.GONE);
            } else if (gen.equalsIgnoreCase(String.valueOf(12))) {
                //adventure
                btn_genre_1.setText("Adventure");
                btn_genre_1.setVisibility(View.VISIBLE);
                btn_genre_2.setVisibility(View.GONE);
                btn_genre_3.setVisibility(View.GONE);
            } else if (gen.equalsIgnoreCase(String.valueOf(16))) {
                //animation
                btn_genre_1.setText("Animation");
                btn_genre_1.setVisibility(View.VISIBLE);
                btn_genre_2.setVisibility(View.GONE);
                btn_genre_3.setVisibility(View.GONE);
            } else if (gen.equalsIgnoreCase(String.valueOf(35))) {
                //comedy
                btn_genre_1.setText("Comedy");
                btn_genre_1.setVisibility(View.VISIBLE);
                btn_genre_2.setVisibility(View.GONE);
                btn_genre_3.setVisibility(View.GONE);
            } else if (gen.equalsIgnoreCase(String.valueOf(80))) {
                //crime
                btn_genre_1.setText("Crime");
                btn_genre_1.setVisibility(View.VISIBLE);
                btn_genre_2.setVisibility(View.GONE);
                btn_genre_3.setVisibility(View.GONE);
            } else if (gen.equalsIgnoreCase(String.valueOf(99))) {
                //documentary
                btn_genre_1.setText("Documentary");
                btn_genre_1.setVisibility(View.VISIBLE);
                btn_genre_2.setVisibility(View.GONE);
                btn_genre_3.setVisibility(View.GONE);
            } else if (gen.equalsIgnoreCase(String.valueOf(18))) {
                //drama
                btn_genre_1.setText("Drama");
                btn_genre_1.setVisibility(View.VISIBLE);
                btn_genre_2.setVisibility(View.GONE);
                btn_genre_3.setVisibility(View.GONE);
            } else if (gen.equalsIgnoreCase(String.valueOf(10751))) {
                //family
                btn_genre_1.setText("Family");
                btn_genre_1.setVisibility(View.VISIBLE);
                btn_genre_2.setVisibility(View.GONE);
                btn_genre_3.setVisibility(View.GONE);
            } else if (gen.equalsIgnoreCase(String.valueOf(14))) {
                //fantasy
                btn_genre_1.setText("Fantasy");
                btn_genre_1.setVisibility(View.VISIBLE);
                btn_genre_2.setVisibility(View.GONE);
                btn_genre_3.setVisibility(View.GONE);
            } else if (gen.equalsIgnoreCase(String.valueOf(36))) {
                //history
                btn_genre_1.setText("History");
                btn_genre_1.setVisibility(View.VISIBLE);
                btn_genre_2.setVisibility(View.GONE);
                btn_genre_3.setVisibility(View.GONE);
            } else if (gen.equalsIgnoreCase(String.valueOf(27))) {
                //horror
                btn_genre_1.setText("Horror");
                btn_genre_1.setVisibility(View.VISIBLE);
                btn_genre_2.setVisibility(View.GONE);
                btn_genre_3.setVisibility(View.GONE);
            } else if (gen.equalsIgnoreCase(String.valueOf(10402))) {
                //music
                btn_genre_1.setText("Music");
                btn_genre_1.setVisibility(View.VISIBLE);
                btn_genre_2.setVisibility(View.GONE);
                btn_genre_3.setVisibility(View.GONE);
            } else if (gen.equalsIgnoreCase(String.valueOf(9648))) {
                //mystery
                btn_genre_1.setText("Mystery");
                btn_genre_1.setVisibility(View.VISIBLE);
                btn_genre_2.setVisibility(View.GONE);
                btn_genre_3.setVisibility(View.GONE);
            } else if (gen.equalsIgnoreCase(String.valueOf(10749))) {
                //romance
                btn_genre_1.setText("Romance");
                btn_genre_1.setVisibility(View.VISIBLE);
                btn_genre_2.setVisibility(View.GONE);
                btn_genre_3.setVisibility(View.GONE);
            } else if (gen.equalsIgnoreCase(String.valueOf(878))) {
                //science fiction
                btn_genre_1.setText("Science Fiction");
                btn_genre_1.setVisibility(View.VISIBLE);
                btn_genre_2.setVisibility(View.GONE);
                btn_genre_3.setVisibility(View.GONE);
            } else if (gen.equalsIgnoreCase(String.valueOf(10770))) {
                //tv movie
                btn_genre_1.setText("Tv Movie");
                btn_genre_1.setVisibility(View.VISIBLE);
                btn_genre_2.setVisibility(View.GONE);
                btn_genre_3.setVisibility(View.GONE);
            } else if (gen.equalsIgnoreCase(String.valueOf(53))) {
                //thriller
                btn_genre_1.setText("Thriller");
                btn_genre_1.setVisibility(View.VISIBLE);
                btn_genre_2.setVisibility(View.GONE);
                btn_genre_3.setVisibility(View.GONE);
            } else if (gen.equalsIgnoreCase(String.valueOf(10752))) {
                //war
                btn_genre_1.setText("War");
                btn_genre_1.setVisibility(View.VISIBLE);
                btn_genre_2.setVisibility(View.GONE);
                btn_genre_3.setVisibility(View.GONE);
            } else if (gen.equalsIgnoreCase(String.valueOf(37))) {
                //western
                btn_genre_1.setText("Western");
                btn_genre_1.setVisibility(View.VISIBLE);
                btn_genre_2.setVisibility(View.GONE);
                btn_genre_3.setVisibility(View.GONE);
            }
        }
    }
}