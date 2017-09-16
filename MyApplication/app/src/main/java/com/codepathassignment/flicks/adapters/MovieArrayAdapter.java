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

    private static class ViewHolder{
        TextView tvTitle;
        TextView tvOverview;
        ImageView ivImage;

    }

    public MovieArrayAdapter(Context context, List<Movie> movies){
        super(context, R.layout.item_movie,movies);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //get the data item for position;

        Movie movie = getItem(position);

        ViewHolder viewHolder;

        if (convertView == null){

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_movie,parent,false);
            viewHolder.tvTitle = (TextView)convertView.findViewById(R.id.tvTitle);
            viewHolder.tvOverview=(TextView)convertView.findViewById(R.id.tvOverview);
            viewHolder.ivImage =(ImageView)convertView.findViewById(R.id.ivMovieImage);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }



        viewHolder.ivImage.setImageResource(0);


        viewHolder.tvTitle.setText(movie.getOriginalTitle());
        viewHolder.tvOverview.setText(movie.getOverview());

        int orientation = getContext().getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {

            Picasso.with(getContext()).load(movie.getPosterPath()).fit().placeholder(R.drawable.loading).into(viewHolder.ivImage);
        }else if (orientation == Configuration.ORIENTATION_LANDSCAPE){

            Picasso.with(getContext()).load(movie.getBackdropPath()).fit().placeholder(R.drawable.loading_192).into(viewHolder.ivImage);
        }
        return convertView;
    }
}


