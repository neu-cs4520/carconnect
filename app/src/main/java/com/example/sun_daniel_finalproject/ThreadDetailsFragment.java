package com.example.sun_daniel_finalproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ThreadDetailsFragment extends Fragment {

    private static final String ARG_TITLE = "title";
    private static final String ARG_CONTENT = "content";
    private static final String ARG_LIKES = "likes";

    private String title;
    private String content;
    private int likes;

    public static ThreadDetailsFragment newInstance(String title, String content, int likes) {
        ThreadDetailsFragment fragment = new ThreadDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putString(ARG_CONTENT, content);
        args.putInt(ARG_LIKES, likes);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString(ARG_TITLE);
            content = getArguments().getString(ARG_CONTENT);
            likes = getArguments().getInt(ARG_LIKES);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_thread_details, container, false);

        TextView titleTextView = view.findViewById(R.id.thread_title);
        TextView contentTextView = view.findViewById(R.id.thread_content);
        TextView likesTextView = view.findViewById(R.id.thread_likes);

        titleTextView.setText(title);
        contentTextView.setText(content);
        likesTextView.setText(String.valueOf(likes));

        // Implement reply, like, and share functionality here

        return view;
    }
}
