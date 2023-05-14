package com.binarybrainiac.moviegenie.ModelClass;

public class UsingTitleRecommendationActivityModel {
    String adult;
    String backdrop_path;
    String genre_ids;
    String original_language;
    String original_title;
    String overview;
    String popularity;
    String poster_path;
    String release_date;
    String title;
    String video;
    String vote_average;
    String vote_count;
    String movie_id;
    String imdb_id;

    public UsingTitleRecommendationActivityModel() {
    }

    public UsingTitleRecommendationActivityModel(String adult, String backdrop_path, String genre_ids, String original_language, String original_title, String overview, String popularity, String poster_path, String release_date, String title, String video, String vote_average, String vote_count, String movie_id, String imdb_id) {
        this.adult = adult;
        this.backdrop_path = backdrop_path;
        this.genre_ids = genre_ids;
        this.original_language = original_language;
        this.original_title = original_title;
        this.overview = overview;
        this.popularity = popularity;
        this.poster_path = poster_path;
        this.release_date = release_date;
        this.title = title;
        this.video = video;
        this.vote_average = vote_average;
        this.vote_count = vote_count;
        this.movie_id = movie_id;
        this.imdb_id = imdb_id;
    }

    public String getAdult() {
        return adult;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public String getGenre_ids() {
        return genre_ids;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public String getOverview() {
        return overview;
    }

    public String getPopularity() {
        return popularity;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public String getRelease_date() {
        return release_date;
    }

    public String getTitle() {
        return title;
    }

    public String getVideo() {
        return video;
    }

    public String getVote_average() {
        return vote_average;
    }

    public String getVote_count() {
        return vote_count;
    }

    public String getMovie_id() {
        return movie_id;
    }

    public String getImdb_id() {
        return imdb_id;
    }
}
