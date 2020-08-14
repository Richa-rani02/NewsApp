package welcome.example.newsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Adapter.newsAdapter;
import model.news_model;
import model.sources_model;

public class MainActivity extends AppCompatActivity {
    private TabLayout tab_cat;
    private List<sources_model> sources_modelList = new ArrayList<>();
    private RecyclerView rv_news;
    private List<String> cat_id = new ArrayList<>();
    private static final String source_url = "https://newsapi.org/v2/sources?language=en&apiKey=18fc246ac7524256b55ebb2b7257dc29";
    private String article_url;
    String geturl;
    private ShimmerFrameLayout ShimmerViewContainer;
    private List<news_model> news_modelList = new ArrayList<>();
    private newsAdapter adapter_news;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ShimmerViewContainer =findViewById(R.id.shimmer_container);
        tab_cat = findViewById(R.id.tab_cat);
        rv_news = findViewById(R.id.rv_cat);
        rv_news.setHasFixedSize(true);
        rv_news.setLayoutManager(new LinearLayoutManager(this));
        adapter_news = new newsAdapter(news_modelList);
        adapter_news.notifyDataSetChanged();
        rv_news.setAdapter(adapter_news);
        getCategoryRequest();

        tab_cat.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String getcat_id = cat_id.get(tab.getPosition());
                article_url = "http://newsapi.org/v2/top-headlines?language=en&sources=" + getcat_id + "&apiKey=18fc246ac7524256b55ebb2b7257dc29";
                getnews(getcat_id);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        rv_news.addOnItemTouchListener(new RecyclerTouchListener(this, rv_news, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                geturl=news_modelList.get(position).getUrl();
                Intent intent = new Intent(MainActivity.this, Detail.class);
                intent.putExtra("url", geturl);
                startActivity(intent);

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

    }

    private void getnews(final String getcat_id) {
        StringRequest stringRequest1 = new StringRequest(Method.GET, article_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("", ">>" + response);
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.optString("status").equals("ok")) {


                        JSONArray dataarray = obj.getJSONArray("articles");
                        news_modelList.clear();
                        for (int i = 0; i < dataarray.length(); i++) {
                            news_model news = new news_model();
                            JSONObject dataobj = dataarray.getJSONObject(i);
                            news.setAuthor(dataobj.getString("author"));
                            news.setTitle(dataobj.getString("title"));
                            news.setDescription(dataobj.getString("description"));
                            news.setUrl(dataobj.getString("url"));
                            news.setUrlToImage(dataobj.getString("urlToImage"));
                            news.setPublishedAt(dataobj.getString("publishedAt"));
                            news_modelList.add(news);
                            adapter_news.notifyDataSetChanged();
                        }




                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("tag", "error" + error);
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(stringRequest1);
    }

    private void getCategoryRequest() {
        StringRequest stringRequest = new StringRequest(Method.GET, source_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("source", ">>" + response);
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.optString("status").equals("ok")) {


                        sources_modelList.clear();
                        JSONArray dataarray = obj.getJSONArray("sources");
                        for (int i = 0; i < dataarray.length(); i++) {
                            sources_model cat = new sources_model();
                            JSONObject dataobj = dataarray.getJSONObject(i);
                            cat.setId(dataobj.getString("id"));
                            cat.setName(dataobj.getString("name"));

                            sources_modelList.add(cat);
                        }
                        cat_id.clear();
                        for (int j = 0; j < sources_modelList.size(); j++) {
                            cat_id.add(sources_modelList.get(j).getId());
                            tab_cat.addTab(tab_cat.newTab().setText(sources_modelList.get(j).getName()));
                            ShimmerViewContainer.stopShimmer();
                            ShimmerViewContainer.setVisibility(View.GONE);
                        }


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("tag", "error" + error);
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(stringRequest);
    }
    @Override
    public void onResume() {
        super.onResume();
        ShimmerViewContainer.startShimmer();
    }

    @Override
    public void onPause() {
        ShimmerViewContainer.stopShimmer();
        super.onPause();
    }
}
