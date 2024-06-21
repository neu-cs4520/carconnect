package com.example.sun_daniel_finalproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EventsFragment extends Fragment {

    private RecyclerView recyclerView;
    private EventAdapter eventAdapter;
    private List<Event> eventList;
    private FirebaseFirestore db;
    private ExtendedFloatingActionButton addEventFab;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_events, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_events);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        addEventFab = view.findViewById(R.id.add_event_fab);
        addEventFab.setOnClickListener(v -> showAddEventDialog());

        eventList = new ArrayList<>();
        eventAdapter = new EventAdapter(eventList, getActivity());
        recyclerView.setAdapter(eventAdapter);

        db = FirebaseFirestore.getInstance();

        loadEvents();

        return view;
    }

    private void loadEvents() {
        CollectionReference eventsRef = db.collection("events");
        eventsRef.orderBy("date").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                eventList.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Event event = document.toObject(Event.class);
                    eventList.add(event);
                }
                eventAdapter.notifyDataSetChanged();
            } else {
                // Handle the error
            }
        });
    }

    private void showAddEventDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Add New Event");

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_event, null);
        builder.setView(dialogView);

        EditText eventTitleInput = dialogView.findViewById(R.id.event_title_input);
        TextView eventDateInput = dialogView.findViewById(R.id.event_date_input);
        EditText eventLocationInput = dialogView.findViewById(R.id.event_location_input);
        EditText eventDescriptionInput = dialogView.findViewById(R.id.event_description_input);

        // Setup Material Date Picker
        eventDateInput.setOnClickListener(v -> {
            MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Select Event Date")
                    .build();

            datePicker.addOnPositiveButtonClickListener(selection -> {
                String selectedDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date(selection));
                eventDateInput.setText(selectedDate);
            });

            datePicker.show(getParentFragmentManager(), "datePicker");
        });

        builder.setPositiveButton("Add", (dialog, which) -> {
            String title = eventTitleInput.getText().toString().trim();
            String date = eventDateInput.getText().toString().trim();
            String location = eventLocationInput.getText().toString().trim();
            String description = eventDescriptionInput.getText().toString().trim();

            if (!title.isEmpty() && !date.isEmpty() && !location.isEmpty() && !description.isEmpty()) {
                Event event = new Event(title, date, location, description);
                saveEventToFirestore(event);
            } else {
                Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void saveEventToFirestore(Event event) {
        CollectionReference eventsRef = db.collection("events");
        eventsRef.add(event)
                .addOnSuccessListener(documentReference -> {
                    eventList.add(event);
                    eventAdapter.notifyDataSetChanged();
                    Toast.makeText(getContext(), "Event added", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Failed to add event", Toast.LENGTH_SHORT).show();
                });
    }
}
