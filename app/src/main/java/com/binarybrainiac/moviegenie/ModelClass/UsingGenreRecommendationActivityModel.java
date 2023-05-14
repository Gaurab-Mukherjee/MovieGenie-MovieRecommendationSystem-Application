package com.binarybrainiac.moviegenie.ModelClass;

public class UsingGenreRecommendationActivityModel {
   String adult;
   String backdrop_path;
   String belongs_to_collection;
   String budget;
   String genres;
   String homepage;
   String id;
   String imdb_id;
   String original_language;
   String original_title;
   String overview;
   String popularity;
   String poster_path;
   String release_date;
   String revenue;
   String runtime;
   String title;
   String vote_average;
   String vote_count;

   public UsingGenreRecommendationActivityModel() {}

   public UsingGenreRecommendationActivityModel(String adult, String backdrop_path, String belongs_to_collection, String budget, String genres, String homepage, String id, String imdb_id, String original_language, String original_title, String overview, String popularity, String poster_path, String release_date, String revenue, String runtime, String title, String vote_average, String vote_count) {
      this.adult = adult;
      this.backdrop_path = backdrop_path;
      this.belongs_to_collection = belongs_to_collection;
      this.budget = budget;
      this.genres = genres;
      this.homepage = homepage;
      this.id = id;
      this.imdb_id = imdb_id;
      this.original_language = original_language;
      this.original_title = original_title;
      this.overview = overview;
      this.popularity = popularity;
      this.poster_path = poster_path;
      this.release_date = release_date;
      this.revenue = revenue;
      this.runtime = runtime;
      this.title = title;
      this.vote_average = vote_average;
      this.vote_count = vote_count;
   }

   public String getAdult() {
      return adult;
   }

   public String getBackdrop_path() {
      return backdrop_path;
   }

   public String getBelongs_to_collection() {
      return belongs_to_collection;
   }

   public String getBudget() {
      return budget;
   }

   public String getGenres() {
      return genres;
   }

   public String getHomepage() {
      return homepage;
   }

   public String getId() {
      return id;
   }

   public String getImdb_id() {
      return imdb_id;
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

   public String getRevenue() {
      return revenue;
   }

   public String getRuntime() {
      return runtime;
   }

   public String getTitle() {
      return title;
   }

   public String getVote_average() {
      return vote_average;
   }

   public String getVote_count() {
      return vote_count;
   }
}
