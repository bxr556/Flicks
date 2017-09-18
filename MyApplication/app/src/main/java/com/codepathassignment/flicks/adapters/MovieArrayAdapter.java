package com.codepathassignment.flicks.adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepathassignment.flicks.R;
import com.codepathassignment.flicks.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by qunli on 9/13/17.
 */

public class MovieArrayAdapter extends ArrayAdapter<Movie> {


    public enum MovieType{
        POPULAR,
        NOT_POPULAR
    }
    private static class ViewHolderPopular{
        ImageView ivImage;

    }

    private static class ViewHolderNotPopular{
        TextView tvTitle;
        TextView tvOverview;
        ImageView ivImage;

    }

    public MovieArrayAdapter(Context context, List<Movie> movies){
        super(context, R.layout.item_movie_not_popular,movies);
    }


    @Override
    public int getViewTypeCount() {
        return MovieType.values().length;
    }

    @Override
    public int getItemViewType(int position) {

        Movie movie = getItem(position);
        if (movie.getVote_average()>7.0f){
            return MovieType.POPULAR.ordinal();
        }else{
            return MovieType.NOT_POPULAR.ordinal();
        }

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //get the data item for position;

        Movie movie = getItem(position);
        int orientation = getContext().getResources().getConfiguration().orientation;

        switch (getItemViewType(position)){
            case 0:
                ViewHolderPopular holderPopular;
                if(convertView == null){
                    LayoutInflater inflater = LayoutInflater.from(getContext());
                    convertView = inflater.inflate(R.layout.item_movie_popular,parent,false);
                    holderPopular = new ViewHolderPopular();
                    holderPopular.ivImage = (ImageView)convertView.findViewById(R.id.ivMovieImage);
                    convertView.setTag(holderPopular);
                }else{
                    holderPopular = (ViewHolderPopular)convertView.getTag();
                }

                if (orientation == Configuration.ORIENTATION_PORTRAIT) {

                    Picasso.with(getContext()).load(movie.getPosterPath()).fit().placeholder(R.drawable.loading).into(holderPopular.ivImage);
                }else if (orientation == Configuration.ORIENTATION_LANDSCAPE){

                    Picasso.with(getContext()).load(movie.getBackdropPath()).fit().placeholder(R.drawable.loading_192).into(holderPopular.ivImage);
                }
                break;

            case 1:
                ViewHolderNotPopular holderNotPopular;
                if(convertView == null){
                    LayoutInflater inflater = LayoutInflater.from(getContext());
                    convertView = inflater.inflate(R.layout.item_movie_not_popular,parent,false);
                    holderNotPopular = new ViewHolderNotPopular();
                    holderNotPopular.ivImage = (ImageView)convertView.findViewById(R.id.ivMovieImage);
                    holderNotPopular.tvTitle = (TextView)convertView.findViewById(R.id.tvTitle) ;
                    holderNotPopular.tvOverview=(TextView)convertView.findViewById(R.id.tvOverview);
                    convertView.setTag(holderNotPopular);
                }else{
                    holderNotPopular = (ViewHolderNotPopular)convertView.getTag();
                }

                if (orientation == Configuration.ORIENTATION_PORTRAIT) {

                    Picasso.with(getContext()).load(movie.getPosterPath()).fit().placeholder(R.drawable.loading).into(holderNotPopular.ivImage);
                }else if (orientation == Configuration.ORIENTATION_LANDSCAPE){

                    Picasso.with(getContext()).load(movie.getBackdropPath()).fit().placeholder(R.drawable.loading_192).into(holderNotPopular.ivImage);
                }
                holderNotPopular.tvTitle.setText(movie.getOriginalTitle());
                holderNotPopular.tvOverview.setText(movie.getOverview());
                break;

            default:
                //Throw exception

        }

        return convertView;
    }
}


