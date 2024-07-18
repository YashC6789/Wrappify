package com.example.wrappify;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.wrappify.ui.wrapped.WrappedGenerator;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SongService {
    private RequestQueue queue;
    private ArrayList<Song> songs = new ArrayList<>();
    private ArrayList<Artist> artists = new ArrayList<>();

    public SongService(Context context) {
        queue = Volley.newRequestQueue(context);
    }

    public ArrayList<Song> getTopSongs(final VolleyCallBack callBack) {
        songs = new ArrayList<>();
        String timeframe = (String) WrappedGenerator.timeframeSpinner.getSelectedItem();
        String endpoint = "https://api.spotify.com/v1/me/top/tracks?time_range=";
        switch (timeframe) {
            case "Past Year":
                endpoint += "long_term&limit=5&offset=0";
                break;
            case "Past 6 Months":
                endpoint += "medium_term&limit=5&offset=0";
                break;
            default:
                endpoint += "short_term&limit=5&offset=0";
                break;
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, endpoint, null, response -> {
                    Gson gson = new Gson();
                    JSONArray jsonArray = response.optJSONArray("items");
                    try {
                        for (int i = 0; i < 5; i++) {
                            JSONObject trackObject = jsonArray.getJSONObject(i);
                            Song song = gson.fromJson(trackObject.toString(), Song.class);
                            JSONObject albumObject = trackObject.optJSONObject("album");
                            JSONArray images = albumObject.optJSONArray("images");
                            String url = images.getJSONObject(0).optString("url");
                            song.setUrl(url);
                            songs.add(song);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    callBack.onSuccess();
                }, error -> {
                    Log.e("Error in JSON", error.toString());
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String token = LoginActivity.mAccessToken;
                String auth = "Bearer " + token;
                headers.put("Authorization", auth);
                return headers;
            }
        };
        queue.add(jsonObjectRequest);
        return songs;
    }

    public ArrayList<Artist> getTopArtists(final VolleyCallBack callBack) {
        artists = new ArrayList<>();
        String timeframe = (String) WrappedGenerator.timeframeSpinner.getSelectedItem();
        String endpoint = "https://api.spotify.com/v1/me/top/artists?time_range=";
        switch (timeframe) {
            case "Past Year":
                endpoint += "long_term&limit=5&offset=0";
                break;
            case "Past 6 Months":
                endpoint += "medium_term&limit=5&offset=0";
                break;
            default:
                endpoint += "short_term&limit=5&offset=0";
                break;
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, endpoint, null, response -> {
                    Gson gson = new Gson();
                    JSONArray jsonArray = response.optJSONArray("items");
                    try {
                        for (int i = 0; i < 5; i++) {
                            JSONObject artistObject = jsonArray.getJSONObject(i);
                            Artist artist = gson.fromJson(artistObject.toString(), Artist.class);
                            JSONArray images = artistObject.optJSONArray("images");
                            String url = images.getJSONObject(0).optString("url");
                            artist.setUrl(url);
                            artists.add(artist);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    callBack.onSuccess();
                }, error -> {
                    Log.e("SongService", error.toString());
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String token = LoginActivity.mAccessToken;
                String auth = "Bearer " + token;
                headers.put("Authorization", auth);
                return headers;
            }
        };
        queue.add(jsonObjectRequest);
        return artists;
    }

    public ArrayList<Song> getSongs() {
        return songs;
    }
    public ArrayList<Artist> getArtists() {
        return artists;
    }

}