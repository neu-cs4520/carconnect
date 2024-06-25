package com.example.sun_daniel_finalproject.onboarding;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.sun_daniel_finalproject.MainActivity;
import com.example.sun_daniel_finalproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class OnboardingActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private OnboardingAdapter onboardingAdapter;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        mAuth = FirebaseAuth.getInstance();
        viewPager = findViewById(R.id.view_pager);
        onboardingAdapter = new OnboardingAdapter(this);
        viewPager.setAdapter(onboardingAdapter);

        // Check if user has completed onboarding before
        checkOnboardingStatus();
    }

    private void checkOnboardingStatus() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // Check if the user has completed onboarding
            // For simplicity, assuming a boolean field "onboardingCompleted" in the user's Firestore document
            String userId = currentUser.getUid();
            FirebaseFirestore.getInstance().collection("users").document(userId).get().addOnCompleteListener(task -> {
                if (task.isSuccessful() && task.getResult().exists() && task.getResult().getBoolean("onboardingCompleted") != null && task.getResult().getBoolean("onboardingCompleted")) {
                    // User has completed onboarding, redirect to MainActivity
                    startActivity(new Intent(OnboardingActivity.this, MainActivity.class));
                    finish();
                }
            });
        }
    }

    public void goToNextStep() {
        if (viewPager.getCurrentItem() < onboardingAdapter.getItemCount() - 1) {
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
        }
    }

    public void completeOnboarding() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
