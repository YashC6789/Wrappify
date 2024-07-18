package com.example.wrappify.ui.wrapped;

import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wrappify.R;
import com.example.wrappify.Wrapped;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class WrappedViewAdapter extends RecyclerView.Adapter<WrappedViewAdapter.ViewHolder> {

    Wrapped currentWrapped;

    public WrappedViewAdapter(Wrapped currentWrapped) {
        this.currentWrapped = currentWrapped;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.wrapped_slide,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if (position == 0) {
            holder.slide_title.setText("");
            holder.image1.setVisibility(View.INVISIBLE);
            holder.image2.setVisibility(View.INVISIBLE);
            holder.image3.setVisibility(View.INVISIBLE);
            holder.image4.setVisibility(View.INVISIBLE);
            holder.image5.setVisibility(View.INVISIBLE);
            holder.text1.setText("");
            holder.text2.setText("");
            holder.text3.setText("Let's wrap up \nyour music tastes!");
            holder.text3.setTypeface(null, Typeface.BOLD);
            holder.text3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            holder.text4.setText("");
            holder.text5.setText("");
        } else if (position == 1) {
            holder.slide_title.setText("");
            holder.image1.setVisibility(View.INVISIBLE);
            holder.image2.setVisibility(View.INVISIBLE);
            holder.image3.setVisibility(View.INVISIBLE);
            holder.image4.setVisibility(View.INVISIBLE);
            holder.image5.setVisibility(View.INVISIBLE);
            holder.text1.setText("");
            holder.text2.setText("");
            holder.text3.setText("First up:\nTop Songs");
            holder.text3.setTypeface(null, Typeface.BOLD);
            holder.text3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            holder.text4.setText("");
            holder.text5.setText("");
        } else if (position == 2) {
            holder.slide_title.setText("Top Songs");
            holder.text1.setText(currentWrapped.getTopSongs().get(0).toString());
            holder.text2.setText(currentWrapped.getTopSongs().get(1).toString());
            holder.text3.setText(currentWrapped.getTopSongs().get(2).toString());
            holder.text4.setText(currentWrapped.getTopSongs().get(3).toString());
            holder.text5.setText(currentWrapped.getTopSongs().get(4).toString());
            ArrayList<ImageView> images = new ArrayList<>();
            images.add(holder.image1);
            images.add(holder.image2);
            images.add(holder.image3);
            images.add(holder.image4);
            images.add(holder.image5);

            for(int i = 0; i < 5; i++) {
                Picasso.with(holder.image1.getContext()).load(currentWrapped.getTopSongs().get(i).getUrl()).into(images.get(i));
            }
        } else if (position == 3) {
            holder.slide_title.setText("");
            holder.image1.setVisibility(View.INVISIBLE);
            holder.image2.setVisibility(View.INVISIBLE);
            holder.image3.setVisibility(View.INVISIBLE);
            holder.image4.setVisibility(View.INVISIBLE);
            holder.image5.setVisibility(View.INVISIBLE);
            holder.text1.setText("");
            holder.text2.setText("");
            holder.text3.setText("Next up:\nTop Artists");
            holder.text3.setTypeface(null, Typeface.BOLD);
            holder.text3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            holder.text4.setText("");
            holder.text5.setText("");
        } else if (position == 4) {
            holder.slide_title.setText("Top Artists");
            holder.text1.setText(currentWrapped.getTopArtists().get(0).toString());
            holder.text2.setText(currentWrapped.getTopArtists().get(1).toString());
            holder.text3.setText(currentWrapped.getTopArtists().get(2).toString());
            holder.text4.setText(currentWrapped.getTopArtists().get(3).toString());
            holder.text5.setText(currentWrapped.getTopArtists().get(4).toString());
            ArrayList<ImageView> images = new ArrayList<>();
            images.add(holder.image1);
            images.add(holder.image2);
            images.add(holder.image3);
            images.add(holder.image4);
            images.add(holder.image5);

            for(int i = 0; i < 5; i++) {
                Picasso.with(holder.image1.getContext()).load(currentWrapped.getTopArtists().get(i).getUrl()).into(images.get(i));
            }
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public void setCurrentWrapped(Wrapped currentWrapped) {
        this.currentWrapped = currentWrapped;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView image1;
        ImageView image2;
        ImageView image3;
        ImageView image4;
        ImageView image5;
        TextView text1;
        TextView text2;
        TextView text3;
        TextView text4;
        TextView text5;
        TextView slide_title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image1 = itemView.findViewById(R.id.image1);
            image2 = itemView.findViewById(R.id.image2);
            image3 = itemView.findViewById(R.id.image3);
            image4 = itemView.findViewById(R.id.image4);
            image5 = itemView.findViewById(R.id.image5);
            text1 = itemView.findViewById(R.id.text1);
            text2 = itemView.findViewById(R.id.text2);
            text3 = itemView.findViewById(R.id.text3);
            text4 = itemView.findViewById(R.id.text4);
            text5 = itemView.findViewById(R.id.text5);
            slide_title = itemView.findViewById(R.id.slide_title);
        }
    }

}