package com.example.rkjc.news_app_2;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

@Database(entities = {NewsItem.class}, version = 1)
public abstract class NewsItemRoomDatabase extends RoomDatabase {
    public abstract NewsItemDao newsItemDao();
    public ArrayList<NewsItem> newsItems = new ArrayList<>();

    private static NewsItemRoomDatabase.Callback sRoomDatabaseCallback =
            new NewsItemRoomDatabase.Callback(){

        @Override
                public void onOpen(@NonNull SupportSQLiteDatabase db)
        {
            super.onOpen(db);
//            URL newsSearchUrl = NetworkUtils.buildUrl();
            new PopulateDbAsync(INSTANCE).execute();
        }

    };

    private static volatile NewsItemRoomDatabase INSTANCE;

    static NewsItemRoomDatabase getDatabase(final Context context)
    {
        if (INSTANCE == null) {
            synchronized (NewsItemRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            NewsItemRoomDatabase.class, "newsitem_database")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;

    }

    private static class PopulateDbAsync extends AsyncTask<URL, Void, String> {

        private final NewsItemDao mDao;
        public PopulateDbAsync(NewsItemRoomDatabase db) {
            mDao = db.newsItemDao();
        }

        @Override
        protected String doInBackground(URL... urls) {
            mDao.clearAll();
//            NewsItem newsitem1 = new NewsItem("Hello World", "This is a description", "www.google.com", "2018-11-23");
//            NewsItem newsitem2 = new NewsItem("Hello World 2", "This is a description", "www.google.com", "2018-11-23");
//            ArrayList newslist = new ArrayList();
//            newslist.add(newsitem1);
//            newslist.add(newsitem2);
//            String newsSearchResults = "";
//            try {
//                newsSearchResults = NetworkUtils.getResponseFromHttpUrl(urls[0]);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return newsSearchResults;

            try {
                mDao.insert(JsonUtils.parseNews(NetworkUtils.getResponseFromHttpUrl(NetworkUtils.buildUrl())));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
//
//        @Override
//        protected void onPostExecute(String newsSearchResults) {
//            Log.d("mycode", newsSearchResults);
//            super.onPostExecute(newsSearchResults);
////            mProgressBar.setVisibility(View.GONE);
//            mDao.insert(JsonUtils.parseNews(newsSearchResults));
////            mAdapter.mNewsItems.addAll(newsItems);
////            mAdapter.notifyDataSetChanged();
//        }


    }



}
