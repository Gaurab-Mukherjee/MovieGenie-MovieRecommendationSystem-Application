package com.binarybrainiac.moviegenie;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.binarybrainiac.moviegenie.ModelClass.HomeActivityInterFace;
import com.binarybrainiac.moviegenie.ModelClass.HomeActivityModel;
import com.binarybrainiac.moviegenie.ModelClass.UsingTitleRecommendationActivityModel;
import com.binarybrainiac.moviegenie.ModelClass.UsingTitleRecommendationInterFace;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UsingTitleRecommendationActivity extends AppCompatActivity implements UsingTitleRecommendationInterFace {
    EditText ed_movie_name;
    Button btn_submit;
    TextView txt1;
    String title;
    ProgressBar mProgressBar;
    RecyclerView listView;
    ArrayList<UsingTitleRecommendationActivityModel> TitleRecommendedMovieList = new ArrayList<>();
    UsingTitleRecommendationActivityAdapter usingTitleRecommendationActivityAdapter;
    // top panel
    View top_include;
    ImageView back;
    TextView txtTitle;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_using_title_recommandation);
        // top panel
        top_include = findViewById(R.id.top_include);
        back = findViewById(R.id.back);
        txtTitle = findViewById(R.id.txtTitle);
        txtTitle.setText("Title Based Recommendation");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UsingTitleRecommendationActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        // -=--------------
        ed_movie_name = findViewById(R.id.ed_movie_name);
        btn_submit = findViewById(R.id.btn_submit);
        txt1 = findViewById(R.id.txt1);
        mProgressBar = findViewById(R.id.mProgressBar);
        listView = findViewById(R.id.listView);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title = ed_movie_name.getText().toString().trim();
                sendRequestToServer(title);
//                Log.d("=====>", "onCreate: "+ ed_movie_name.getText());
            }
        });

        usingTitleRecommendationActivityAdapter = new UsingTitleRecommendationActivityAdapter(TitleRecommendedMovieList, UsingTitleRecommendationActivity.this);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listView.setLayoutManager(mLayoutManager);
        listView.setItemAnimator(new DefaultItemAnimator());
        listView.setAdapter(usingTitleRecommendationActivityAdapter);
    }

    public static final String mTag = "MyTag";
    JsonObjectRequest jsonObjectRequest = null;
    RequestQueue queue;
    int serverCount = 0;

    private void sendRequestToServer(String title) {
        mProgressBar.setVisibility(View.VISIBLE);
        String url = "https://movie-genie-api.onrender.com/recommend_movie";
        Log.d("====>>", url);
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("title", title);
//        Log.d("======>>>>", "sendRequestToServer: "+params);
//        Log.d("----===>>>>", "sendRequestToServer: "+new JSONObject(params));
        queue = Volley.newRequestQueue(UsingTitleRecommendationActivity.this);
        jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, url, new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    // Handle the API response
//                    Log.d(":::::>>", "onResponse: No Response :(");
//                    Log.d("------->>", "onResponse: " + response);
                    JSONArray array = response.getJSONArray("results");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        String adult = object.getString("adult");
                        String backdrop_path = object.getString("backdrop_path");
                        String genre_ids = object.getString("genre_ids");
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
                        String movieId = object.getString("movieId");
                        String imdbId = object.getString("imdbId");
                        TitleRecommendedMovieList.add(new UsingTitleRecommendationActivityModel(adult, backdrop_path, genre_ids, original_language, original_title, overview, popularity, poster_path, release_date, title, video, vote_average, vote_count, movieId, imdbId));
                    }
                    txt1.setVisibility(View.VISIBLE);
                    usingTitleRecommendationActivityAdapter.notifyDataSetChanged();
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

                if (serverCount >= 3) {
                    serverCount = 0;
                    askForRetry();
                    Toast.makeText(UsingTitleRecommendationActivity.this, "Retry :(", Toast.LENGTH_SHORT).show();
                } else {
                    sendRequestToServer(title);
                }
            }
        });
        queue.add(jsonObjectRequest);
    }

    //==========================  Call Back =====================================
    @Override
    public void getTitleRecommendedMovieData(UsingTitleRecommendationActivityModel itemData, int position) {
        Log.d("--=>", "getUpcommingMovieData: :) " + position);
        Intent intent = new Intent(UsingTitleRecommendationActivity.this, MovieDetailsActivity.class);
        intent.putExtra("poster_path", itemData.getPoster_path());
        intent.putExtra("adult", itemData.getAdult());
        intent.putExtra("overview", itemData.getOverview());
        intent.putExtra("release_date", itemData.getRelease_date());
        intent.putExtra("id", itemData.getMovie_id());
        intent.putExtra("genre_ids", itemData.getGenre_ids());
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
    static class UsingTitleRecommendationActivityAdapter extends RecyclerView.Adapter<UsingTitleRecommendationActivityAdapter.ViewHolder> {
        ArrayList<UsingTitleRecommendationActivityModel> aList;
        UsingTitleRecommendationInterFace usingTitleRecommendationInterFace;

        UsingTitleRecommendationActivityAdapter(ArrayList<UsingTitleRecommendationActivityModel> list, UsingTitleRecommendationInterFace usingTitleRecommendationInterFace) {
            this.aList = list;
            this.usingTitleRecommendationInterFace = usingTitleRecommendationInterFace;
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
        public UsingTitleRecommendationActivityAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_home_activity, parent, false);
            return new UsingTitleRecommendationActivityAdapter.ViewHolder(itemView);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull UsingTitleRecommendationActivityAdapter.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
            final UsingTitleRecommendationActivityModel item = aList.get(position);
            if (item != null) {
                try {
                    holder.txtOriginalTitle_Title.setText((item.getOriginal_title()));
                    holder.txtTitle.setText("(" + item.getTitle() + ")");
                    holder.rating.setRating(Float.parseFloat(item.getPopularity()));
                    Picasso.get().load(item.getPoster_path()).into(holder.txtPosterImg);
                    holder.rootLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            UsingTitleRecommendationActivityModel clickItem = aList.get(position);
                            if (clickItem != null) {
                                try {
                                    usingTitleRecommendationInterFace.getTitleRecommendedMovieData(clickItem, position);
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

    private void askForRetry() {
        // cancel server request
        if (jsonObjectRequest != null) {
            queue.cancelAll(mTag);
        }
        try {
            //mprogressBar.setVisibility(View.GONE);
            //txtWText.setVisibility(View.GONE);
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
            builder.setTitle("");
            builder.setMessage("Due to the slow or unstable network issue,  Retry by clicking submit.");
            builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    title = ed_movie_name.getText().toString().trim();
                    sendRequestToServer(title);
                }
            });
            Dialog dialog = builder.create();
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
