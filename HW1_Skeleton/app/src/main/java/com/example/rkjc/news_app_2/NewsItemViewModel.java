package com.example.rkjc.news_app_2;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

public class NewsItemViewModel extends AndroidViewModel {
    private NewsItemRepository mRepository;
    private LiveData<List<NewsItem>> mAllNewsItems;

    public NewsItemViewModel(Application application) {
        super(application);
        mRepository = new NewsItemRepository(application);
        mAllNewsItems = mRepository.loadAllNewsItems();

    }

    public LiveData<List<NewsItem>> loadAllNewsItems() {
        return mAllNewsItems;
    }

    public void insert(List<NewsItem> items)
    {
        mRepository.insert(items);
    }


}
