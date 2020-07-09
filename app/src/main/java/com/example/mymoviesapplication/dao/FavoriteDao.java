package com.example.mymoviesapplication.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.mymoviesapplication.model.FavoriteList;

import java.util.List;

@Dao
public interface FavoriteDao {

    @Insert
    public void addData(FavoriteList favoriteList);

    @Query("select * from favoritelist")
    public List<FavoriteList> getFavoriteData();

    @Query("SELECT EXISTS (SELECT 1 FROM favoritelist WHERE id=:id)")
    public int isFavorite(int id);

    @Delete
    public void delete(FavoriteList favoriteList);


}
