package com.example.rkjc.news_app_2;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class JsonUtils {
    public static ArrayList<NewsItem> parseNews(String JSONString){
        ArrayList<NewsItem> newsItemsList = new ArrayList<>();
        try {
            JSONObject mainJSONObject = new JSONObject(JSONString);
            JSONArray items = mainJSONObject.getJSONArray("articles");

            for(int i = 0; i < items.length(); i++){
                JSONObject item = items.getJSONObject(i);
                newsItemsList.add(new NewsItem(item.getString("title"), item.getString("description"), item.getString("url"), item.getString("publishedAt")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return newsItemsList;
    }
}


