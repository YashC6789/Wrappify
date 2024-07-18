package com.example.wrappify;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

public class Wrapped {
    private ArrayList<Song> topSongs;
    private ArrayList<Artist> topArtists;
    private String date;
    private String theme;

    public Wrapped(String theme) {
        topSongs = new ArrayList<>();
        topArtists = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyy.MMMMM.dd GGG hh:mm aaa");
        date = sdf.format(new Date());
        this.theme = theme;
    }

    public void addSongs(ArrayList<Song> songs) {
        topSongs.addAll(songs);
    }

    public void addArtists(ArrayList<Artist> artists) {
        topArtists.addAll(artists);
    }
    public ArrayList<Song> getTopSongs() {
        return topSongs;
    }

    public ArrayList<Artist> getTopArtists() {
        return topArtists;
    }

    public String toString() {
        return date;
    }

    public String getTheme() {
        return theme;
    }
}
