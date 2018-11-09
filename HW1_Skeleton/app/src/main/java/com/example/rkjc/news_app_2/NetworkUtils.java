package com.example.rkjc.news_app_2;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {
    final static String NEWS_BASE_URL = "https://newsapi.org/v1/articles";
    final static String PARAM_SOURCE = "source";
    final static String PARAM_SORTBY = "sortBy";
    final static String source = "the-next-web";
    final static String sortBy = "latest";
    final static String PARAM_APIKEY = "apiKey";
    final static String apiKey = "7f1ea60a84684429a6e2c880131a0c59";
    public static URL buildUrl(){
        Uri builtUri = Uri.parse(NEWS_BASE_URL).buildUpon()
                .appendQueryParameter(PARAM_SOURCE, source)
                .appendQueryParameter(PARAM_SORTBY, sortBy)
                .appendQueryParameter(PARAM_APIKEY, apiKey)
                .build();

        URL buildURL = null;
        try {
            buildURL = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return buildURL;
    }
    public static String getResponseFromHttpUrl(URL url) throws IOException {
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
        } finally {
            urlConnection.disconnect();
        }
    }
}