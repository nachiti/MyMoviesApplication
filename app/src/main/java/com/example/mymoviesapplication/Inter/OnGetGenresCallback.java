package com.example.mymoviesapplication.Inter;

import com.example.mymoviesapplication.model.Genre;

import java.util.List;

public interface OnGetGenresCallback {


    void onSuccess(List<Genre> genres);

    void onError();
}
