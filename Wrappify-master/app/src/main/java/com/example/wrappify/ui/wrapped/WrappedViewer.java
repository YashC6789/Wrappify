package com.example.wrappify.ui.wrapped;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.wrappify.MainActivity;
import com.example.wrappify.R;

import java.util.ArrayList;

public class WrappedViewer extends Fragment {

    private ViewPager2 viewPager2;
    private Button backButton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wrapped_viewer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        switch(MainActivity.currentWrapped.getTheme()) {
            case "St. Patrick's":
                view.setBackground(getResources().getDrawable(R.drawable.stpatricksday));
                break;
            case "Valentine's":
                view.setBackground(getResources().getDrawable(R.drawable.valentines));
                break;
            case "Halloween":
                view.setBackground(getResources().getDrawable(R.drawable.halloween));
                break;
            default:
                break;
        }
        viewPager2 = view.findViewById(R.id.viewpager);
        backButton = view.findViewById(R.id.backToGenerator);

        WrappedViewAdapter wrappedViewAdapter = new WrappedViewAdapter(WrappedGenerator.currentWrapped);

        viewPager2.setAdapter(wrappedViewAdapter);

        viewPager2.setClipToPadding(false);

        viewPager2.setClipChildren(false);

        viewPager2.setOffscreenPageLimit(2);

        viewPager2.getChildAt(0).setOverScrollMode(View.OVER_SCROLL_NEVER);

        backButton.setOnClickListener(e -> {
            Navigation.findNavController(view).navigate(R.id.wrappedView_to_wrappedGenerator);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}