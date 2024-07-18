package com.example.wrappify;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ViewHolder> {

    private List<Artist> recommends;

    private Activity activity;
    public ArtistAdapter(List<Artist> recommends, Activity activity) {
        this.recommends = recommends;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ArtistAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_dashboard, parent, false);
        return new ArtistAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistAdapter.ViewHolder holder, int position) {
        Artist item = recommends.get(position);
        holder.artName.setText("Class: " + item.getName());
    }

    @Override
    public int getItemCount() {
        return recommends.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView artName;
        ImageView artImage;

        ViewHolder(View view) {
            super(view);
            artName = view.findViewById(R.id.artName);
            artImage = view.findViewById(R.id.artImage);
        }
    }
}
