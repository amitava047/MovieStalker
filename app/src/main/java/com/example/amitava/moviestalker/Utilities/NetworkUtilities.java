package com.example.amitava.moviestalker.Utilities;

import android.net.Uri;

import com.example.amitava.moviestalker.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Amitava on 29-Nov-17.
 */

public class NetworkUtilities {

    private static final String TAG = NetworkUtilities.class.getSimpleName();
    private static final String MOVIE_DB_URL = "https://api.themoviedb.org/3/movie/";
    //private static final String PARAM_QUERY = "?";
    private static final String PARAM_SORT = "/";
    //private static final String PARAM_CONCAT = "&";
    private static final String PARAM_API = "api_key";
    private static final String API = BuildConfig.MY_MOVIE_DB_API_KEY;


    public static URL buildUrl(String movieDbSearchQuery){
        Uri uri = Uri.parse(MOVIE_DB_URL).buildUpon()
                .appendPath(movieDbSearchQuery)
                .appendQueryParameter(PARAM_API,API)
                .build();
        URL url = null;
        try{
            url = new URL(uri.toString());
        }catch (MalformedURLException e){
            e.printStackTrace();
        }
        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException{
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        }
        finally {
            urlConnection.disconnect();
        }
    }
}
