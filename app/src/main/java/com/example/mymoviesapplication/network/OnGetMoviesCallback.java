package com.example.mymoviesapplication.network;

import com.example.mymoviesapplication.model.Movie;

import java.util.List;

public interface OnGetMoviesCallback {

    void onSuccess(int page,List<Movie> movies);

    void onError();
}