package com.example.rkjc.news_app_2;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class NewsItemRepository {
    private NewsItemDao mNewsItemDao;
    private LiveData<List<NewsItem>> mAllNewsItems;
//    private boolean runOnStartup = true;
    private ProgressBar mProgressBar;
    private static final String TAG = "MainActivity";
    private RecyclerView mRecyclerView;
    private NewsRecyclerViewAdapter mAdapter;
    private ArrayList<NewsItem> newsItems = new ArrayList<>();
    NewsItemRepository(Application application)
    {
        NewsItemRoomDatabase db = NewsItemRoomDatabase.getDatabase(application.getApplicationContext());
        mNewsItemDao = db.newsItemDao();
        mAllNewsItems = mNewsItemDao.loadAllNewsItems();
    }


    LiveData<List<NewsItem>> loadAllNewsItems(){
        // Do this in a separate async task
        return mAllNewsItems;
    }


    private URL makeNewsSearchQuery() {
        URL newsSearchUrl = NetworkUtils.buildUrl();
        String urlString = newsSearchUrl.toString();
        Log.d("mycode", urlString);
        return newsSearchUrl;
    }

    public void insert(List<NewsItem> items)
    {
//        if(runOnStartup) {
//            mNewsItemDao.clearAll();
            URL url = makeNewsSearchQuery();
            NewsQueryTask task = new NewsQueryTask(mNewsItemDao);
            task.execute(url);
//            runOnStartup = false;
//        }
    }

//    public static class insertAsyncTask extends AsyncTask<NewsItem, Void, Void> {
//
//        private NewsItemDao mAsyncTaskDao;
//
//        insertAsyncTask(NewsItemDao dao)
//        {
//            mAsyncTaskDao = dao;
//        }
//
//        @Override
//        protected Void doInBackground(final NewsItem... params) {
//            mAsyncTaskDao.insert(params[0]);
//            return null;
//        }
//    }
    public class NewsQueryTask extends AsyncTask<URL, Void, String>
    {
        private NewsItemDao mAsyncTaskDao;

        NewsQueryTask(NewsItemDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(URL... urls) {
            String newsSearchResults = "";
            try {
                newsSearchResults = NetworkUtils.getResponseFromHttpUrl(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return newsSearchResults;
        }

        @Override
        protected void onPostExecute(String newsSearchResults) {
            Log.d("mycode", newsSearchResults);
            super.onPostExecute(newsSearchResults);
            mProgressBar.setVisibility(View.GONE);
            newsItems = JsonUtils.parseNews(newsSearchResults);
            mAsyncTaskDao.insert(newsItems);
            mAdapter.mNewsItems.addAll(newsItems);
            mAdapter.notifyDataSetChanged();
        }

    }


}
