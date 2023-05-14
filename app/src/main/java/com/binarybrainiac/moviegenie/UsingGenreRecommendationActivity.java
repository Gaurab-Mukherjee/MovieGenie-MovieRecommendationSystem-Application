package com.binarybrainiac.moviegenie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.binarybrainiac.moviegenie.ModelClass.UsingGenreRecommendationActivityModel;
import com.binarybrainiac.moviegenie.ModelClass.UsingGenreRecommendationInterFace;
import com.binarybrainiac.moviegenie.ModelClass.UsingTitleRecommendationActivityModel;
import com.binarybrainiac.moviegenie.ModelClass.UsingTitleRecommendationInterFace;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UsingGenreRecommendationActivity extends AppCompatActivity implements UsingGenreRecommendationInterFace {
    EditText ed_genre_name;
    EditText ed_year;
    Button btn_submit;
    TextView txt1;
    ProgressBar mProgressBar;
    RecyclerView listView;
    ArrayList<UsingGenreRecommendationActivityModel> GenreRecommendedMovieList = new ArrayList<>();
    UsingGenreRecommendationActivityAdapter usingGenreRecommendationActivityAdapter;
    // top panel
    View top_include;
    ImageView back;
    TextView txtTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_using_genre_recommendation);
        // top panel
        top_include = findViewById(R.id.top_include);
        back = findViewById(R.id.back);
        txtTitle = findViewById(R.id.txtTitle);
        txtTitle.setText("Genre Based Recommendation");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UsingGenreRecommendationActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        // -=--------------
        ed_genre_name = findViewById(R.id.ed_genre_name);
        ed_year = findViewById(R.id.ed_year);
        btn_submit = findViewById(R.id.btn_submit);
        txt1 = findViewById(R.id.txt1);
        mProgressBar = findViewById(R.id.mProgressBar);
        listView = findViewById(R.id.listView);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String genre = ed_genre_name.getText().toString().trim();
                String year = ed_year.getText().toString().trim();
                sendRequestToServer(genre, year);
//                Log.d("=====>", "onCreate: "+ ed_movie_name.getText());
            }
        });

        usingGenreRecommendationActivityAdapter = new UsingGenreRecommendationActivityAdapter(GenreRecommendedMovieList, UsingGenreRecommendationActivity.this);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listView.setLayoutManager(mLayoutManager);
        listView.setItemAnimator(new DefaultItemAnimator());
        listView.setAdapter(usingGenreRecommendationActivityAdapter);

    }

    public static final String mTag = "MyTag";
    JsonObjectRequest jsonObjectRequest = null;
    RequestQueue queue;
    int serverCount = 0;

    private void sendRequestToServer(String genre, String year) {
        mProgressBar.setVisibility(View.VISIBLE);
        String url = "https://movie-genie-api.onrender.com/genre_recommend_movie";
        Log.d("====>>", url);
        queue = Volley.newRequestQueue(UsingGenreRecommendationActivity.this);
        StringRequest request = new StringRequest(
                Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Handle the API response
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray("results");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        String adult = object.getString("adult");
                        String backdrop_path = object.getString("backdrop_path");
                        String belongs_to_collection = object.getString("belongs_to_collection");
                        String budget = object.getString("budget");
                        String genres = object.getString("genres");
                        String homepage = object.getString("homepage");
                        String id = object.getString("id");
                        String imdb_id = object.getString("imdb_id");
                        String original_language = object.getString("original_language");
                        String original_title = object.getString("original_title");
                        String overview = object.getString("overview");
                        String popularity = object.getString("popularity");
                        String xPoster_path = object.getString("poster_path");
                        String poster_path = "https://image.tmdb.org/t/p/w500" + xPoster_path;
                        String release_date = object.getString("release_date");
                        String revenue = object.getString("revenue");
                        String runtime = object.getString("runtime");
                        String title = object.getString("title");
                        String vote_average = object.getString("vote_average");
                        String vote_count = object.getString("vote_count");
                        GenreRecommendedMovieList.add(new UsingGenreRecommendationActivityModel(adult, backdrop_path, belongs_to_collection, budget, genres, homepage, id, imdb_id, original_language, original_title, overview, popularity, poster_path, release_date, revenue, runtime, title, vote_average, vote_count));
                    }
                    txt1.setVisibility(View.VISIBLE);
                    usingGenreRecommendationActivityAdapter.notifyDataSetChanged();
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
//                    Toast.makeText(UsingGenreRecommendationActivity.this, "Retry :(", Toast.LENGTH_SHORT).show();
//                } else {
//                    sendRequestToServer(title);
//                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("genre", genre);
                params.put("year", year);
                return params;
            }
        };
        queue.add(request);
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
//                    title = ed_movie_name.getText().toString().trim();
//                    sendRequestToServer(title);
                }
            });
            Dialog dialog = builder.create();
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //==========================  Call Back =====================================
    @Override
    public void getGenreRecommendedMovieData(UsingGenreRecommendationActivityModel itemData, int position) {
        Intent intent = new Intent(UsingGenreRecommendationActivity.this, MovieDetailsActivity.class);
        intent.putExtra("poster_path", itemData.getPoster_path());
        intent.putExtra("adult", itemData.getAdult());
        intent.putExtra("overview", itemData.getOverview());
        intent.putExtra("release_date", itemData.getRelease_date());
        intent.putExtra("id", itemData.getId());
        intent.putExtra("genre_ids", itemData.getGenres());
        intent.putExtra("original_title", itemData.getOriginal_title());
        intent.putExtra("original_language", itemData.getOriginal_language());
        intent.putExtra("title", itemData.getTitle());
        intent.putExtra("backdrop_path", itemData.getBackdrop_path());
        intent.putExtra("popularity", itemData.getPopularity());
        intent.putExtra("vote_count", itemData.getVote_count());
        intent.putExtra("vote_average", itemData.getVote_average());
        startActivity(intent);
    }


    // ------------------------------- Adapter -------------------------------------
    static class UsingGenreRecommendationActivityAdapter extends RecyclerView.Adapter<UsingGenreRecommendationActivityAdapter.ViewHolder> {
        ArrayList<UsingGenreRecommendationActivityModel> aList;
        UsingGenreRecommendationInterFace usingGenreRecommendationInterFace;

        UsingGenreRecommendationActivityAdapter(ArrayList<UsingGenreRecommendationActivityModel> list, UsingGenreRecommendationInterFace usingGenreRecommendationInterFace) {
            this.aList = list;
            this.usingGenreRecommendationInterFace = usingGenreRecommendationInterFace;
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
        public UsingGenreRecommendationActivityAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_home_activity, parent, false);
            return new UsingGenreRecommendationActivityAdapter.ViewHolder(itemView);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull UsingGenreRecommendationActivityAdapter.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
            final UsingGenreRecommendationActivityModel item = aList.get(position);
            if (item != null) {
                try {
                    holder.txtOriginalTitle_Title.setText((item.getOriginal_title()));
                    holder.txtTitle.setText("(" + item.getTitle() + ")");
                    holder.rating.setRating(Float.parseFloat(item.getPopularity()));
                    Picasso.get().load(item.getPoster_path()).into(holder.txtPosterImg);
                    holder.rootLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            UsingGenreRecommendationActivityModel clickItem = aList.get(position);
                            if (clickItem != null) {
                                try {
                                    usingGenreRecommendationInterFace.getGenreRecommendedMovieData(clickItem, position);
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