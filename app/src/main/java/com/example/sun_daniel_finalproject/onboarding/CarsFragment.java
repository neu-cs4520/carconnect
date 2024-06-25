package com.example.sun_daniel_finalproject.onboarding;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sun_daniel_finalproject.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import com.example.sun_daniel_finalproject.CarsAdapter;
import com.example.sun_daniel_finalproject.Car;

import java.util.ArrayList;
import java.util.List;

public class CarsFragment extends Fragment {

    private Button addCarButton, finishButton;
    private RecyclerView carsRecyclerView;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private CarsAdapter carsAdapter;
    private List<Car> carList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_onboarding_cars, container, false);
        addCarButton = view.findViewById(R.id.add_car_button);
        finishButton = view.findViewById(R.id.finish_button);
        carsRecyclerView = view.findViewById(R.id.cars_recycler_view);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        carList = new ArrayList<>();

        carsAdapter = new CarsAdapter(carList);
        carsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        carsRecyclerView.setAdapter(carsAdapter);

        addCarButton.setOnClickListener(v -> showAddCarDialog());

        finishButton.setOnClickListener(v -> {
            ((OnboardingActivity) getActivity()).completeOnboarding();
        });

        return view;
    }

    private void showAddCarDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Add New Car");

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_car, null);
        builder.setView(dialogView);

        TextInputEditText carMakeInput = dialogView.findViewById(R.id.car_make_input);
        TextInputEditText carModelInput = dialogView.findViewById(R.id.car_model_input);
        TextInputEditText carYearInput = dialogView.findViewById(R.id.car_year_input);
        TextInputEditText carColorInput = dialogView.findViewById(R.id.car_color_input);

        builder.setPositiveButton("Add", (dialog, which) -> {
            String carMake = carMakeInput.getText().toString().trim();
            String carModel = carModelInput.getText().toString().trim();
            String carYear = carYearInput.getText().toString().trim();
            String carColor = carColorInput.getText().toString().trim();

            if (!carMake.isEmpty() && !carModel.isEmpty() && !carYear.isEmpty() && !carColor.isEmpty()) {
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if (currentUser != null) {
                    String userId = currentUser.getUid();
                    Car car = new Car(carMake, carModel, carYear, carColor, userId);
                    saveCarToFirestore(car);
                    carsAdapter.notifyDataSetChanged();
                }
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void saveCarToFirestore(Car car) {
        String userId = mAuth.getCurrentUser().getUid();
        CollectionReference carsCollection = db.collection("users").document(userId).collection("cars");
        carsCollection.add(car)
                .addOnSuccessListener(documentReference -> {
                    carList.add(car);
                    carsAdapter.notifyDataSetChanged();
                    Toast.makeText(getContext(), "Car added successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Failed to add car: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
