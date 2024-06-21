package com.example.sun_daniel_finalproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private List<Event> eventList;
    private FragmentActivity fragmentActivity;

    public EventAdapter(List<Event> eventList, FragmentActivity fragmentActivity) {
        this.eventList = eventList;
        this.fragmentActivity = fragmentActivity;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event currentEvent = eventList.get(position);
        holder.titleTextView.setText(currentEvent.getTitle());
        holder.dateTextView.setText(currentEvent.getDate());
        holder.locationTextView.setText(currentEvent.getLocation());
        holder.descriptionTextView.setText(currentEvent.getDescription());
        holder.descriptionTextView.setMaxLines(2); // Truncate description to a max of 2 lines

        holder.itemView.setOnClickListener(v -> {
            EventDetailsFragment detailsFragment = EventDetailsFragment.newInstance(
                    currentEvent.getTitle(),
                    currentEvent.getDate(),
                    currentEvent.getLocation(),
                    currentEvent.getDescription());

            fragmentActivity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, detailsFragment)
                    .addToBackStack(null)
                    .commit();
        });
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {

        public TextView titleTextView;
        public TextView dateTextView;
        public TextView locationTextView;
        public TextView descriptionTextView;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.event_title);
            dateTextView = itemView.findViewById(R.id.event_date);
            locationTextView = itemView.findViewById(R.id.event_location);
            descriptionTextView = itemView.findViewById(R.id.event_description);
        }
    }
}
