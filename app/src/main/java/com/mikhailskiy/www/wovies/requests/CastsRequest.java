package com.mikhailskiy.www.wovies.requests;

import android.net.Uri;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.mikhailskiy.www.wovies.helpers.Constants;
import com.mikhailskiy.www.wovies.helpers.States;
import com.mikhailskiy.www.wovies.managers.AppController;
import com.mikhailskiy.www.wovies.managers.RequestManager;
import com.mikhailskiy.www.wovies.models.Cast;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * Created by Mikhail Valuyskiy on 07.09.2015.
 */
public class CastsRequest implements RequestFactory {

    //region Keys for building query
    private final String BASE_URL = "http://api.themoviedb.org/3/movie/";
    private final String CASTS = "casts";
    private final String API_KEY = "api_key";
    //endregion

    private static RequestManager manager_;

    public CastsRequest(RequestManager manager){
        this.manager_ = manager;
    }

    public void postRequest(long id){
        String url = createCastsUrl(id);
        Timber.v("Created Casts Url", url);
        postGetRequest(url);
    }

    // Creates url for casts request
    private String createCastsUrl(long id){
        String url = null;
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendEncodedPath(Long.toString(id))
                .appendEncodedPath(CASTS)
                .appendQueryParameter(API_KEY,getApiKey())
                .build();
        Timber.v(builtUri.toString());
        url = builtUri.toString();
        return url;
    }

    // use Volley for sending request
    private void postGetRequest(final String url) {
        String tag = "casts_request";
        // Check if there is saved data in the cache
        // If so, there is no need to send same request, just take it from cache
        // Otherwise, send request
        String response = getDataFromCache(url);
        if (response != null) {
            Timber.v("Data was cashed");
            manager_.sendMessage(manager_.obtainMessage(States.CASTS_REQUEST_COMPLETED, response));
        } else {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Timber.v("Data was not cashed");
                            String responseString = response.toString();
                            manager_.sendMessage(manager_.obtainMessage(States.CASTS_REQUEST_COMPLETED, responseString));
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            String errorMsg = error.getMessage();
                            Timber.e(error.getMessage());
                            manager_.sendMessage(manager_.obtainMessage(States.VOLLEY_REQUEST_FAILED, errorMsg));
                        }
                    });

            AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag);
        }
    }

    private String getDataFromCache(String url) {
        Cache.Entry retrievedData = retrieveDataFromCache(url);
        String data = null;
        if (retrievedData != null) {
            try {
                data = new String(retrievedData.data, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return data;
    }

    private Cache.Entry retrieveDataFromCache(String url) {
        Cache cache = AppController.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get(url);
        return entry;
    }

    public static void getCastsList(String castsResponse){
        List<Cast> casts = getCastsFromJson(castsResponse);
        manager_.sendMessage(manager_.obtainMessage(States.CASTS_REQUEST_WAS_PARSED,casts));
    }

    private static List<Cast> getCastsFromJson(String response){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        Gson gson = gsonBuilder.create();

        Type listType = new TypeToken<List<Cast>>() {
        }.getType();
        List<Cast> casts = new ArrayList<Cast>();

        JsonParser parser = new JsonParser();
        JsonObject jsonObject = parser.parse(response).getAsJsonObject();
        JsonElement resultElement = jsonObject.get("cast");
        casts = (List<Cast>)gson.fromJson(resultElement,listType);
        return casts;
    }

    private String getApiKey() {
        return Constants.API_KEY_VALUE;
    }


}
