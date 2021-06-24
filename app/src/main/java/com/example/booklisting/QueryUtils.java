package com.example.booklisting;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public final class QueryUtils {



    public static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils() {
    }


    public static ArrayList<book> extractBooks(String JSON_RESPONSE) {

        // Create an empty ArrayList that we can start adding earthquakes to
        ArrayList<book> books = new ArrayList<>();


        try {


            JSONObject jj=new JSONObject(JSON_RESPONSE);

            JSONArray j_array1= jj.optJSONArray("items");
            for(int k=0;k<j_array1.length();k++)
            {
                JSONObject j_obj= j_array1.optJSONObject(k);
                JSONObject obj= j_obj.optJSONObject("volumeInfo");

                String title=obj.getString("title");
                String url=obj.getString("previewLink");
                String imgURL="https://static.wikia.nocookie.net/source-filmmaker/images/a/a7/No_Image.jpg/revision/latest?cb=20201126143924";
                if(obj.has("imageLinks"))
                {
                    JSONObject imglinks=obj.optJSONObject("imageLinks");
                     imgURL=imglinks.getString("thumbnail");
                }

                JSONArray j_array2= obj.optJSONArray("authors");
                String authors="";
                for(int i=0;i<j_array2.length();i++)
                {
                    if(i==j_array2.length()-1)
                     authors+= j_array2.getString(i);
                    else
                    authors+= j_array2.getString(i) + ", ";

                }
                int index=3;
                String ModifiedImgURL=  imgURL.substring(0, index + 1) + "s" + imgURL.substring(index + 1);
                books.add(new book(title,authors,ModifiedImgURL,url));

            }




        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return books;
    }


    /**
     * Query the USGS dataset and return an {@link ArrayList<book>} object to represent a single earthquake.
     */
    public static ArrayList<book> fetchBookData(String requestUrl) {
        // Create URL object


        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }


        ArrayList<book> earthquakes = extractBooks(jsonResponse);


        return earthquakes;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

}
