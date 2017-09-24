package com.codepathassignment.nytimessearch;

import android.util.Log;
import android.widget.AbsListView;
import android.widget.GridView;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;

/**
 * Created by qunli on 9/23/17.
 */

public abstract class EndlessScrollListener implements AbsListView.OnScrollListener {

    private int visibleThreshold = 50;
    private int currentPage =0;

    private int previousTotalItemCount =0;
    public static boolean loading =true;
    public static boolean httpFailure = false;

    private int startingPageIndex =0;

    public EndlessScrollListener(){}

    public EndlessScrollListener(int visibleThreshold){
        this.visibleThreshold=visibleThreshold;
    }

    public EndlessScrollListener(int visibleThreshold,int startPage){
        this.visibleThreshold = visibleThreshold;
        this.startingPageIndex = startPage;
        this.currentPage = startPage;
    }
    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        Log.d("DEBUG","0|firstVisibleItem:" +firstVisibleItem+"|visibleItemCount:"+ visibleItemCount+"|totalItemCount:"+ totalItemCount+ "|loading:"+ loading+"|previousTotalItemCount:"+ previousTotalItemCount );
        if (totalItemCount < previousTotalItemCount) {
            Log.d("DEBUG","1");
            this.currentPage = this.startingPageIndex;
            this.previousTotalItemCount = totalItemCount;
            if (totalItemCount == 0) {
                this.loading = true;
            }
        }

//        if (loading && (totalItemCount > previousTotalItemCount)) {
//            Log.d("DEBUG","2");
//            loading = false;
//            previousTotalItemCount = totalItemCount;
//        }

        if ((totalItemCount > previousTotalItemCount)) {
            Log.d("DEBUG","2");
            previousTotalItemCount = totalItemCount;
        }



        if ( httpFailure || (!loading && (firstVisibleItem + visibleItemCount + visibleThreshold) > totalItemCount)) {
            Log.d("DEBUG","3"+ " page:"+ currentPage + " totalCount:"+ totalItemCount);
            loading=true;
            if(!httpFailure){

                //if not httpfailure, load next page, otherwise , retry current page.
                currentPage++;
            }

            httpFailure=false;


            //The following method will update loading upon successfully load some number of rows.
            onLoadMore(currentPage, totalItemCount);
        }




    }

    public abstract boolean onLoadMore(int page, int totalItemsCount);

    //call this method whenever performing new searches
    public void resetState(){
        this.currentPage = this.startingPageIndex;
        this.previousTotalItemCount =0;
        this.loading = true;
    }
}
