package com.example.mymoviesapplication.database;




import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.example.mymoviesapplication.dao.FavoriteDao;
import com.example.mymoviesapplication.model.FavoriteList;


@Database(entities={FavoriteList.class},version = 1, exportSchema = false)
public abstract class FavoriteDatabase extends RoomDatabase{

    public abstract FavoriteDao favoriteDao();

}