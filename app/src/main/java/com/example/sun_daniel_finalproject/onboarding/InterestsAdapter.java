package com.example.sun_daniel_finalproject.onboarding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sun_daniel_finalproject.R;

import java.util.ArrayList;
import java.util.List;

public class InterestsAdapter extends RecyclerView.Adapter<InterestsAdapter.InterestsViewHolder> {

    private List<String> interestsList;
    private List<String> filteredInterestsList;
    private List<String> selectedInterests;

    public InterestsAdapter(List<String> interestsList, List<String> selectedInterests) {
        this.interestsList = new ArrayList<>(interestsList);
        this.filteredInterestsList = new ArrayList<>(interestsList);
        this.selectedInterests = selectedInterests;
    }

    @NonNull
    @Override
    public InterestsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_interest, parent, false);
        return new InterestsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InterestsViewHolder holder, int position) {
        String interest = filteredInterestsList.get(position);
        holder.interestText.setText(interest);
        holder.interestCheckBox.setChecked(selectedInterests.contains(interest));

        holder.itemView.setOnClickListener(v -> {
            if (holder.interestCheckBox.isChecked()) {
                selectedInterests.remove(interest);
                holder.interestCheckBox.setChecked(false);
            } else {
                selectedInterests.add(interest);
                holder.interestCheckBox.setChecked(true);
            }
        });
    }

    @Override
    public int getItemCount() {
        return filteredInterestsList.size();
    }

    public void filter(String query) {
        filteredInterestsList.clear();
        if (query.isEmpty()) {
            filteredInterestsList.addAll(interestsList);
        } else {
            for (String interest : interestsList) {
                if (interest.toLowerCase().contains(query.toLowerCase())) {
                    filteredInterestsList.add(interest);
                }
            }
        }
        notifyDataSetChanged();
    }

    static class InterestsViewHolder extends RecyclerView.ViewHolder {
        TextView interestText;
        CheckBox interestCheckBox;

        public InterestsViewHolder(@NonNull View itemView) {
            super(itemView);
            interestText = itemView.findViewById(R.id.interest_text);
            interestCheckBox = itemView.findViewById(R.id.interest_checkbox);
        }
    }
}
