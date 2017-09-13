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
        return "https://image/tmdb.org/t/p/w342/"+ posterPath;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public String posterPath;
    public String originalTitle;
    public String overview;

    public Movie(JSONObject jsonObject) throws JSONException{
        posterPath =  jsonObject.getString("poster_path");
        originalTitle= jsonObject.getString("original_title");
        overview = jsonObject.getString("overview");
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
