package com.example.rkjc.news_app_2;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import java.util.ArrayList;



public class NewsRecyclerViewAdapter extends RecyclerView.Adapter<NewsRecyclerViewAdapter.NewsViewHolder>  {
    Context mContext;
    ArrayList<NewsItem> mNewsItems;

    public NewsRecyclerViewAdapter(Context mContext, ArrayList<NewsItem> mNewsItems) {
        this.mContext = mContext;
        this.mNewsItems = mNewsItems;
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(R.layout.news_item, parent, shouldAttachToParentImmediately);
        NewsViewHolder viewHolder = new NewsViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mNewsItems.size();
    }




    public class NewsViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        TextView description;
        TextView date;

        public NewsViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            description = (TextView) itemView.findViewById(R.id.description);
            date = (TextView) itemView.findViewById(R.id.date);
        }

        void bind(final int listIndex) {
            title.setText("Title: ".concat(mNewsItems.get(listIndex).getTitle()));
            title.setTextColor(Color.rgb(0,0,0));
            description.setText("Description: ".concat(mNewsItems.get(listIndex).getDescription()));
            description.setTextColor(Color.rgb(50, 50, 50));
            date.setText("Date: ".concat(mNewsItems.get(listIndex).getPublishedAt().substring(0, mNewsItems.get(listIndex).getPublishedAt().indexOf("T")).concat("\n")));
            date.setTextColor(Color.rgb(112, 112, 112));
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String urlString = mNewsItems.get(listIndex).getUrl();
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlString));
                    mContext.startActivity(intent);
                }
            });

        }
    }
}
