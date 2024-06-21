package com.example.sun_daniel_finalproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class EventDetailsFragment extends Fragment {

    private static final String ARG_TITLE = "title";
    private static final String ARG_DATE = "date";
    private static final String ARG_LOCATION = "location";
    private static final String ARG_DESCRIPTION = "description";

    private String title;
    private String date;
    private String location;
    private String description;

    public static EventDetailsFragment newInstance(String title, String date, String location, String description) {
        EventDetailsFragment fragment = new EventDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putString(ARG_DATE, date);
        args.putString(ARG_LOCATION, location);
        args.putString(ARG_DESCRIPTION, description);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString(ARG_TITLE);
            date = getArguments().getString(ARG_DATE);
            location = getArguments().getString(ARG_LOCATION);
            description = getArguments().getString(ARG_DESCRIPTION);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_details, container, false);

        TextView titleTextView = view.findViewById(R.id.event_title);
        TextView dateTextView = view.findViewById(R.id.event_date);
        TextView locationTextView = view.findViewById(R.id.event_location);
        TextView descriptionTextView = view.findViewById(R.id.event_description);

        titleTextView.setText(title);
        dateTextView.setText(date);
        locationTextView.setText(location);
        descriptionTextView.setText(description);

        return view;
    }
}
