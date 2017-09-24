package com.codepathassignment.nytimessearch;

import android.widget.AbsListView;
import android.widget.GridView;

/**
 * Created by qunli on 9/23/17.
 */

public abstract class EndlessScrollListener implements AbsListView.OnScrollListener {

    private int visibleThreshold = 50;
    private int currentPage =0;

    private int previousTotalItemCount =0;
    private boolean loading =true;

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
        if (totalItemCount < previousTotalItemCount) {
            this.currentPage = this.startingPageIndex;
            this.previousTotalItemCount = totalItemCount;
            if (totalItemCount == 0) {
                this.loading = true;
            }
        }

        if (loading && (totalItemCount > previousTotalItemCount)) {
            loading = false;
            previousTotalItemCount = totalItemCount;
        }

        if (!loading && (firstVisibleItem + visibleItemCount + visibleThreshold) > totalItemCount) {
            currentPage++;
            loading = onLoadMore(currentPage, totalItemCount);
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
