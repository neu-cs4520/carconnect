package com.example.sun_daniel_finalproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CarsAdapter extends RecyclerView.Adapter<CarsAdapter.CarsViewHolder> {

    private List<Car> carList;

    public CarsAdapter(List<Car> carList) {
        this.carList = carList;
    }

    @NonNull
    @Override
    public CarsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_car, parent, false);
        return new CarsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarsViewHolder holder, int position) {
        Car currentCar = carList.get(position);
        holder.carName.setText(currentCar.getMake() + " " + currentCar.getModel());
        holder.carYear.setText(currentCar.getYear());
        holder.carColor.setText(currentCar.getColor());
    }

    @Override
    public int getItemCount() {
        return carList.size();
    }

    public static class CarsViewHolder extends RecyclerView.ViewHolder {
        public TextView carName;
        public TextView carYear;
        public TextView carColor;

        public CarsViewHolder(@NonNull View itemView) {
            super(itemView);
            carName = itemView.findViewById(R.id.car_name);
            carYear = itemView.findViewById(R.id.car_year);
            carColor = itemView.findViewById(R.id.car_color);
        }
    }
}
