package com.example.wrappify.ui.wrapped;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.wrappify.Artist;
import com.example.wrappify.MainActivity;
import com.example.wrappify.R;
import com.example.wrappify.Song;
import com.example.wrappify.SongService;
import com.example.wrappify.Wrapped;
import com.squareup.picasso.Picasso;

import java.io.OutputStream;
import java.util.ArrayList;
import java.io.File;
import java.util.Objects;

import android.net.Uri;
import androidx.core.content.FileProvider;
import androidx.navigation.Navigation;

import android.content.Intent;
import android.graphics.Bitmap;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;


public class WrappedGenerator extends Fragment {

    private Button generateButton;
    private Button viewWrappedButton;
    private Button exportImageButton;
    private Spinner pastWrappedSpinner;
    public static Spinner timeframeSpinner;
    ArrayAdapter<Wrapped> adapter;
    private Spinner themeSpinner;


    private SongService songService;
    private ArrayList<Wrapped> pastWrapped;
    public static ArrayList<Song> topSongs;
    public static ArrayList<Artist> topArtists;
    public static Wrapped currentWrapped;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_wrapped, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        songService = new SongService(view.getContext());
        generateButton = view.findViewById(R.id.generateButton);
        viewWrappedButton = view.findViewById(R.id.viewWrapped);
        exportImageButton = view.findViewById(R.id.exportImageButton);
        pastWrapped = MainActivity.pastWrapped;
        topSongs = MainActivity.topSongs;
        topArtists = MainActivity.topArtists;
        currentWrapped = MainActivity.currentWrapped;

        pastWrappedSpinner = view.findViewById(R.id.pastWrappedSpinner);
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, pastWrapped);
        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        pastWrappedSpinner.setAdapter(adapter);
        pastWrappedSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                currentWrapped = (Wrapped) parent.getItemAtPosition(pos);
                MainActivity.currentWrapped = currentWrapped;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        timeframeSpinner = view.findViewById(R.id.timeframeSpinner);
        ArrayList<String> timeframes = new ArrayList<>();
        timeframes.add("Past Year");
        timeframes.add("Past 6 Months");
        timeframes.add("Past 4 Weeks");
        ArrayAdapter<String> timeframeAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, timeframes);
        timeframeSpinner.setAdapter(timeframeAdapter);

        themeSpinner = view.findViewById(R.id.themeSpinner);
        ArrayList<String> themes = new ArrayList<>();
        themes.add("Regular");
        themes.add("St. Patrick's");
        themes.add("Valentine's");
        themes.add("Halloween");
        ArrayAdapter<String> themeAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, themes);
        themeSpinner.setAdapter(themeAdapter);

        generateButton.setOnClickListener(e -> {
            generateWrapped();
        });

        viewWrappedButton.setOnClickListener( e -> {
            Navigation.findNavController(view).navigate(R.id.wrappedGenerator_to_wrappedView);
        });

        exportImageButton.setOnClickListener(e -> {
            Navigation.findNavController(view).navigate(R.id.wrappedGenerator_to_wrappedSummary);
        });
    }

    private void generateWrapped() {
        getTopSongs();
    }

    private void getTopSongs() {
        songService.getTopSongs(() -> {
            MainActivity.topSongs = songService.getSongs();
            WrappedGenerator.topSongs = MainActivity.topSongs;
            getTopArtists();
        });
    }

    private void getTopArtists() {
        songService.getTopArtists(() -> {
            MainActivity.topArtists = songService.getArtists();
            WrappedGenerator.topArtists = MainActivity.topArtists;
            Wrapped newWrapped = new Wrapped((String) themeSpinner.getSelectedItem());
            newWrapped.addSongs(topSongs);
            newWrapped.addArtists(topArtists);
            MainActivity.currentWrapped = newWrapped;
            currentWrapped = newWrapped;
            pastWrapped.add(newWrapped);
            adapter.notifyDataSetChanged();
            pastWrappedSpinner.setSelection(pastWrapped.size() - 1);
            Toast.makeText(getContext(), "Your Wrapped is ready to view!", Toast.LENGTH_SHORT).show();
        });
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}