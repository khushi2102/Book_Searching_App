package com.example.booklisting;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class BooksAdapter extends ArrayAdapter<book> {

    public BooksAdapter(@NonNull Context context, ArrayList<book> list) {
        super(context,0,list);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        final book currentBook =  getItem(position);

        TextView titleView = (TextView) listItemView.findViewById(R.id.title);
        TextView authorsView = (TextView) listItemView.findViewById(R.id.authors);
        ImageView imgView = (ImageView) listItemView.findViewById(R.id.book_preview);

        titleView.setText(currentBook.getTitle());
        authorsView.setText(currentBook.getAuthors());
       // Log.i(TAG, "getView: ADAPTER "+currentBook.getImgURL() );

        Picasso.get().load(currentBook.getImgURL()).into(imgView);

        //URL

        View openLink=(View)listItemView.findViewById(R.id.container);

        openLink.setOnClickListener(new View.OnClickListener()
        {


            public void onClick(View view)
            {
                String url = currentBook.getUrl();

                view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));

            }
        });

        return listItemView;
    }
}

class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {

    private String url;
    private ImageView imageView;

    public ImageLoadTask(String url, ImageView imageView) {
        this.url = url;
        this.imageView = imageView;
    }

    @Override
    protected Bitmap doInBackground(Void... params) {
        try {
            URL urlConnection = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) urlConnection
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        super.onPostExecute(result);
        imageView.setImageBitmap(result);
    }

}