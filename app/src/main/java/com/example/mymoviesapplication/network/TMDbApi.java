package com.example.mymoviesapplication.network;

import android.support.annotation.ColorLong;

import com.example.mymoviesapplication.model.GenresResponse;
import com.example.mymoviesapplication.model.Movie;
import com.example.mymoviesapplication.model.MoviesResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TMDbApi {

    /**
     *  Get the primary information about a movie
     * @param id id du movie
     * @param apiKEy  clé de l'api
     * @param language  langue du movie
     * @return  movie
     */
    @GET("movie/{movie_id}")
    Call<Movie> getMovie(
            @Path("movie_id") int id,
            @Query("api_key") String apiKEy,
            @Query("language") String language
    );

    /**
     * Get the top rated movies.
     * @param apiKey
     * @param language
     * @param page
     * @return
     */
    @GET("movie/top_rated")
    Call<MoviesResponse> getTopRatedMovies(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page
    );

    /**
     * Get the list of official genres for movies.
     * @param apiKey
     * @param language
     * @return
     */
    @GET("genre/movie/list")
    Call<GenresResponse> getGenres(
            @Query("api_key") String apiKey,
            @Query("language") String language
    );

    /**
     * Get a list of upcoming movies in theatres
     * @param apiKey
     * @param language
     * @param page
     * @return
     */
    @GET("movie/upcoming")
    Call<MoviesResponse> getUpcomingMovies(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page
    );

    /**
     * Get a list of movies in theatres
     * @param apiKey
     * @param language
     * @param page
     * @return
     */
    @GET("movie/now_playing")
    Call<MoviesResponse> getNowPlayingMovies(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page
    );

    /**
     * Get a list of the current popular movies.
     * @param apiKey
     * @param language
     * @param page
     * @return
     */
    @GET("movie/popular")
    Call<MoviesResponse> getPopularMovies(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page
    );
}
