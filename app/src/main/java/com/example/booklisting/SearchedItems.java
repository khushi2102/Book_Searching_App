package com.example.booklisting;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class SearchedItems extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<book>> {
    public static final String LOG_TAG = SearchedItems.class.getName();
    public BooksAdapter adapter;
    public TextView emptyView;
    public ProgressBar progressBar;
    public  String query;
    public String Qurl="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searched_items);
        getSupportActionBar().setTitle("Search Results");
        // calling the action bar
        ActionBar actionBar = getSupportActionBar();

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        query = intent.getStringExtra("key");

        Qurl="https://www.googleapis.com/books/v1/volumes?q=";
        Qurl+=query+"&maxResults=10";

        // Find a reference to the {@link ListView} in the layout
        ListView earthquakeListView = (ListView) findViewById(R.id.list);

        emptyView = (TextView) findViewById(R.id.empty_view);
        earthquakeListView.setEmptyView(emptyView);

        progressBar=(ProgressBar)findViewById(R.id.progress);
        // Create a new {@link ArrayAdapter} of earthquakes
        adapter = new BooksAdapter(this, new ArrayList<book>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(adapter);

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(1, null, this);
        }
        else {
            progressBar.setVisibility(View.GONE);
            emptyView.setText("No internet connectivity");
        }


    }


    @Override
    public Loader<ArrayList<book>> onCreateLoader(int id, Bundle args) {
        return new BooksLoader(this, Qurl);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<book>> loader, ArrayList<book> books) {
        progressBar.setVisibility(View.GONE);
        adapter.clear();

        // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.

        if (books != null && !books.isEmpty()) {
            adapter.addAll(books);
        }
        else
            emptyView.setText("No Books found.");
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<book>> loader) {
        adapter.clear();
    }


}