package com.example.sun_daniel_finalproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class MarketFragment extends Fragment {

    private RecyclerView recyclerView;
    private MarketAdapter marketAdapter;
    private List<MarketItem> marketItemList;
    private FirebaseFirestore db;
    private ExtendedFloatingActionButton addMarketItemFab;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private String sellerName;
    private String sellerEmail;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_market, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_market);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        addMarketItemFab = view.findViewById(R.id.add_market_item_fab);
        addMarketItemFab.setOnClickListener(v -> showAddMarketItemDialog());

        marketItemList = new ArrayList<>();
        marketAdapter = new MarketAdapter(marketItemList, getActivity());
        recyclerView.setAdapter(marketAdapter);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            loadSellerInfo();
        }

        loadMarketItems();

        return view;
    }

    private void loadSellerInfo() {
        String userId = currentUser.getUid();
        db.collection("users").document(userId).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                sellerName = documentSnapshot.getString("username");
                sellerEmail = currentUser.getEmail();
            }
        });
    }

    private void loadMarketItems() {
        CollectionReference marketItemsRef = db.collection("market_items");
        marketItemsRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                marketItemList.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    MarketItem marketItem = document.toObject(MarketItem.class);
                    marketItemList.add(marketItem);
                }
                marketAdapter.notifyDataSetChanged();
            } else {
                // Handle the error
            }
        });
    }

    private void showAddMarketItemDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Add New Item");

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_market_item, null);
        builder.setView(dialogView);

        TextInputEditText itemNameInput = dialogView.findViewById(R.id.item_name_input);
        TextInputEditText itemPriceInput = dialogView.findViewById(R.id.item_price_input);
        TextInputEditText itemDescriptionInput = dialogView.findViewById(R.id.item_description_input);

        builder.setPositiveButton("Add", (dialog, which) -> {
            String name = itemNameInput.getText().toString().trim();
            String price = itemPriceInput.getText().toString().trim();
            String description = itemDescriptionInput.getText().toString().trim();

            if (!name.isEmpty() && !price.isEmpty() && !description.isEmpty() && sellerName != null && sellerEmail != null) {
                MarketItem marketItem = new MarketItem(name, price, description, sellerName, sellerEmail);
                saveMarketItemToFirestore(marketItem);
            } else {
                Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void saveMarketItemToFirestore(MarketItem marketItem) {
        CollectionReference marketItemsRef = db.collection("market_items");
        marketItemsRef.add(marketItem)
                .addOnSuccessListener(documentReference -> {
                    marketItemList.add(marketItem);
                    marketAdapter.notifyDataSetChanged();
                    Toast.makeText(getContext(), "Item added", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Failed to add item", Toast.LENGTH_SHORT).show();
                });
    }
}
