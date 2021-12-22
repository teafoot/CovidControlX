package com.example.covidcontrolx.fragments.home.news;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.covidcontrolx.R;
import com.example.covidcontrolx.fragments.home.stats.Country;
import com.example.covidcontrolx.fragments.home.stats.CountryAdapter;
import com.example.covidcontrolx.utils.CountryUtils;
import com.example.covidcontrolx.utils.CustomVolleyRequest;
import com.example.covidcontrolx.utils.DateUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class NewsFragment extends Fragment {
    private static final String TAG = "NewsFragment";
    RecyclerView rvNews;
    NewsAdapter newsAdapter;
    List<News> newsList = new ArrayList<>();

    public static NewsFragment newInstance() {
        return new NewsFragment();
    }
  
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
  
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View newsFragmentView = inflater.inflate(R.layout.fragment_news, container, false);
//        TextView textView = newsFragmentView.findViewById(R.id.txtNews);
//        textView.setText(""+getArguments().getInt("position")); // from DynamicFragmentAdapter
//        textView.setText("NewsFragment");

        rvNews = newsFragmentView.findViewById(R.id.rvNews);
        rvNews.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvNews.setAdapter(newsAdapter);
        getNewsFromAPI();

        return newsFragmentView;
    }

    private void getNewsFromAPI() {
        String URL = "https://newsapi.org/v2/top-headlines?q=covid&apiKey=a65aaa1ad63d4745aa558ca5b48693a7";
        StringRequest request = new CustomVolleyRequest(Request.Method.GET, URL,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
//                        Log.d(TAG, "getNewsFromAPI onResponse: " + jsonObject.toString());
                        JSONArray articles = jsonObject.getJSONArray("articles");
                        for (int i = 0; i < articles.length(); i++) {
                            JSONObject article = articles.getJSONObject(i);

                            String sourceName = article.getJSONObject("source").getString("name");
                            String author = article.getString("author");
                            String title = article.getString("title");
                            String description = article.getString("description");
                            String url = article.getString("url");
                            String urlToImage = article.getString("urlToImage");
//                            String publishedAt = DateUtils.parseISODate(article.getString("publishedAt"), "yyyy-MM-dd'T'HH:mm:ss.SSSSSSS'Z'"); // doesn't work
                            String publishedAt = article.getString("publishedAt");
                            String content = article.getString("content");

                            News news = new News(sourceName, author, title, description, url, urlToImage, publishedAt, content);
                            newsList.add(news);
                        }
                        if (getActivity() != null) { // add safety
                            newsAdapter = new NewsAdapter(getActivity().getApplicationContext(), newsList);
                            rvNews.setAdapter(newsAdapter);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(request);
    }
}