package com.example.mymoviesapplication.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;


@Entity(tableName="favoritelist")
public class FavoriteList {

    @PrimaryKey
    private int id;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "path")
    private String posterPath;

    @ColumnInfo(name = "date")
    private String releaseDate;

    @ColumnInfo(name = "vote")
    private float rating;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public float getRating() {
        return rating;
    }

    @Override
    public String toString() {
        return "FavoriteList{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", posterPath='" + posterPath + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", rating=" + rating +
                '}';
    }

    public void setRating(float rating) {
        this.rating = rating;
    }



}
