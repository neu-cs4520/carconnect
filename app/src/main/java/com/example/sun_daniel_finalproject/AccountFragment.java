package com.example.sun_daniel_finalproject;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class AccountFragment extends Fragment {

    private TextView usernameText;
    private RecyclerView recyclerViewCars;
    private Button addCarButton;
    private CarsAdapter carsAdapter;
    private List<Car> carList;
    FirebaseAuth mAuth;
    FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        usernameText = view.findViewById(R.id.username_text);
        recyclerViewCars = view.findViewById(R.id.recycler_view_cars);
        addCarButton = view.findViewById(R.id.add_car_button);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            loadUserData(currentUser.getUid());
        } else {
            usernameText.setText("Guest");
        }

        carList = new ArrayList<>();
        carsAdapter = new CarsAdapter(carList);
        recyclerViewCars.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewCars.setAdapter(carsAdapter);

        addCarButton.setOnClickListener(v -> {
            // Add logic to add a new car using CarAPI
            showAddCarDialog();
        });

        return view;
    }

    private void showAddCarDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Add New Car");

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_car, null);
        builder.setView(dialogView);

        EditText carMakeInput = dialogView.findViewById(R.id.car_make_input);
        EditText carModelInput = dialogView.findViewById(R.id.car_model_input);
        EditText carYearInput = dialogView.findViewById(R.id.car_year_input);
        EditText carColorInput = dialogView.findViewById(R.id.car_color_input);

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
                    carList.add(car);
                    carsAdapter.notifyDataSetChanged();
                    saveCarToFirestore(car);
                }
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void saveCarToFirestore(Car car) {
        DocumentReference userDocRef = db.collection("users").document(car.getOwnerId());

        userDocRef.collection("cars").add(car)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(getContext(), "Car added", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Failed to add car", Toast.LENGTH_SHORT).show();
                });
    }

    private void loadUserData(String userId) {
        DocumentReference userDocRef = db.collection("users").document(userId);
        userDocRef.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Account account = documentSnapshot.toObject(Account.class);
                        if (account != null) {
                            usernameText.setText(account.getUsername());
                            // Load cars if any
                            loadUserCars(userId);
                        }
                    } else {
                        // If no account data exists, create a new account
                        FirebaseUser currentUser = mAuth.getCurrentUser();
                        if (currentUser != null) {
                            String username = currentUser.getDisplayName();

                            Account account = new Account(userId, username);
                            userDocRef.set(account)
                                    .addOnSuccessListener(aVoid -> {
                                        usernameText.setText(username);
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(getContext(), "Failed to create user account", Toast.LENGTH_SHORT).show();
                                    });
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Failed to load user data", Toast.LENGTH_SHORT).show();
                });
    }

    private void loadUserCars(String userId) {
        db.collection("users").document(userId).collection("cars")
                .whereEqualTo("ownerId", userId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    carList.clear();
                    carList.addAll(queryDocumentSnapshots.toObjects(Car.class));
                    carsAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Failed to load cars", Toast.LENGTH_SHORT).show();
                });
    }
}
