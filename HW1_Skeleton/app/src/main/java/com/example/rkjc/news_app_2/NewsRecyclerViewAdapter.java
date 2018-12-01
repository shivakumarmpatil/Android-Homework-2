package com.example.rkjc.news_app_2;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class NewsRecyclerViewAdapter extends RecyclerView.Adapter<NewsRecyclerViewAdapter.NewsViewHolder> {
    Context mContext;
    List<NewsItem> mNewsItems;
//    private NewsItemViewModel newsitemviewmodel;

    public NewsRecyclerViewAdapter(Context mContext) {
        this.mContext = mContext;
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

    void setNewsItems(List<NewsItem> newsitems) {
        mNewsItems = newsitems;
        notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        if (mNewsItems != null)
            return mNewsItems.size();
        else
            return 0;
    }


    public class NewsViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView description;
        TextView date;
        ImageView thumburl;

        public NewsViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            description = (TextView) itemView.findViewById(R.id.description);
            date = (TextView) itemView.findViewById(R.id.date);
            thumburl = (ImageView) itemView.findViewById(R.id.thumburl);
        }

        void bind(final int listIndex) {
//
//            title.setText(mNewsItems.get(listIndex).getTitle());
//            title.setTextColor(Color.rgb(0,0,0));
//            Picasso.get().load(mNewsItems.get(listIndex).getThumburl()).into(thumburl);
////            date.setText("Date: ".concat(mNewsItems.get(listIndex).getPublishedAt().substring(0, mNewsItems.get(listIndex).getPublishedAt().indexOf("T")).concat("\n")));
////            date.setTextColor(Color.rgb(112, 112, 112));
//            description.setText("Description: ".concat(mNewsItems.get(listIndex).getDescription()));
//            description.setTextColor(Color.rgb(50, 50, 50));
//
//

            title.setText(mNewsItems.get(listIndex).getTitle());
            description.setText(mNewsItems.get(listIndex).getDescription());
            date.setText(mNewsItems.get(listIndex).getPublishedAt().substring(0, mNewsItems.get(listIndex).getPublishedAt().indexOf("T")));
            Picasso.get().load(mNewsItems.get(listIndex).getThumburl()).into(thumburl);
            itemView.setOnClickListener(new View.OnClickListener()

            {
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

