package com.example.sun_daniel_finalproject.onboarding;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sun_daniel_finalproject.Account;
import com.example.sun_daniel_finalproject.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UsernameFragment extends Fragment {

    private TextInputEditText usernameInput;
    private Button nextButton;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_onboarding_username, container, false);
        usernameInput = view.findViewById(R.id.username_input);
        nextButton = view.findViewById(R.id.next_button);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        nextButton.setOnClickListener(v -> {
            String username = usernameInput.getText().toString().trim();
            if (!username.isEmpty()) {
                checkUsernameAvailability(username);
            }
        });

        return view;
    }

    private void checkUsernameAvailability(String username) {
        db.collection("users").whereEqualTo("username", username).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult().isEmpty()) {
                            if (checkUsernameSize(username)) {
                                saveUsername(username);
                            } else {
                                Toast.makeText(getContext(), "Username is too long. Please stay under 20 characters.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getContext(), "Username already exists. Please choose another username.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "Failed to check username availability", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private boolean checkUsernameSize(String username) {
        return username.length() <= 20;
    }

    private void saveUsername(String username) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(username)
                    .build();

            user.updateProfile(profileUpdates)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            createOrUpdateUserAccount(user.getUid(), user.getDisplayName());
                        } else {
                            Toast.makeText(getContext(), "Error updating profile: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void createOrUpdateUserAccount(String userId, String username) {
        db.collection("users").document(userId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // If account already exists, just update the username
                        db.collection("users").document(userId).update("username", username)
                                .addOnSuccessListener(aVoid -> {
                                    ((OnboardingActivity) getActivity()).goToNextStep();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(getContext(), "Failed to update username", Toast.LENGTH_SHORT).show();
                                });
                    } else {
                        // If no account data exists, create a new account
                        Account account = new Account(userId, username);
                        db.collection("users").document(userId).set(account)
                                .addOnSuccessListener(aVoid -> {
                                    ((OnboardingActivity) getActivity()).goToNextStep();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(getContext(), "Failed to create user account", Toast.LENGTH_SHORT).show();
                                });
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Failed to load user data", Toast.LENGTH_SHORT).show();
                });
    }
}
