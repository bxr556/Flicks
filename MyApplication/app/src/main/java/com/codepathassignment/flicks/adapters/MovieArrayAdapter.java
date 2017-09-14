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

    public MovieArrayAdapter(Context context, List<Movie> movies){
        super(context, R.layout.item_movie,movies);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //get the data item for position;

        Movie movie = getItem(position);

        if (convertView == null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_movie,parent,false);
        }


        ImageView ivImage = (ImageView)convertView.findViewById(R.id.ivMovieImage);
        ivImage.setImageResource(0);

        TextView tvTitle = (TextView)convertView.findViewById(R.id.tvTitle);
        TextView tvOverview = ( TextView)convertView.findViewById(R.id.tvOverview);

        tvTitle.setText(movie.getOriginalTitle());
        tvOverview.setText(movie.getOverview());

        int orientation = getContext().getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {

            Picasso.with(getContext()).load(movie.getPosterPath()).into(ivImage);
        }else if (orientation == Configuration.ORIENTATION_LANDSCAPE){

            Picasso.with(getContext()).load(movie.getBackdropPath()).into(ivImage);
        }
        return convertView;
    }
}


