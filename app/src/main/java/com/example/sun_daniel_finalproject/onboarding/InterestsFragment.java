package com.example.sun_daniel_finalproject.onboarding;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sun_daniel_finalproject.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class InterestsFragment extends Fragment {

    private TextInputEditText searchInterestsInput;
    private RecyclerView interestsRecyclerView;
    private Button nextButton;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private InterestsAdapter interestsAdapter;
    private List<String> interestsList;
    private List<String> selectedInterests;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_onboarding_interests, container, false);
        searchInterestsInput = view.findViewById(R.id.search_interests_input);
        interestsRecyclerView = view.findViewById(R.id.interests_recycler_view);
        nextButton = view.findViewById(R.id.next_button);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        selectedInterests = new ArrayList<>();

        interestsList = getInterestsList(); // Fetch the list of interests
        interestsAdapter = new InterestsAdapter(interestsList, selectedInterests);
        interestsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        interestsRecyclerView.setAdapter(interestsAdapter);

        searchInterestsInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                interestsAdapter.filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        nextButton.setOnClickListener(v -> {
            saveInterests(selectedInterests);
            ((OnboardingActivity) getActivity()).goToNextStep();
        });

        return view;
    }

    private List<String> getInterestsList() {
        // Return a predefined list of interests or fetch from Firestore
        List<String> interests = new ArrayList<>();
        interests.add("Cars");
        interests.add("Racing");
        interests.add("Mechanics");
        interests.add("JDM");
        interests.add("Motorcycles");
        interests.add("Trucks");
        interests.add("German");
        interests.add("American");
        interests.add("Muscle Cars");
        interests.add("");
        // Add more interests as needed
        return interests;
    }

    private void saveInterests(List<String> interests) {
        String userId = mAuth.getCurrentUser().getUid();
        db.collection("users").document(userId).update("interests", interests);
    }
}
