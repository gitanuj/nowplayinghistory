package com.tanuj.nowplayinghistory.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tanuj.nowplayinghistory.R;
import com.tanuj.nowplayinghistory.Utils;

public class EmptyFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.empty_state, container, false);
        view.findViewById(R.id.launch_now_playing_settings).setOnClickListener(v -> Utils.launchNowPlayingSettings());
        return view;
    }
}
