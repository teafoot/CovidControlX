package com.example.covidcontrolx.fragments.home.news;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.covidcontrolx.R;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.CountryViewHolder> {
    Context context;
    List<News> newsList;

    public NewsAdapter(Context context, List<News> newsList) {
        this.context = context;
        this.newsList = newsList;
    }

    @Override
    public CountryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_news, parent, false);
        return new CountryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CountryViewHolder holder, int position) {
        String sourceName = newsList.get(position).getSource();
        String author = newsList.get(position).getAuthor();
        String title = newsList.get(position).getTitle();
        String description = newsList.get(position).getDescription();
        String url = newsList.get(position).getUrl();
        String urlToImage = newsList.get(position).getUrlToImage();
        String publishedAt = newsList.get(position).getPublishedAt();
        String content = newsList.get(position).getContent();

        Glide.with(context).load(urlToImage).override(1400, 280).into(holder.newsImage);
        holder.sourceName.setText(sourceName);
        holder.author.setText(author);
        holder.title.setText(title);
        holder.description.setText(description);
        holder.url.setText(url);
        holder.publishedAt.setText(publishedAt);
        holder.content.setText(content);
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    class CountryViewHolder extends RecyclerView.ViewHolder {
        ImageView newsImage;
        TextView sourceName, author, title, description, url, publishedAt, content;

        public CountryViewHolder(View itemView) {
            super(itemView);
            newsImage = itemView.findViewById(R.id.news_image);
            title = itemView.findViewById(R.id.news_title);
            description = itemView.findViewById(R.id.news_description);
            content = itemView.findViewById(R.id.news_content);
            sourceName = itemView.findViewById(R.id.news_source);
            author = itemView.findViewById(R.id.news_author);
            url = itemView.findViewById(R.id.news_url);
            publishedAt = itemView.findViewById(R.id.news_publishedat);
        }
    }
}
