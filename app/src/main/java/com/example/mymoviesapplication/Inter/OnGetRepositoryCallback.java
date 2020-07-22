package com.example.mymoviesapplication.Inter;

import com.example.mymoviesapplication.model.Movie;

import java.util.List;


public interface OnGetRepositoryCallback {

    void onSuccess(int page,List<Movie> movies);
    void onError();
}
