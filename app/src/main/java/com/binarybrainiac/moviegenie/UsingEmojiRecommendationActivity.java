package com.binarybrainiac.moviegenie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.binarybrainiac.moviegenie.ModelClass.UsingEmojiRecommendationActivityModel;
import com.binarybrainiac.moviegenie.ModelClass.UsingEmojiRecommendationInterFace;
import com.binarybrainiac.moviegenie.ModelClass.UsingGenreRecommendationActivityModel;
import com.binarybrainiac.moviegenie.ModelClass.UsingTitleRecommendationActivityModel;
import com.binarybrainiac.moviegenie.ModelClass.UsingTitleRecommendationInterFace;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UsingEmojiRecommendationActivity extends AppCompatActivity implements UsingEmojiRecommendationInterFace {
    RadioGroup radio_group;
    RadioButton action, drama, comedy, horror, fantasy, science_fiction, romance, mystery, family, crime, adventure, history;
    Button btn_submit;
    TextView txt1;
    ProgressBar mProgressBar;
    RecyclerView listView;
    ArrayList<UsingEmojiRecommendationActivityModel> EmojiRecommendedMovieList = new ArrayList<>();
    UsingEmojiRecommendationActivityAdapter usingEmojiRecommendationActivityAdapter;
    // top panel
    View top_include;
    ImageView back;
    TextView txtTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_using_emoji_recommendation);
        // top panel
        top_include = findViewById(R.id.top_include);
        back = findViewById(R.id.back);
        txtTitle = findViewById(R.id.txtTitle);
        txtTitle.setText("Emoji Based Recommendation");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UsingEmojiRecommendationActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        // -=--------------
        radio_group = findViewById(R.id.radio_group);
        action = findViewById(R.id.action);
        drama = findViewById(R.id.drama);
        comedy = findViewById(R.id.comedy);
        horror = findViewById(R.id.horror);
        fantasy = findViewById(R.id.fantasy);
        science_fiction = findViewById(R.id.science_fiction);
        romance = findViewById(R.id.romance);
        mystery = findViewById(R.id.mystery);
        family = findViewById(R.id.family);
        crime = findViewById(R.id.crime);
        adventure = findViewById(R.id.adventure);
        history = findViewById(R.id.history);

        btn_submit = findViewById(R.id.btn_submit);
        txt1 = findViewById(R.id.txt1);
        mProgressBar = findViewById(R.id.mProgressBar);
        listView = findViewById(R.id.listView);

//        radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup radioGroup, int i) {
//                switch (i){
//                    case R.id.action:
//                        Log.d("[][][][][]", "onCheckedChanged: 1");
//                        break;
//                    case R.id.drama:
//                        Log.d("[][][][][]", "onCheckedChanged: 2");
//                        break;
//                    case R.id.comedy:
//                        Log.d("[][][][][]", "onCheckedChanged: 3");
//                        break;
//                }
//            }
//        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (action.isChecked()) {
                    String emoji = "&#x1F3AC;";
                    sendRequestToServer(emoji);
                } else if (drama.isChecked()) {
                    String emoji = "&#x1F3AD;";
                    sendRequestToServer(emoji);
                } else if (comedy.isChecked()) {
                    String emoji = "&#x1F921;";
                    sendRequestToServer(emoji);
                } else if (horror.isChecked()) {
                    String emoji = "&#x1F47B;";
                    sendRequestToServer(emoji);
                } else if (fantasy.isChecked()) {
                    String emoji = "&#x1F451;";
                    sendRequestToServer(emoji);
                } else if (science_fiction.isChecked()) {
                    String emoji = "&#x1F680;";
                    sendRequestToServer(emoji);
                } else if (romance.isChecked()) {
                    String emoji = "&#x1F498;";
                    sendRequestToServer(emoji);
                } else if (mystery.isChecked()) {
                    String emoji = "&#x1F575;";
                    sendRequestToServer(emoji);
                } else if (family.isChecked()) {
                    String emoji = "&#x1F46A;";
                    sendRequestToServer(emoji);
                } else if (crime.isChecked()) {
                    String emoji = "&#x1F608;";
                    sendRequestToServer(emoji);
                } else if (adventure.isChecked()) {
                    String emoji = "&#x1FA82;";
                    sendRequestToServer(emoji);
                } else if (history.isChecked()) {
                    String emoji = "&#x1F36F;";
                    sendRequestToServer(emoji);
                }
            }
        });
        usingEmojiRecommendationActivityAdapter = new UsingEmojiRecommendationActivityAdapter(EmojiRecommendedMovieList, UsingEmojiRecommendationActivity.this);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listView.setLayoutManager(mLayoutManager);
        listView.setItemAnimator(new DefaultItemAnimator());
        listView.setAdapter(usingEmojiRecommendationActivityAdapter);
    }

    public static final String mTag = "MyTag";
    JsonObjectRequest jsonObjectRequest = null;
    RequestQueue queue;
    int serverCount = 0;

    private void sendRequestToServer(String emoji) {
        Log.d("--======>???///", "sendRequestToServer: "+emoji);
        mProgressBar.setVisibility(View.VISIBLE);
        String url = "https://movie-genie-api.onrender.com/emoji_recommend_movie";
        Log.d("====>>", url);
        queue = Volley.newRequestQueue(UsingEmojiRecommendationActivity.this);
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

                        EmojiRecommendedMovieList.add(new UsingEmojiRecommendationActivityModel(adult, backdrop_path, genre_ids, id, original_language, original_title, overview, popularity, poster_path, release_date, title, video, vote_average, vote_count));
                    }
                    txt1.setVisibility(View.VISIBLE);
                    usingEmojiRecommendationActivityAdapter.notifyDataSetChanged();
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
                params.put("emoji", emoji);
                return params;
            }
        };
        queue.add(request);
    }

    //==========================  Call Back =====================================
    @Override
    public void getEmojiRecommendedMovieData(UsingEmojiRecommendationActivityModel itemData, int position) {
        Intent intent = new Intent(UsingEmojiRecommendationActivity.this, MovieDetailsActivity.class);
        intent.putExtra("poster_path", itemData.getPoster_path());
        intent.putExtra("adult", itemData.getAdult());
        intent.putExtra("overview", itemData.getOverview());
        intent.putExtra("release_date", itemData.getRelease_date());
        intent.putExtra("id", itemData.getId());
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
    static class UsingEmojiRecommendationActivityAdapter extends RecyclerView.Adapter<UsingEmojiRecommendationActivityAdapter.ViewHolder> {
        ArrayList<UsingEmojiRecommendationActivityModel> aList;
        UsingEmojiRecommendationInterFace usingEmojiRecommendationInterFace;

        UsingEmojiRecommendationActivityAdapter(ArrayList<UsingEmojiRecommendationActivityModel> list, UsingEmojiRecommendationInterFace usingEmojiRecommendationInterFace) {
            this.aList = list;
            this.usingEmojiRecommendationInterFace = usingEmojiRecommendationInterFace;
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
        public UsingEmojiRecommendationActivityAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_home_activity, parent, false);
            return new UsingEmojiRecommendationActivityAdapter.ViewHolder(itemView);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull UsingEmojiRecommendationActivityAdapter.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
            final UsingEmojiRecommendationActivityModel item = aList.get(position);
            if (item != null) {
                try {
                    holder.txtOriginalTitle_Title.setText((item.getOriginal_title()));
                    holder.txtTitle.setText("(" + item.getTitle() + ")");
                    holder.rating.setRating(Float.parseFloat(item.getPopularity()));
                    Picasso.get().load(item.getPoster_path()).into(holder.txtPosterImg);
                    holder.rootLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            UsingEmojiRecommendationActivityModel clickItem = aList.get(position);
                            if (clickItem != null) {
                                try {
                                    usingEmojiRecommendationInterFace.getEmojiRecommendedMovieData(clickItem, position);
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
                    sendRequestToServer("&#x1F3AC;");
                }
            });
            Dialog dialog = builder.create();
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}