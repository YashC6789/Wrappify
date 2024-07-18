package com.example.wrappify.ui.wrapped;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wrappify.MainActivity;
import com.example.wrappify.R;
import com.example.wrappify.Wrapped;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Objects;

public class WrappedSummary extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_wrapped_summary, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button back = view.findViewById(R.id.back);
        Button export = view.findViewById(R.id.export);
        TextView song1 = view.findViewById(R.id.song1);
        TextView song2 = view.findViewById(R.id.song2);
        TextView song3 = view.findViewById(R.id.song3);
        TextView song4 = view.findViewById(R.id.song4);
        TextView song5 = view.findViewById(R.id.song5);
        ImageView songImage1 = view.findViewById(R.id.songImage1);
        ImageView songImage2 = view.findViewById(R.id.songImage2);
        ImageView songImage3 = view.findViewById(R.id.songImage3);
        ImageView songImage4 = view.findViewById(R.id.songImage4);
        ImageView songImage5 = view.findViewById(R.id.songImage5);

        Wrapped selected = MainActivity.currentWrapped;
        song1.setText(selected.getTopSongs().get(0).toString());
        song2.setText(selected.getTopSongs().get(1).toString());
        song3.setText(selected.getTopSongs().get(2).toString());
        song4.setText(selected.getTopSongs().get(3).toString());
        song5.setText(selected.getTopSongs().get(4).toString());

        ArrayList<ImageView> songImages = new ArrayList<>();
        songImages.add(songImage1);
        songImages.add(songImage2);
        songImages.add(songImage3);
        songImages.add(songImage4);
        songImages.add(songImage5);

        for(int i = 0; i < 5; i++) {
            Picasso.with(getContext()).load(selected.getTopSongs().get(i).getUrl()).into(songImages.get(i));
        }

        TextView artist1 = view.findViewById(R.id.artist1);
        TextView artist2 = view.findViewById(R.id.artist2);
        TextView artist3 = view.findViewById(R.id.artist3);
        TextView artist4 = view.findViewById(R.id.artist4);
        TextView artist5 = view.findViewById(R.id.artist5);
        ImageView artistImage1 = view.findViewById(R.id.artistImage1);
        ImageView artistImage2 = view.findViewById(R.id.artistImage2);
        ImageView artistImage3 = view.findViewById(R.id.artistImage3);
        ImageView artistImage4 = view.findViewById(R.id.artistImage4);
        ImageView artistImage5 = view.findViewById(R.id.artistImage5);

        artist1.setText(selected.getTopArtists().get(0).toString());
        artist2.setText(selected.getTopArtists().get(1).toString());
        artist3.setText(selected.getTopArtists().get(2).toString());
        artist4.setText(selected.getTopArtists().get(3).toString());
        artist5.setText(selected.getTopArtists().get(4).toString());

        ArrayList<ImageView> artistImages = new ArrayList<>();
        artistImages.add(artistImage1);
        artistImages.add(artistImage2);
        artistImages.add(artistImage3);
        artistImages.add(artistImage4);
        artistImages.add(artistImage5);

        for(int i = 0; i < 5; i++) {
            Picasso.with(getContext()).load(selected.getTopArtists().get(i).getUrl()).into(artistImages.get(i));
        }

        back.setOnClickListener(e -> {
            Navigation.findNavController(view).navigate(R.id.wrappedSummary_to_wrappedGenerator);
        });

        export.setOnClickListener(e -> {
            exportWrappedSummaryAsImage();
        });
    }

    private void exportWrappedSummaryAsImage() {
        if (getView() == null) return; // If view is not ready

        // Create a Bitmap with same size as the view
        getView().post(() -> {
            getView().setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(getView().getDrawingCache());
            getView().setDrawingCacheEnabled(false);

            // Save the Bitmap to file
            String fileName = "wrapped_summary";
            String imagePath = saveBitmapToFile(getContext(), bitmap, fileName);

            // Share the Bitmap using Intent
            if (imagePath != null) {
                shareImage(imagePath);
            }
        });
    }
    private String saveBitmapToFile(Context context, Bitmap bitmap, String filename) {
        Uri images;
        ContentResolver contentResolver = getActivity().getContentResolver();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            images = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
        } else {
            images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME,  System.currentTimeMillis() + "jpg");
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        Uri uri = contentResolver.insert(images, contentValues);

        try {
            OutputStream outputStream = contentResolver.openOutputStream(Objects.requireNonNull(uri));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            Objects.requireNonNull(outputStream);

            Toast.makeText(getContext(), "Images Saved Successfully", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getContext(), "Images not saved", Toast.LENGTH_SHORT).show();
        }
        File outputFile = new File(context.getFilesDir(), filename + ".png");
        String storedImagePath = null;
        storedImagePath = outputFile.getAbsolutePath();
        return storedImagePath;
    }

    private void shareImage(String imagePath) {
        File imageFile = new File(imagePath);
        Uri imageUri = FileProvider.getUriForFile(getContext(), getContext().getApplicationContext().getPackageName() + ".provider", imageFile);
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // Temp permission for receiving app to read file
        shareIntent.setDataAndType(imageUri, getContext().getContentResolver().getType(imageUri));
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        startActivity(Intent.createChooser(shareIntent, "Share Wrapped Summary"));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}