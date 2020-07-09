package com.example.mymoviesapplication.Inter;

import com.example.mymoviesapplication.model.Movie;

public interface OnGetMovieCallback {

    void onSuccess(Movie movie);

    void onError();
}
