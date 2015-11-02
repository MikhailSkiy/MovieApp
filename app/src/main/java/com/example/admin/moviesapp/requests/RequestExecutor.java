package com.example.admin.moviesapp.requests;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.admin.moviesapp.helpers.States;
import com.example.admin.moviesapp.managers.AppController;
import com.example.admin.moviesapp.managers.RequestManager;
import com.example.admin.moviesapp.models.Movie;
import com.example.admin.moviesapp.models.network.Response;
import com.example.admin.moviesapp.models.requests.UpdateItemRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import timber.log.Timber;

/**
 * Created by Mikhail Valuyskiy on 23.10.2015.
 */
public class RequestExecutor {


    private static RequestManager manager_;

    public RequestExecutor(RequestManager manager) {
        this.manager_ = manager;
    }

    /**
     * Sends GET request to get list of items
     */
    public void sendGetRequest(final UpdateItemRequest itemRequest) {
        //String tag = "favorite_request";
        String tag =  itemRequest.getTag();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(JsonObjectRequest.Method.GET, itemRequest.createGetUrl(),
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String responseString = response.toString();
                        List<Movie> account = getMoviesFromJson(responseString);
                        manager_.sendMessage(manager_.obtainMessage(itemRequest.getSuccessfulGetRequestStatus(), account));
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String errorMsg = error.getMessage();
                        Timber.e(error.getMessage());
                        manager_.sendMessage(manager_.obtainMessage(States.VOLLEY_REQUEST_FAILED, errorMsg));
                    }
                });
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag);
    }




    /**
     * Sends POST request to mark movie as favorite by given movieId
     */
    public void sendPostRequest(final UpdateItemRequest itemRequest){
        final JSONObject body = new JSONObject();
        try{
            body.put("media_type",itemRequest.getMediaType());
            body.put("media_id",Long.toString(itemRequest.getItemId()));
            body.put(itemRequest.getType(),itemRequest.getOperationFlag());
        } catch (JSONException e){
            e.printStackTrace();
        }

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, itemRequest.createPostUrl(), new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Response markAsFavoriteResponse = getResponseFromJson(response.toString());
                Timber.v("Status code");
                Timber.v(markAsFavoriteResponse.statusCode);
//                if (itemRequest.isRequestSuccesfull(markAsFavoriteResponse.statusCode)) {
                    manager_.sendMessage(manager_.obtainMessage(itemRequest.getSuccessfullPostRequestStatus(),markAsFavoriteResponse));
                //}
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {

            @Override
            public byte[] getBody() {
                return body.toString().getBytes();
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept","application/json");
                params.put("Content-Type", "application/json");
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    /**
     * Returns list of movies from json
     * @param jsonResponse - response from server
     * @return list of Movie objects
     */
    private List<Movie> getMoviesFromJson(String jsonResponse){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        Gson gson = gsonBuilder.create();

        Type listType = new TypeToken<List<Movie>>() {
        }.getType();
        List<Movie> movies = new ArrayList<Movie>();

        JsonParser parser = new JsonParser();
        JsonObject jsonObject = parser.parse(jsonResponse).getAsJsonObject();
        JsonElement resultsElement = jsonObject.get("results");
        movies = (List<Movie>) gson.fromJson(resultsElement, listType);
        return movies;
    }


    /**
     * Parse json result and get status_code and status_message
     * @param json
     * @return session_id
     */
    private Response getResponseFromJson(String json){
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        Response response = new Response();
        response = gson.fromJson(json,Response.class);
        return response;
    }

}
