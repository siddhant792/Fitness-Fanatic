package com.sagar.fitnessfanatic.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.sagar.fitnessfanatic.Constants.ApiConstants;
import com.sagar.fitnessfanatic.Constants.UnivDialouge;
import com.sagar.fitnessfanatic.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import static javax.xml.transform.OutputKeys.ENCODING;

public class Read_posts extends AppCompatActivity {

    TextView tv_header,tv_author_time;
    WebView web_view;
    private String post_id;
    private Context context;
    private String JSON_URL;
    private UnivDialouge progress;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_posts);
        context = this;
        tv_header = findViewById(R.id.tv_header);
        tv_author_time = findViewById(R.id.tv_author_time);
        web_view = findViewById(R.id.web_view);
        progress = new UnivDialouge(Read_posts.this);

        post_id = getIntent().getStringExtra("post_id");

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

//        RequestConfiguration configuration = new RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList("D73F5DC57977D8638C209DD0DD0898BC")).build();
//        MobileAds.setRequestConfiguration(configuration);

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        JSON_URL = "https://www.googleapis.com/blogger/v3/blogs/"+ ApiConstants.BLOG_ID +"/posts/"+post_id+"?key="+ApiConstants.API_KEY;

        web_view.getSettings().setJavaScriptEnabled(true);
        web_view.getSettings().setDomStorageEnabled(true);
        web_view.setWebViewClient(new WebViewClient());
        web_view.setWebChromeClient(new WebChromeClient());
        web_view.getSettings().setTextSize(WebSettings.TextSize.SMALLER);
        loadPostData();

    }

    private void loadPostData() {

        progress.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET,JSON_URL,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String title = jsonObject.getString("title");
                    String published = jsonObject.getString("published");
                    String content = jsonObject.getString("content");
                    String url = jsonObject.getString("url");
                    String displayName = jsonObject.getJSONObject("author").getString("displayName");

                    String gmtDate = published;
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd/MM/yyyy K:mm a");
                    String formattedDate = "";
                    try {
                        Date date = dateFormat.parse(gmtDate);
                        formattedDate = dateFormat2.format(date);

                    } catch (Exception e) {
                        formattedDate = published;
                        e.printStackTrace();
                    }

                    tv_header.setText(title);
                    tv_author_time.setText("By "+displayName+" "+formattedDate);
                    web_view.loadDataWithBaseURL(null,content,"text/html",ENCODING,null);
                    progress.cancel();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    progress.cancel();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Unexpected Error", Toast.LENGTH_SHORT).show();
                progress.cancel();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

    }
}