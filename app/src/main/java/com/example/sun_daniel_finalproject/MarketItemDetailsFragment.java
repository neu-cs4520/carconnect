package com.example.sun_daniel_finalproject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class MarketItemDetailsFragment extends Fragment {

    private static final String ARG_NAME = "name";
    private static final String ARG_PRICE = "price";
    private static final String ARG_DESCRIPTION = "description";
    private static final String ARG_SELLER_NAME = "sellerName";
    private static final String ARG_SELLER_CONTACT = "sellerContact";

    private String name;
    private String price;
    private String description;
    private String sellerName;
    private String sellerContact;

    public static MarketItemDetailsFragment newInstance(String name, String price, String description, String sellerName, String sellerContact) {
        MarketItemDetailsFragment fragment = new MarketItemDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NAME, name);
        args.putString(ARG_PRICE, price);
        args.putString(ARG_DESCRIPTION, description);
        args.putString(ARG_SELLER_NAME, sellerName);
        args.putString(ARG_SELLER_CONTACT, sellerContact);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            name = getArguments().getString(ARG_NAME);
            price = getArguments().getString(ARG_PRICE);
            description = getArguments().getString(ARG_DESCRIPTION);
            sellerName = getArguments().getString(ARG_SELLER_NAME);
            sellerContact = getArguments().getString(ARG_SELLER_CONTACT);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_market_item_details, container, false);

        TextView nameTextView = view.findViewById(R.id.item_name);
        TextView priceTextView = view.findViewById(R.id.item_price);
        TextView descriptionTextView = view.findViewById(R.id.item_description);
        TextView sellerNameTextView = view.findViewById(R.id.seller_name);
        TextView sellerContactTextView = view.findViewById(R.id.seller_contact);
        Button emailSellerButton = view.findViewById(R.id.button_email_seller);

        nameTextView.setText(name);
        priceTextView.setText("$" + price);
        descriptionTextView.setText(description);
        sellerNameTextView.setText(sellerName);
        sellerContactTextView.setText(sellerContact);

        emailSellerButton.setOnClickListener(v -> {
            if (sellerContact != null && !sellerContact.isEmpty()) {
                sendEmailToSeller(sellerContact);
            } else {
                Toast.makeText(getContext(), "Seller email not available", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void sendEmailToSeller(String email) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", email, null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Inquiry about your item");
        startActivity(Intent.createChooser(emailIntent, "Send email..."));
    }
}
