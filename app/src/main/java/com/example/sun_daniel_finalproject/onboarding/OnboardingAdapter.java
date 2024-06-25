package com.example.sun_daniel_finalproject.onboarding;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class OnboardingAdapter extends FragmentStateAdapter {

    public OnboardingAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new UsernameFragment();
            case 1:
                return new PhoneFragment();
            case 2:
                return new InterestsFragment();
            case 3:
                return new CarsFragment();
            default:
                return new UsernameFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
