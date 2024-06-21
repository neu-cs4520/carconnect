package com.example.sun_daniel_finalproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ThreadAdapter extends RecyclerView.Adapter<ThreadAdapter.ThreadViewHolder> {

    private List<Thread> threadList;
    private FragmentActivity fragmentActivity;

    public ThreadAdapter(List<Thread> threadList, FragmentActivity fragmentActivity) {
        this.threadList = threadList;
        this.fragmentActivity = fragmentActivity;
    }

    @NonNull
    @Override
    public ThreadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_thread, parent, false);
        return new ThreadViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ThreadViewHolder holder, int position) {
        Thread currentThread = threadList.get(position);
        holder.titleTextView.setText(currentThread.getTitle());
        holder.contentTextView.setText(currentThread.getContent());
        holder.likesTextView.setText(String.valueOf(currentThread.getLikes()));

        holder.itemView.setOnClickListener(v -> {
            ThreadDetailsFragment detailsFragment = ThreadDetailsFragment.newInstance(
                    currentThread.getTitle(),
                    currentThread.getContent(),
                    currentThread.getLikes());

            fragmentActivity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, detailsFragment)
                    .addToBackStack(null)
                    .commit();
        });
    }

    @Override
    public int getItemCount() {
        return threadList.size();
    }

    public static class ThreadViewHolder extends RecyclerView.ViewHolder {

        public TextView titleTextView;
        public TextView contentTextView;
        public TextView likesTextView;

        public ThreadViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.thread_title);
            contentTextView = itemView.findViewById(R.id.thread_content);
            likesTextView = itemView.findViewById(R.id.thread_likes);
        }
    }
}
