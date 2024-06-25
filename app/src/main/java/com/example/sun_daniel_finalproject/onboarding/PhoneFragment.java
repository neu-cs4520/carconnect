package com.example.sun_daniel_finalproject.onboarding;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sun_daniel_finalproject.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class PhoneFragment extends Fragment {

    private TextInputEditText phoneInput;
    private Button nextButton;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_onboarding_phone, container, false);
        phoneInput = view.findViewById(R.id.phone_input);
        nextButton = view.findViewById(R.id.next_button);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        nextButton.setOnClickListener(v -> {
            String phoneNumber = phoneInput.getText().toString().trim();
            if (!phoneNumber.isEmpty()) {
                savePhoneNumber(phoneNumber);
                ((OnboardingActivity) getActivity()).goToNextStep();
            }
        });

        return view;
    }

    private void savePhoneNumber(String phoneNumber) {
        String userId = mAuth.getCurrentUser().getUid();
        db.collection("users").document(userId).update("phoneNumber", phoneNumber);
    }
}
