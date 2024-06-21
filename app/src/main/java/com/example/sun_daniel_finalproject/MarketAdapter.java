package com.example.sun_daniel_finalproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MarketAdapter extends RecyclerView.Adapter<MarketAdapter.MarketViewHolder> {

    private List<MarketItem> marketItemList;
    private FragmentActivity fragmentActivity;

    public MarketAdapter(List<MarketItem> marketItemList, FragmentActivity fragmentActivity) {
        this.marketItemList = marketItemList;
        this.fragmentActivity = fragmentActivity;
    }

    @NonNull
    @Override
    public MarketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_market, parent, false);
        return new MarketViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MarketViewHolder holder, int position) {
        MarketItem currentItem = marketItemList.get(position);
        holder.nameTextView.setText(currentItem.getName());
        holder.priceTextView.setText("$" + currentItem.getPrice());
        holder.descriptionTextView.setText(currentItem.getDescription());
        holder.sellerNameTextView.setText(currentItem.getSellerName());

        holder.itemView.setOnClickListener(v -> {
            MarketItemDetailsFragment detailsFragment = MarketItemDetailsFragment.newInstance(
                    currentItem.getName(),
                    currentItem.getPrice(),
                    currentItem.getDescription(),
                    currentItem.getSellerName(),
                    currentItem.getSellerContact());

            fragmentActivity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, detailsFragment)
                    .addToBackStack(null)
                    .commit();
        });
    }

    @Override
    public int getItemCount() {
        return marketItemList.size();
    }

    public static class MarketViewHolder extends RecyclerView.ViewHolder {

        public TextView nameTextView;
        public TextView priceTextView;
        public TextView descriptionTextView;
        public TextView sellerNameTextView;

        public MarketViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.item_name);
            priceTextView = itemView.findViewById(R.id.item_price);
            descriptionTextView = itemView.findViewById(R.id.item_description);
            sellerNameTextView = itemView.findViewById(R.id.seller_name);
        }
    }
}
