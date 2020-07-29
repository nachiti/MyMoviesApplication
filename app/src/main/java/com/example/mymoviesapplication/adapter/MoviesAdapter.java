package com.example.mymoviesapplication.adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.mymoviesapplication.Inter.OnMoviesClickCallback;
import com.example.mymoviesapplication.MainActivity;
import com.example.mymoviesapplication.R;
import com.example.mymoviesapplication.model.FavoriteList;
import com.example.mymoviesapplication.model.Genre;
import com.example.mymoviesapplication.model.Movie;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import okhttp3.OkHttpClient;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {
    private String IMAGE_URL = "http://image.tmdb.org/t/p/w500";
    private Boolean resultfavoriteBoolean ;
    private List<Genre> allGenres;
    private List<Movie> movies;
    private static  Picasso picasso;

    private OnMoviesClickCallback callback;

    public MoviesAdapter(List<Movie> movies,List<Genre> allGenres,  OnMoviesClickCallback callback) {
        this.callback = callback;
        this.movies = movies;
        this.allGenres = allGenres;

    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
        picasso = new Picasso.Builder(parent.getContext().getApplicationContext())
                .downloader(new OkHttp3Downloader(new OkHttpClient()))
                .build();
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MovieViewHolder holder, int position) {
        final Movie movieList = movies.get(position);
        holder.bind(movies.get(position));

        // for favorite
        if (MainActivity.favoriteDatabase.favoriteDao().isFavorite(movieList.getId())==1) {
            holder.fav_btn.setImageResource(R.drawable.ic_is_favorite);
            System.out.println("white");
        }
        else
            holder.fav_btn.setImageResource(R.drawable.ic_isnot_favorite);

            holder.fav_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FavoriteList favoriteList=new FavoriteList();

                int id=movieList.getId();
                String name=movieList.getTitle();
                String image=movieList.getPosterPath();
                String date=movieList.getReleaseDate();
                Float rating = movieList.getRating();



                favoriteList.setId(id);
                favoriteList.setTitle(name);
                favoriteList.setPosterPath(image);
                favoriteList.setReleaseDate(date);
                favoriteList.setRating(rating);

                if (MainActivity.favoriteDatabase.favoriteDao().isFavorite(id)!=1){
                    holder.fav_btn.setImageResource(R.drawable.ic_is_favorite);
                    MainActivity.favoriteDatabase.favoriteDao().addData(favoriteList);
                    resultfavoriteBoolean =true;


                }else {
                    holder.fav_btn.setImageResource(R.drawable.ic_isnot_favorite);
                    MainActivity.favoriteDatabase.favoriteDao().delete(favoriteList);
                    resultfavoriteBoolean =false;


                }


            }
        });
    }

    public boolean ResultfavoriteBoolean(){
        return resultfavoriteBoolean;
    }


        @Override
    public int getItemCount() {
        return movies.size();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {

        Movie movie;
        TextView releaseDate;
        TextView title;
        TextView rating;
        TextView genres;
        ImageView poster;
        ImageView fav_btn;

        public MovieViewHolder(View itemView) {
            super(itemView);
            releaseDate = itemView.findViewById(R.id.item_movie_release_date);
            title = itemView.findViewById(R.id.item_movie_title);
            rating = itemView.findViewById(R.id.item_movie_rating);
            genres = itemView.findViewById(R.id.item_movie_genre);
            poster = itemView.findViewById(R.id.item_movie_poster);
            fav_btn = itemView.findViewById(R.id.fav_btn);
            

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.onClick(movie);
                }
            });


        }

        /**
         * rempler les champs des movie par les information recuperé
         * @param movie
         */
        public void bind(Movie movie) {

            this.movie = movie;
            releaseDate.setText(movie.getReleaseDate().split("-")[0]);
            title.setText(movie.getTitle());
            rating.setText(String.valueOf(movie.getRating()));
            genres.setText(getGenres(movie.getGenreIds()));
            if(movie.getPosterPath() == null){
                Glide.with(itemView)
                        .load( R.drawable.empty)
                        .apply(RequestOptions.placeholderOf(R.color.colorPrimary))
                        .into(poster);

            }else
            {
                Glide.with(itemView)
                        .load(IMAGE_URL + movie.getPosterPath())
                        .apply(RequestOptions.placeholderOf(R.color.colorPrimary))
                        .into(poster);
            }



        }
        private String getGenres(List<Integer> genreIds) {
            List<String> movieGenres = new ArrayList<>();
            for (Integer genreId : genreIds) {
                for (Genre genre : allGenres) {
                    if (genre.getId() == genreId) {
                        movieGenres.add(genre.getName());
                        break;
                    }
                }
            }
            return TextUtils.join(", ", movieGenres);
        }
    }
    public void appendMovies(List<Movie> moviesToAppend) {
        movies.addAll(moviesToAppend);
        notifyDataSetChanged();
    }

    public void clearMovies() {
        movies.clear();
        notifyDataSetChanged();
    }


}