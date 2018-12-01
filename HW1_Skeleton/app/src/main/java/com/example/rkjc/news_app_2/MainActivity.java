package com.example.rkjc.news_app_2;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private RecyclerView mRecyclerView;
    private NewsRecyclerViewAdapter mAdapter;
    private ArrayList<NewsItem> newsItems = new ArrayList<>();
    private NewsItemViewModel mNewsItemViewModel;
    private NewsItemRepository mNewsItemRepository;
    private ProgressBar mProgressBar;
    private static final String JOB_TAG = "my_job_tag";
    private FirebaseJobDispatcher dispatcher;
    private boolean runOnStartup = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
//        Job myJob = dispatcher.newJobBuilder()
//                .setService(MyJobService.class)
//                .setTrigger(Trigger.executionWindow(
//                        10,
//                        10 + 10
//
//                ))
//                .setReplaceCurrent(true)
//                .setTag("my-unique-tag")
//                .build();
//        dispatcher.mustSchedule(myJob);
        mProgressBar = (ProgressBar) findViewById(R.id.progress);
//        new NewsQueryTask(NewsItemRoomDatabase.getDatabase(this)).execute();
        mNewsItemViewModel = ViewModelProviders.of(this).get(NewsItemViewModel.class);
        mRecyclerView = (RecyclerView) findViewById(R.id.news_recyclerview);
        mAdapter = new NewsRecyclerViewAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mNewsItemViewModel.loadAllNewsItems().observe(this, new Observer<List<NewsItem>>() {
            @Override
            public void onChanged(@Nullable final List<NewsItem> newsitems) {
                mAdapter.setNewsItems(newsitems);
            }
        });

    }

    public void startJob(View view)
    {
        Job job = dispatcher.newJobBuilder()
                .setService(MyJobService.class)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setTag(JOB_TAG)
                .setTrigger(Trigger.executionWindow(10, 15))
                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                .setReplaceCurrent(true)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .build();
        dispatcher.mustSchedule(job);
        Toast.makeText(this, "Job scheduled", Toast.LENGTH_SHORT).show();
    }

    public void stopJob(View view)
    {
        dispatcher.cancel(JOB_TAG);
        Toast.makeText(this, "Job cancelled", Toast.LENGTH_SHORT).show();
    }

    private URL makeNewsSearchQuery() {
        mAdapter.mNewsItems.removeAll(newsItems);
        URL newsSearchUrl = NetworkUtils.buildUrl();
        String urlString = newsSearchUrl.toString();
        Log.d("mycode", urlString);
        return newsSearchUrl;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();
        if (itemThatWasClickedId == R.id.action_search) {

            //            URL url = makeNewsSearchQuery();
            new NewsQueryTask(NewsItemRoomDatabase.getDatabase(this)).execute();
//            NewsItemViewModel nivm = NewsItemViewModel.loadAllNewsItems();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class NewsQueryTask extends AsyncTask<Void, Void, Void> {
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
                if(runOnStartup) {
                    newsItems = JsonUtils.parseNews(NetworkUtils.getResponseFromHttpUrl(NetworkUtils.buildUrl()));
                    mainDao.insert(newsItems);
                    runOnStartup = false;
                }
                else{
                                                                                                                                                                                    mainDao.insert(newsItems);
                    mainDao.loadAllNewsItems();
                }
//                mProgressBar.setVisibility(View.GONE);
//                mainDao.loadAllNewsItems();
            } catch (IOException e) {
                e.printStackTrace();
            }

            ;
            return null;
        }
    }


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



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }


}
