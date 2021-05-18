package com.example.space_shooter.Shop.main;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.space_shooter.Content;
import com.example.space_shooter.R;
import com.google.android.material.snackbar.Snackbar;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "sectionNumber";

    private PageViewModel pageViewModel;

    private static String picture_number = "test";

    public static PlaceholderFragment newInstance(int index) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = new ViewModelProvider(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_shop, container, false);
        final TextView textView = root.findViewById(R.id.section_label);
        final ImageView imageView = root.findViewById(R.id.imageView);
        pageViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if(Integer.valueOf(s) > 0 && Integer.valueOf(s) <= 3){
                    String name = "player" + s;
                    int holderImg = getResources().getIdentifier(name, "drawable", getContext().getPackageName());
                    int holderString = getResources().getIdentifier(name, "string", getContext().getPackageName());
                    textView.setText(holderString);
                    imageView.setImageResource(holderImg);
                }
                else{
                    Snackbar.make(root, "Произошла непредвиденная ошибка! Этого файла нету в игре или он добавлен вручную.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    try{
                        String name = "player" + s;
                        int holderString = getResources().getIdentifier(name, "string", getContext().getPackageName());
                        textView.setText(holderString);
                    }catch (Exception e){
                        textView.setText(s + ". Exception "+e);
                    }

                    try{
                        String name = "player" + s;
                        int holderImg = getResources().getIdentifier(name, "drawable", getContext().getPackageName());
                        imageView.setImageResource(holderImg);
                    } catch (Exception e) {
                        imageView.setImageResource(R.mipmap.ic_launcher);
                    }
                }
            }
        });
        return root;
    }
}