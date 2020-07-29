package com.example.mymoviesapplication.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.mymoviesapplication.R;
import com.example.mymoviesapplication.model.FavoriteList;

import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder>{
    private String IMAGE_URL = "http://image.tmdb.org/t/p/w500";
    private List<FavoriteList>favoriteLists;
    Context context;
    private OnItemClickListener mListener;


    public interface OnItemClickListener{
        void onDeleteClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;

    }

    public FavoriteAdapter(List<FavoriteList> favoriteLists, Context context) {
        this.favoriteLists = favoriteLists;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.favourites_list,viewGroup,false);
        return new ViewHolder(view,mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.bind(favoriteLists.get(position));
    }

    @Override
    public int getItemCount() {
        return favoriteLists.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        FavoriteList favoriteList;
        TextView releaseDate;
        TextView title;
        TextView rating;
        ImageView poster;
        ImageView fav_btn_delete;


        public ViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            releaseDate = itemView.findViewById(R.id.fitem_movie_release_date);
            title       = itemView.findViewById(R.id.fitem_movie_title);
            rating      = itemView.findViewById(R.id.fitem_movie_rating);
            poster      = itemView.findViewById(R.id.fitem_movie_poster);
            fav_btn_delete = itemView.findViewById(R.id.fitem_movie_fav_btn_delete);

            // suprrimer favorite de la liste des favorites
            fav_btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){

                            listener.onDeleteClick(position);

                        }
                    }

                }
            });

        }


        /**
         * rempler les champs de la liste favories par les informaton recupere par favoritelist
         * @param favoriteList
         */
        public void bind(FavoriteList favoriteList) {

            this.favoriteList = favoriteList;
            title.setText(favoriteList.getTitle());
            releaseDate.setText(favoriteList.getReleaseDate());

            title.setText(favoriteList.getTitle());
            rating.setText(String.valueOf(favoriteList.getRating()));

            if(favoriteList.getPosterPath()==null){
                Glide.with(itemView)
                        .load(R.drawable.empty)
                        .apply(RequestOptions.placeholderOf(R.color.colorAccent))
                        .into(poster);
            }else{
                Glide.with(itemView)
                        .load(IMAGE_URL + favoriteList.getPosterPath())
                        .apply(RequestOptions.placeholderOf(R.color.colorAccent))
                        .into(poster);
            }

        }


    }
}