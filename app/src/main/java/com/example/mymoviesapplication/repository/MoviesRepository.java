package com.example.mymoviesapplication.repository;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.mymoviesapplication.Inter.OnGetRepositoryCallback;
import com.example.mymoviesapplication.model.GenresResponse;
import com.example.mymoviesapplication.model.Movie;
import com.example.mymoviesapplication.model.MoviesResponse;
import com.example.mymoviesapplication.Inter.OnGetGenresCallback;
import com.example.mymoviesapplication.Inter.OnGetMovieCallback;
import com.example.mymoviesapplication.Inter.OnGetMoviesCallback;
import com.example.mymoviesapplication.network.TMDbApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MoviesRepository {

    private static final String TAG = MoviesRepository.class.getSimpleName();

    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    public static final String TMDB_API_KEY ="1e5c997fdc17d0449bbe0a83854fd1ec" ;

    private static final String LANGUAGE = "en-US";


    public static final String POPULAR = "popular";
    public static final String TOP_RATED = "top_rated";
    public static final String UPCOMING = "upcoming";
    public static final String NOW_PLAYING = "now_playing";
    public static final String SEARCH = "search";


    private static MoviesRepository repository;

    private TMDbApi api;

    private MoviesRepository(TMDbApi api) {
        this.api = api;
    }

    public static MoviesRepository getInstance() {
        if (repository == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            repository = new MoviesRepository(retrofit.create(TMDbApi.class));
        }

        return repository;
    }

    public void getMovies(int page, final OnGetMoviesCallback callback) {
        Log.d("MoviesRepository", "Next Page = " + page);
        api.getPopularMovies(TMDB_API_KEY, LANGUAGE, page)
                .enqueue(new Callback<MoviesResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<MoviesResponse> call, @NonNull Response<MoviesResponse> response) {
                        if (response.isSuccessful()) {
                            MoviesResponse moviesResponse = response.body();
                            if (moviesResponse != null && moviesResponse.getMovies() != null) {
                                callback.onSuccess(moviesResponse.getPage(), moviesResponse.getMovies());
                            } else {
                                callback.onError();
                            }
                        } else {
                            callback.onError();
                        }
                    }

                    @Override
                    public void onFailure(Call<MoviesResponse> call, Throwable t) {
                        callback.onError();
                    }
                });
    }

    public void getMovies(int page, String sortBy, final OnGetMoviesCallback callback) {
        Callback<MoviesResponse> call = new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                if (response.isSuccessful()) {
                    MoviesResponse moviesResponse = response.body();
                    if (moviesResponse != null && moviesResponse.getMovies() != null) {
                        callback.onSuccess(moviesResponse.getPage(), moviesResponse.getMovies());
                    } else {
                        callback.onError();
                    }
                } else {
                    callback.onError();
                }
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                callback.onError();
            }
        };

        switch (sortBy) {
            case TOP_RATED:
                api.getTopRatedMovies(TMDB_API_KEY, LANGUAGE, page)
                        .enqueue(call);
                break;
            case UPCOMING:
                api.getUpcomingMovies(TMDB_API_KEY, LANGUAGE, page)
                        .enqueue(call);
                break;
            //
            case NOW_PLAYING:
                api.getNowPlayingMovies(TMDB_API_KEY, LANGUAGE, page)
                        .enqueue(call);
                break;
            //
            case POPULAR:
            default:
                api.getPopularMovies(TMDB_API_KEY, LANGUAGE, page)
                        .enqueue(call);
                break;
        }
    }

    /**
     * recupere les generes de movie
     *
     * @param callback
     */
    public void getGenres(final OnGetGenresCallback callback) {
        api.getGenres(TMDB_API_KEY, LANGUAGE)
                .enqueue(new Callback<GenresResponse>() {
                    @Override
                    public void onResponse(Call<GenresResponse> call, Response<GenresResponse> response) {
                        if (response.isSuccessful()) {
                            GenresResponse genresResponse = response.body();
                            if (genresResponse != null && genresResponse.getGenres() != null) {
                                callback.onSuccess(genresResponse.getGenres());
                            } else {
                                callback.onError();
                            }
                        } else {
                            callback.onError();
                        }
                    }

                    @Override
                    public void onFailure(Call<GenresResponse> call, Throwable t) {
                        callback.onError();
                    }
                });

    }

    /**
     * get DETAIL OF MOVIE BY ID
     *
     * @param movieId
     * @param callback
     */
    public void getMovie(int movieId, final OnGetMovieCallback callback) {
        api.getMovie(movieId, TMDB_API_KEY, LANGUAGE)
                .enqueue(new Callback<Movie>() {
                    @Override
                    public void onResponse(Call<Movie> call, Response<Movie> response) {
                        if (response.isSuccessful()) {
                            Movie movie = response.body();
                            if (movie != null) {
                                callback.onSuccess(movie);
                            } else {
                                callback.onError();
                            }
                        } else {
                            callback.onError();
                        }
                    }

                    @Override
                    public void onFailure(Call<Movie> call, Throwable t) {
                        callback.onError();
                    }
                });
    }

    /**
     * Search for movies
     *
     * @param query
     * @param sortBySearch
     * @param page
     * @param callback
     */
    public void searchMovies(final String query, String sortBySearch, int page, final OnGetRepositoryCallback callback) {

        Callback<MoviesResponse> call = new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                if (response.isSuccessful()) {
                    MoviesResponse moviesResponse = response.body();
                    if (moviesResponse != null && moviesResponse.getMovies() != null) {
                        callback.onSuccess(moviesResponse.getPage(), moviesResponse.getMovies());
                    } else {
                        callback.onError();
                    }
                } else {
                    callback.onError();
                }
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                callback.onError();
            }
        };

        switch (sortBySearch) {
            case SEARCH:
                api.searchTitles(query, TMDB_API_KEY, LANGUAGE, page)
                        .enqueue(call);
                break;
        }
    }

}