package com.example.booklisting;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.ArrayList;

public class BooksLoader extends AsyncTaskLoader<ArrayList<book>> {
    public static final String LOG_TAG = QueryUtils.class.getSimpleName();
    private String Qurl;
    public BooksLoader(Context context, String url) {
        super(context);
        Qurl=url;
    }
    @Override
    protected void onStartLoading() {
        forceLoad();
    }
    @Override
    public ArrayList<book> loadInBackground() {
        if (Qurl == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of earthquakes.
        ArrayList<book> books = QueryUtils.fetchBookData(Qurl);
        return books;

    }


}
