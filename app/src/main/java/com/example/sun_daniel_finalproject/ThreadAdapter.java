package com.example.sun_daniel_finalproject;

import android.content.Context;
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

    public ThreadAdapter(List<Thread> threadList) {
        this.threadList = threadList;
    }

    @NonNull
    @Override
    public ThreadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_thread, parent, false);
        return new ThreadViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ThreadViewHolder holder, int position) {
        Thread thread = threadList.get(position);
        holder.titleTextView.setText(thread.getTitle());
        holder.contentTextView.setText(thread.getContent());
        holder.itemView.setOnClickListener(v -> {
            Context context = holder.itemView.getContext();
            if (context instanceof FragmentActivity) {
                FragmentActivity activity = (FragmentActivity) context;
                ThreadDetailsFragment threadDetailsFragment = ThreadDetailsFragment.newInstance(thread.getThreadId());
                activity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, threadDetailsFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return threadList.size();
    }

    static class ThreadViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, contentTextView;

        public ThreadViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.thread_title);
            contentTextView = itemView.findViewById(R.id.thread_content);
        }
    }
}
