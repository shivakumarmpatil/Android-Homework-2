package com.example.rkjc.news_app_2;

import android.os.AsyncTask;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import java.io.IOException;

public class MyJobService extends JobService {
    NewsQueryTask task;

    @Override
    public boolean onStartJob(final JobParameters job) {

        task = new NewsQueryTask(NewsItemRoomDatabase.getDatabase(this))
        {
            @Override
                    protected void onPostExecute(){
                jobFinished(job, false);

        }
        };
        task.execute();
//        new MainActivity.NewsQueryTask(NewsItemRoomDatabase.getDatabase(this)).execute();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return true;
    }
    public static abstract class NewsQueryTask extends AsyncTask<Void, Void, Void>
    {
        private final NewsItemDao mainDao;
        public NewsQueryTask(NewsItemRoomDatabase nirm) {
            mainDao = nirm.newsItemDao();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
//            mProgressBar.setVisibility(View.GONE);
//  String newsSearchResults = "";

            mainDao.clearAll();
//            mProgressBar.setVisibility(View.GONE);
            try {
                mainDao.insert(JsonUtils.parseNews(NetworkUtils.getResponseFromHttpUrl(NetworkUtils.buildUrl())));
//                mProgressBar.setVisibility(View.GONE);
            }
            catch (IOException e) {
                e.printStackTrace();
            }

            ;
            return null;
        }

        protected abstract void onPostExecute();


//        @Override
//        protected void onPostExecute() {
//        mProgressBar.setVisibility(View.GONE);
//      Log.d("mycode", newsSearchResults);
//            super.onPostExecute();
//            mProgressBar.setVisibility(View.GONE);
//            mainDao.insert(JsonUtils.parseNews(newsSearchResults));
//            mAdapter.mNewsItems.addAll(newsItems);
//            mAdapter.notifyDataSetChanged();
//        }

    }
}
