package com.example.wrappify.ui.notifications;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.wrappify.LoginActivity;
import com.example.wrappify.MainActivity;
import com.example.wrappify.R;
import com.example.wrappify.SongService;
import com.example.wrappify.databinding.FragmentNotificationsBinding;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;
    private Button removeProfile;
    private Button changeAccount;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_notifications, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        removeProfile = view.findViewById(R.id.removeProfile);

        removeProfile.setOnClickListener(e -> {
            removeProfile();

        });

        changeAccount = view.findViewById(R.id.changeAccount);

        changeAccount.setOnClickListener(e -> {
            removeProfile();
        });
    }

    public void removeProfile() {
        LoginActivity.setAccessToken(null);
        String url = "https://accounts.spotify.com/en/logout";
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
        Intent myIntent = new Intent(this.getContext(), LoginActivity.class);
        startActivity(myIntent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}