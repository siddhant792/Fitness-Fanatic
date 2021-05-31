package com.sagar.fitnessfanatic.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sagar.fitnessfanatic.Adapters.Adapter_diet_posts;
import com.sagar.fitnessfanatic.Constants.ApiConstants;
import com.sagar.fitnessfanatic.Models.Model_diet_posts;
import com.sagar.fitnessfanatic.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Diet extends Fragment {

    Adapter_diet_posts ap_premium,ap_food_drinks,ap_health_fitness,ap_weight_loss,ap_recipes;
    ArrayList<Model_diet_posts> premium_plans = new ArrayList<>();
    ArrayList<Model_diet_posts> food_drinks = new ArrayList<>();
    ArrayList<Model_diet_posts> health_fitness = new ArrayList<>();
    ArrayList<Model_diet_posts> weight_loss = new ArrayList<>();
    ArrayList<Model_diet_posts> recipes_list = new ArrayList<>();
    RecyclerView rv_premium_plans,rv_food_drinks,rv_health_fitness,rv_weight_loss,rv_recipes;

    private String JSON_URL = "https://www.googleapis.com/blogger/v3/blogs/"+ ApiConstants.BLOG_ID+"/posts?maxResults=50&key="+ ApiConstants.API_KEY;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_diet, container, false);
        rv_premium_plans = view.findViewById(R.id.rv_diet_plan);
        rv_food_drinks = view.findViewById(R.id.rv_food_and_drinks);
        rv_health_fitness = view.findViewById(R.id.rv_health_and_fitness);
        rv_weight_loss = view.findViewById(R.id.rv_weight_loss);
        rv_recipes = view.findViewById(R.id.rv_recipes);
        callApi();
        return view;
    }

    private void callApi() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET,JSON_URL,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("MyApi_Res",response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("items");
                    System.out.println("Size_Resp" + jsonArray.length());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObjectData = jsonArray.getJSONObject(i);
                        JSONArray label_array = jsonObjectData.getJSONArray("labels");
                        String labels = label_array.getString(0);
                        String title = jsonObjectData.getString("title");
                        String post_id = jsonObjectData.getString("id");
                        String content = jsonObjectData.getString("content");
                        int start_index = content.indexOf("src=\"")+5;
                        int end_index = content.indexOf("\"",start_index);
                        String img_url = content.substring(start_index,end_index);
                        if (labels.equalsIgnoreCase("premium")){
                            premium_plans.add(new Model_diet_posts(title,img_url,post_id));
                        }else if (labels.equalsIgnoreCase("food")){
                            food_drinks.add(new Model_diet_posts(title,img_url,post_id));
                        }else if (labels.equalsIgnoreCase("health")){
                            health_fitness.add(new Model_diet_posts(title,img_url,post_id));
                        }else if (labels.equalsIgnoreCase("weight")){
                            weight_loss.add(new Model_diet_posts(title,img_url,post_id));
                        }else if (labels.equalsIgnoreCase("recipe")){
                            recipes_list.add(new Model_diet_posts(title,img_url,post_id));
                        }
                    }
                    getDietPosts();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);

    }

    private void getDietPosts() {

        System.out.println("Array_Size" + premium_plans.size() + "   " + food_drinks.size() + "   " + health_fitness.size() +"  " + weight_loss.size() +"   " + recipes_list.size());

        ap_premium = new Adapter_diet_posts(getContext(),premium_plans);
        rv_premium_plans.setLayoutManager(new GridLayoutManager(getContext(),1,GridLayoutManager.HORIZONTAL,false));
        rv_premium_plans.setAdapter(ap_premium);

        ap_food_drinks = new Adapter_diet_posts(getContext(),food_drinks);
        rv_food_drinks.setLayoutManager(new GridLayoutManager(getContext(),1,GridLayoutManager.HORIZONTAL,false));
        rv_food_drinks.setAdapter(ap_food_drinks);

        ap_health_fitness = new Adapter_diet_posts(getContext(),health_fitness);
        rv_health_fitness.setLayoutManager(new GridLayoutManager(getContext(),1,GridLayoutManager.HORIZONTAL,false));
        rv_health_fitness.setAdapter(ap_health_fitness);

        ap_weight_loss = new Adapter_diet_posts(getContext(),weight_loss);
        rv_weight_loss.setLayoutManager(new GridLayoutManager(getContext(),1,GridLayoutManager.HORIZONTAL,false));
        rv_weight_loss.setAdapter(ap_weight_loss);

        ap_recipes = new Adapter_diet_posts(getContext(),recipes_list);
        rv_recipes.setLayoutManager(new GridLayoutManager(getContext(),1,GridLayoutManager.HORIZONTAL,false));
        rv_recipes.setAdapter(ap_recipes);

    }

}