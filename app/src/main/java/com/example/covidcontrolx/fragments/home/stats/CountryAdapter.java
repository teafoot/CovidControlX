package com.example.covidcontrolx.fragments.home.stats;

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

public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.CountryViewHolder> {
    Context context;
    List<Country> countryList;

    public CountryAdapter(Context context, List<Country> countryList) {
        this.context = context;
        this.countryList = countryList;
    }

    @Override
    public CountryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_country, parent, false);
        return new CountryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CountryViewHolder holder, int position) {
        String flag = countryList.get(position).getFlag();
        String infected = countryList.get(position).getInfected();
        String tested = countryList.get(position).getTested();
        String recovered = countryList.get(position).getRecovered();
        String deceased = countryList.get(position).getDeceased();
        String country = countryList.get(position).getCountry();
        String lastUpdated = countryList.get(position).getLastUpdatedApify();

        Glide.with(context).load(flag).override(1400, 280).into(holder.flag);
        holder.country.setText(country);
        holder.infected.setText(infected);
        holder.tested.setText(tested);
        holder.recovered.setText(recovered);
        holder.deceased.setText(deceased);
        holder.lastUpdated.setText(lastUpdated);
    }

    @Override
    public int getItemCount() {
        return countryList.size();
    }

    class CountryViewHolder extends RecyclerView.ViewHolder {
        ImageView flag;
        TextView infected, tested, recovered, deceased, country, moreData, historyData, sourceUrl, lastUpdated;

        public CountryViewHolder(View itemView) {
            super(itemView);
            flag = itemView.findViewById(R.id.news_image);
            country = itemView.findViewById(R.id.news_title);
            infected = itemView.findViewById(R.id.news_description);
            tested = itemView.findViewById(R.id.news_content);
            recovered = itemView.findViewById(R.id.news_source);
            deceased = itemView.findViewById(R.id.news_author);
            lastUpdated = itemView.findViewById(R.id.news_publishedat);
        }
    }
}
