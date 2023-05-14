package com.binarybrainiac.moviegenie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Rating;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.binarybrainiac.moviegenie.ModelClass.HomeActivityInterFace;
import com.binarybrainiac.moviegenie.ModelClass.HomeActivityModel;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity implements HomeActivityInterFace {
    LinearLayout btn_recommendation_title, btn_recommendation_genre, btn_recommendation_emoji;
    RecyclerView listView;
    ProgressBar mProgressBar;
    ArrayList<HomeActivityModel> UpcommingMovieList = new ArrayList<>();
    HomeActivityAdapter homeActivityAdapter;
    // top panel
    View top_include;
    ImageView log_out;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        // top panel
        top_include = findViewById(R.id.top_include);
        log_out = findViewById(R.id.log_out);
        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences preferences =getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.apply();
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                Toast.makeText(HomeActivity.this, "Logout Successfully...", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        // -=--------------
        btn_recommendation_title = findViewById(R.id.btn_recommendation_title);
        btn_recommendation_genre = findViewById(R.id.btn_recommendation_genre);
        btn_recommendation_emoji = findViewById(R.id.btn_recommendation_emoji);
        btn_recommendation_title.setOnClickListener(view -> {
            Intent intent = new Intent(HomeActivity.this, UsingTitleRecommendationActivity.class);
            startActivity(intent);
        });

        btn_recommendation_genre.setOnClickListener(view -> {
            Intent intent = new Intent(HomeActivity.this, UsingGenreRecommendationActivity.class);
            startActivity(intent);
        });

        btn_recommendation_emoji.setOnClickListener(view -> {
            Intent intent = new Intent(HomeActivity.this, UsingEmojiRecommendationActivity.class);
            startActivity(intent);
        });
        listView = findViewById(R.id.listView);
        mProgressBar = findViewById(R.id.mProgressBar);
        homeActivityAdapter = new HomeActivityAdapter(UpcommingMovieList, HomeActivity.this);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listView.setLayoutManager(mLayoutManager);
        listView.setItemAnimator(new DefaultItemAnimator());
        listView.setAdapter(homeActivityAdapter);
        sendRequestToServer();
    }


    public static final String mTag = "MyTag";
    JsonObjectRequest jsonObjectRequest = null;
    RequestQueue queue;
    int serverCount = 0;

    private void sendRequestToServer() {
        mProgressBar.setVisibility(View.VISIBLE);
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        Log.d("---=>", "sendRequestToServer: " + today);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, 1);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String one_year_later = dateFormat.format(calendar.getTime());
        Log.d("--===>", "sendRequestToServer: " + one_year_later);


        String url = "https://api.themoviedb.org/3/discover/movie?api_key=c1b5b5d1017cbf9f1ae2e311e9ab068a&language=en-US&with_original_language=hi&include_adult=false&page=1" +
                "&primary_release_date.gte=" + today +
                "&primary_release_date.lte=" + one_year_later;
        Log.d("====>>", url);
        queue = Volley.newRequestQueue(HomeActivity.this);
        jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    // Handle the API response
                    JSONArray array = response.getJSONArray("results");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        String adult = object.getString("adult");
                        String backdrop_path = object.getString("backdrop_path");
                        String genre_ids = object.getString("genre_ids");
                        String id = object.getString("id");
                        String original_language = object.getString("original_language");
                        String original_title = object.getString("original_title");
                        String overview = object.getString("overview");
                        String popularity = object.getString("popularity");
                        String xPoster_path = object.getString("poster_path");
                        String poster_path = "https://image.tmdb.org/t/p/w500" + xPoster_path;
                        String release_date = object.getString("release_date");
                        String title = object.getString("title");
                        String video = object.getString("video");
                        String vote_average = object.getString("vote_average");
                        String vote_count = object.getString("vote_count");
                        UpcommingMovieList.add(new HomeActivityModel(adult, backdrop_path, genre_ids, id, original_language, original_title, overview, popularity, poster_path, release_date, title, video, vote_average, vote_count));
                    }
                    homeActivityAdapter.notifyDataSetChanged();
                    mProgressBar.setVisibility(View.GONE);

                } catch (JSONException e) {
                    e.printStackTrace();
                    mProgressBar.setVisibility(View.GONE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle API errors
                ++serverCount;
                Log.d("----->>", serverCount + " " + error.getMessage());
//                if (serverCount >= 3) {
//                    serverCount = 0;
//                    askForRetry();
//                } else {
//                    sendRequestToServer(title);
//                }
            }
        });
        queue.add(jsonObjectRequest);
    }

    //==========================  Call Back =====================================
    @Override
    public void getUpcommingMovieData(HomeActivityModel itemData, int position) {
        Log.d("--=>", "getUpcommingMovieData: :) " + position);
        Intent intent = new Intent(HomeActivity.this, MovieDetailsActivity.class);
        intent.putExtra("poster_path", itemData.getPoster_path());
        intent.putExtra("adult", itemData.getAdult());
        intent.putExtra("overview", itemData.getOverview());
        intent.putExtra("release_date", itemData.getRelease_date());
        intent.putExtra("id", itemData.getId());
        intent.putExtra("genre_ids",itemData.getGenre_ids());
        intent.putExtra("original_title", itemData.getOriginal_title());
        intent.putExtra("original_language", itemData.getOriginal_language());
        intent.putExtra("title", itemData.getTitle());
        intent.putExtra("backdrop_path", itemData.getBackdrop_path());
        intent.putExtra("popularity", itemData.getPopularity());
        intent.putExtra("vote_count", itemData.getVote_count());
        intent.putExtra("video", itemData.getVideo());
        intent.putExtra("vote_average", itemData.getVote_average());
        startActivity(intent);
    }

    // ------------------------------- Adapter -------------------------------------
    static class HomeActivityAdapter extends RecyclerView.Adapter<HomeActivityAdapter.ViewHolder> {
        ArrayList<HomeActivityModel> aList;
        HomeActivityInterFace homeActivityInterFace;

        HomeActivityAdapter(ArrayList<HomeActivityModel> list, HomeActivityInterFace homeActivityInterFace) {
            this.aList = list;
            this.homeActivityInterFace = homeActivityInterFace;
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            TextView txtOriginalTitle_Title;
            TextView txtTitle;
            RatingBar rating;
            ImageView txtPosterImg;
            LinearLayout rootLayout;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                txtOriginalTitle_Title = itemView.findViewById(R.id.txtOriginalTitle);
                txtTitle = itemView.findViewById(R.id.txtTitle);
                rating = itemView.findViewById(R.id.rating);
                txtPosterImg = itemView.findViewById(R.id.txtPosterImg);
                rootLayout = itemView.findViewById(R.id.rootLayout);
            }
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_home_activity, parent, false);
            return new ViewHolder(itemView);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
            final HomeActivityModel item = aList.get(position);
            if (item != null) {
                try {
                    holder.txtOriginalTitle_Title.setText((item.getOriginal_title()));
                    holder.txtTitle.setText("(" + item.getTitle() + ")");
                    holder.rating.setRating(Float.parseFloat(item.getPopularity()));
                    Picasso.get().load(item.getPoster_path()).into(holder.txtPosterImg);
                    holder.rootLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            HomeActivityModel clickItem = aList.get(position);
                            if (clickItem != null) {
                                try {
                                    homeActivityInterFace.getUpcommingMovieData(clickItem, position);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public int getItemCount() {
            return aList.size();
        }
    }
}