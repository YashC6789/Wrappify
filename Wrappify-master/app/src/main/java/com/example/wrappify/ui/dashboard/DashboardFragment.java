package com.example.wrappify.ui.dashboard;

import static com.example.wrappify.LoginActivity.mAccessToken;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import androidx.recyclerview.widget.RecyclerView;

import com.example.wrappify.Artist;
import com.example.wrappify.ArtistAdapter;
import androidx.navigation.Navigation;

import com.example.wrappify.R;
import com.example.wrappify.RecommendService;
import com.example.wrappify.SongService;
import com.example.wrappify.databinding.FragmentDashboardBinding;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DashboardFragment extends Fragment implements View.OnClickListener{


    private final OkHttpClient client = new OkHttpClient();
    private Call call, call2, call3, call4;

    private Button recBtn;
    private TextView recText0, recText1, recText2;

    private RecyclerView recommendedArtists;

    private RecommendService recService;
    private ArrayList<Artist> artistList = new ArrayList<>();
    int index = 0;

    private Artist artist;

        private FragmentDashboardBinding binding;
        private TextView textHangman;
        private TextView textInstructions;
        private Button genreButton;
        private Button artistButton;
        private Button songButton;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        textHangman = root.findViewById(R.id.text_hangman);
        textInstructions = root.findViewById(R.id.text_instructions);
        artistButton = root.findViewById(R.id.artist_button);
        songButton = root.findViewById(R.id.song_button);
        artistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.dashboardFragment_to_topArtistsFragment);
            }
        });

        songButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.dashboardFragment_to_topSongsFragment);
            }
        });
//
//
//
//        final TextView textView = binding.textDashboard;
//        dashboardViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        RecyclerView recommendedArtists = root.findViewById(R.id.recyclerView);
        ArtistAdapter adapter = new ArtistAdapter(artistList, getActivity());
        recommendedArtists.setAdapter(adapter);

        recBtn = (Button) root.findViewById(R.id.recommend);
        recBtn.setOnClickListener((v -> {
            getArtistsRecommend();
        }));

        recText0 = (TextView) root.findViewById(R.id.recText0);
        recText1 = (TextView) root.findViewById(R.id.recText1);
        recText2 = (TextView) root.findViewById(R.id.recText2);
        return root;
    }

    public void getArtistsRecommend() {
        ArrayList<Artist> artists = new ArrayList<>();
        ArrayList<String> artistIds = new ArrayList<>();
        final Request requestForTopTracks = new Request.Builder()
                .url("https://api.spotify.com/v1/me/top/artists?limit=3")
                .addHeader("Authorization", "Bearer " + mAccessToken)
                .build();

        if (call != null) {
            call.cancel();
        }

        call = client.newCall(requestForTopTracks);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                final JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response.body().string());
                    JSONArray jsonArray = jsonObject.getJSONArray("items");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject artistName = jsonArray.getJSONObject(i);
                        artistIds.add(artistName.getString("id"));
                    }

                    final Request requestForRecArtists0 = new Request.Builder()
                            .url("https://api.spotify.com/v1/artists/" + artistIds.get(0) + "/related-artists")
                            .addHeader("Authorization", "Bearer " + mAccessToken)
                            .build();

                    if (call2 != null) {
                        call2.cancel();
                    }

                    call2 = client.newCall(requestForRecArtists0);
                    call2.enqueue(new Callback() {
                        @Override
                        public void onFailure(@NonNull Call call, @NonNull IOException e) {

                        }

                        @Override
                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                            final JSONObject jsonObject;
                            try {
                                jsonObject = new JSONObject(response.body().string());
                                JSONArray jsonArray = jsonObject.getJSONArray("artists");
                                /*for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject artistName = jsonArray.getJSONObject(i);
                                    artistList.add(new Artist(artistName.getString("id"), artistName.getString("name")));
                                }*/
                                JSONObject artistName = jsonArray.getJSONObject(index);
                                recText0.setText(artistName.getString("name"));
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    });

                    final Request requestForRecArtists1 = new Request.Builder()
                            .url("https://api.spotify.com/v1/artists/" + artistIds.get(1) + "/related-artists")
                            .addHeader("Authorization", "Bearer " + mAccessToken)
                            .build();

                    if (call4 != null) {
                        call4.cancel();
                    }

                    call4 = client.newCall(requestForRecArtists1);
                    call4.enqueue(new Callback() {
                        @Override
                        public void onFailure(@NonNull Call call, @NonNull IOException e) {

                        }

                        @Override
                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                            final JSONObject jsonObject;
                            try {
                                jsonObject = new JSONObject(response.body().string());
                                JSONArray jsonArray = jsonObject.getJSONArray("artists");
                                /*for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject artistName = jsonArray.getJSONObject(i);
                                    artistList.add(new Artist(artistName.getString("id"), artistName.getString("name")));
                                }*/
                                JSONObject artistName = jsonArray.getJSONObject(index);
                                recText1.setText(artistName.getString("name"));
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    });

                    final Request requestForRecArtists2 = new Request.Builder()
                            .url("https://api.spotify.com/v1/artists/" + artistIds.get(2) + "/related-artists")
                            .addHeader("Authorization", "Bearer " + mAccessToken)
                            .build();

                    if (call3 != null) {
                        call3.cancel();
                    }

                    call3 = client.newCall(requestForRecArtists2);
                    call3.enqueue(new Callback() {
                        @Override
                        public void onFailure(@NonNull Call call, @NonNull IOException e) {

                        }

                        @Override
                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                            final JSONObject jsonObject;
                            try {
                                jsonObject = new JSONObject(response.body().string());
                                JSONArray jsonArray = jsonObject.getJSONArray("artists");
                                //for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject artistName = jsonArray.getJSONObject(index);
                                recText2.setText(artistName.getString("name"));
                                //artistList.add(new Artist(artistName.getString("id"), artistName.getString("name")));
                                //}
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    });
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        if (index == 1) {
            index = 0;
        } else {
            index++;
        }
    }

    private void getArtists() {
        recService.getRecArtists(() -> {
            artistList = recService.getRecommendations();
            updateArtists();
        });
    }

    private void updateArtists() {
        if (artistList.size() > 0) {
            recText0.setText(artistList.get(0).getName());
            artist = artistList.get(0);
        }
    }

    @Override
    public void onClick(View v) {

        getArtists();


        songButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.dashboardFragment_to_topSongsFragment);
            }
        });
    }
        @Override
        public void onDestroyView() {
            super.onDestroyView();
            binding = null;
        }

}