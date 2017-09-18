package com.codepathassignment.flicks.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by qunli on 9/13/17.
 */

public class Movie {
    public String getPosterPath() {
        return "https://image.tmdb.org/t/p/w342/"+ posterPath;
    }

    public String getBackdropPath(){
        return "https://image.tmdb.org/t/p/w342/"+ backdropPath;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public float getVote_average() {return vote_average;}

    public String posterPath;
    public String originalTitle;
    public String overview;
    public String backdropPath;
    private float vote_average;

    public Movie(JSONObject jsonObject) throws JSONException{
        posterPath =  jsonObject.getString("poster_path");
        originalTitle= jsonObject.getString("original_title");
        overview = jsonObject.getString("overview");
        backdropPath = jsonObject.getString("backdrop_path");
        vote_average = (float) jsonObject.getDouble("vote_average");
    }

    public static ArrayList<Movie> fromJsonArray (JSONArray array){
        ArrayList<Movie> results = new ArrayList<>();

        for ( int i=0;i<array.length();i++){
            try {
                results.add(new Movie((JSONObject) array.get(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return results;
    }

}
