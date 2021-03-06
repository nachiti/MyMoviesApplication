package com.example.mymoviesapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mymoviesapplication.adapter.FavoriteAdapter;
import com.example.mymoviesapplication.model.FavoriteList;

import java.util.List;

import es.dmoral.toasty.Toasty;

public class FavoriteListActivity extends AppCompatActivity {

    private ImageView imageViewEmpty;
    private RecyclerView rv;
    private FavoriteAdapter adapter;
    List<FavoriteList> favoriteLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);
        rv=(RecyclerView)findViewById(R.id.rec);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        imageViewEmpty = findViewById(R.id.imageView_empty);
        getFavData();

        adapter.setOnItemClickListener(new FavoriteAdapter.OnItemClickListener() {
            @Override
            public void onDeleteClick(int position) {
                removeItem(position);

            }
        });

        if (listFavorisIsEmpty(favoriteLists)){
            showListEmpty();
        }else {
            showListNotEmpty();
        }
    }
    /**
     * test se la list des favorites est vide ou non.
     * @param favoriteLists
     * @return true ou false
     */
    public boolean listFavorisIsEmpty(List<FavoriteList> favoriteLists){
        return favoriteLists.isEmpty();
    }

    /**
     * afficher list vide sino il affiche la liste des favorites
     */
    public void showListEmpty(){
        imageViewEmpty.setVisibility(View.VISIBLE);

        rv.setVisibility(View.GONE);

    }
    public void showListNotEmpty(){
        imageViewEmpty.setVisibility(View.GONE);
        rv.setVisibility(View.VISIBLE);
    }
    /**
     * suppression d un favorite de la liste
     * @param position
     */
    public void removeItem(int position){
        MainActivity.favoriteDatabase.favoriteDao().delete(favoriteLists.get(position));
        favoriteLists.remove(position);
        adapter.notifyItemRemoved(position);
        removeSuccess();

        if (listFavorisIsEmpty(favoriteLists)){
            showListEmpty();
        }else {
            showListNotEmpty();
        }
    }

    private void removeSuccess() {
        Toasty.success(this,"Remove Success", Toast.LENGTH_SHORT).show();
    }

    private void listEmpty() {
        Toasty.error(this,"Empty List favorites ", Toast.LENGTH_SHORT).show();
    }

    /**
     * selection des favorites a partire du base donne
     */
    private void getFavData() {

       favoriteLists = MainActivity.favoriteDatabase.favoriteDao().getFavoriteData();
        adapter=new FavoriteAdapter(favoriteLists,getApplicationContext());
        rv.setAdapter(adapter);

    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
