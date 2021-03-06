package com.codepathassignment.nytimessearch;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Process;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

import static com.codepathassignment.nytimessearch.FilterActivity.prefs;

public class SearchActivity extends AppCompatActivity {

    EditText etQuery;
    GridView gvResults;
    Button btnSearch;

    ArrayList<Article> articles;
    ArticalArrayAdapter adapter;
    EndlessScrollListener listener;

    private Boolean isNetworkAvailable(){
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo!= null && activeNetworkInfo.isConnectedOrConnecting();
    }

    public boolean isOnline(){
        Runtime runtime = Runtime.getRuntime();
        try{
            java.lang.Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int exitValue = ipProcess.waitFor();
            return (exitValue ==0);
        }catch (IOException e ){e.printStackTrace();}
        catch (InterruptedException e){e.printStackTrace();};
            return false;
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if ((!isNetworkAvailable())||!(isOnline())){
            Toast.makeText(this,"Network is not available, please retry later", Toast.LENGTH_LONG).show();
            finish();
        }

        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupViews();


    }

    private void setupViews() {
        etQuery = (EditText)findViewById(R.id.etQuery);
        gvResults = (GridView )findViewById(R.id.gvResults);
        btnSearch = (Button)findViewById(R.id.btnSearch);
        articles = new ArrayList<>();
        adapter = new ArticalArrayAdapter(this, articles);
        gvResults.setAdapter(adapter);

        gvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getApplicationContext(),ArticleActivity.class);
                Article article = articles.get(position);

                i.putExtra("article",article);
                startActivity(i);


            }
        });

        listener = new EndlessScrollListener(){

            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                loadNextDataFromApi(page);
                return true;//ONLY if more data is actually being loaded; false otherwise.
            }



        };

        gvResults.setOnScrollListener(listener);




    }

    private void loadNextDataFromApi(int page) {
        String query = etQuery.getText().toString();
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "https://api.nytimes.com/svc/search/v2/articlesearch.json";
        RequestParams params = new RequestParams();
        params.put("api-key", "3b6b4e610df64ae0ae10d5db3828816c");
        params.put("page",page);
        params.put("q",query);

        SharedPreferences prefs=this.getSharedPreferences("store", Context.MODE_PRIVATE);
        params.put("sort",prefs.getString("sortOrder","oldest"));



        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        String beginDate = prefs.getString("beginDate","1969-01-01").replace("-","");
        params.put("begin_date",beginDate);

        String newDesk =getNewsDesk();
        if (newDesk!= null) {
            params.put("fq", newDesk);
        }


        client.get(url,params,new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        EndlessScrollListener.httpFailure=false;
                        //super.onSuccess(statusCode, headers, response);
                        Log.d("DEBUG", response.toString());
                        JSONArray articleJsonResults = null;

                        try{
                            articleJsonResults = response.getJSONObject("response").getJSONArray("docs");
                            articles.addAll(Article.fromJSONArray(articleJsonResults));
                            adapter.notifyDataSetChanged();

                            int numberArticle = articleJsonResults.length();
                            Log.d("DEBUG", "numberArtial found:"+ numberArticle);
                            if (numberArticle>0) {
                                EndlessScrollListener.loading = false;
                            }
                            //Log.d("DEBUG",articles.toString());
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        throwable.printStackTrace();
                        Log.d("DEBUG",errorResponse.toString());
                        EndlessScrollListener.loading = false;
                        EndlessScrollListener.httpFailure=true;
                        //super.onFailure(statusCode, headers, throwable, errorResponse);
                    }
                }

        );

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }else if (id==R.id.action_filter){
            Toast.makeText(this,"filtering",Toast.LENGTH_SHORT).show();
            Intent filterIntent = new Intent(this,FilterActivity.class);
            startActivity(filterIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onArticleSearch(View view) {

        articles.clear();
        adapter.notifyDataSetChanged();
        listener.resetState();
        EndlessScrollListener.loading=true;
        loadNextDataFromApi(0);
    }


    public String getNewsDesk() {
        String news_desk="news_desk:(";
        SharedPreferences prefs=this.getSharedPreferences("store", Context.MODE_PRIVATE);
        Boolean bArts = prefs.getBoolean("bArts",false);
        Boolean bFashion = prefs.getBoolean("bFashion",false);
        Boolean bSports = prefs.getBoolean("bSports",false);
        if (bArts||bFashion||bSports) {
            if (bArts) {
                news_desk += "\"Arts\" ";
            }
            if (bFashion) {

                try {
                    news_desk += "\"" + URLEncoder.encode("Fashion & Style", "UTF-8")+" \"";
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            if (bSports) {
                news_desk += "\"Sports\" ";
            }

            news_desk+=")";

            Log.d("DEBUG",news_desk);
            return news_desk;
        }else{
            //This indicates that no filter is applied.
            return null;
        }

    }
}
