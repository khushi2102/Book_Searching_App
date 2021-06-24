package com.example.booklisting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    EditText queryText;
    Button search_button;
    String query;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Get Your Book");
        queryText=(EditText)findViewById(R.id.book_name);
        search_button=(Button)findViewById(R.id.search_button);

        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                query=String.valueOf(queryText.getText());

                Intent myIntent = new Intent(MainActivity.this, SearchedItems.class);
                myIntent.putExtra("key", query); //Optional parameters
                MainActivity.this.startActivity(myIntent);
            }
        });

    }
}