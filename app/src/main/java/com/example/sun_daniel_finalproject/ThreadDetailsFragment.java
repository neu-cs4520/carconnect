package com.example.sun_daniel_finalproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ThreadDetailsFragment extends Fragment {

    private static final String ARG_THREAD_ID = "threadId";

    private String threadId;
    private TextView titleTextView, contentTextView, authorTextView, likeCountTextView;
    private RecyclerView repliesRecyclerView;
    private RepliesAdapter repliesAdapter;
    private List<Reply> replyList;
    private FloatingActionButton fabReply;
    private Button likeButton;
    private FirebaseFirestore db;
    private int likeCount;

    public static ThreadDetailsFragment newInstance(String threadId) {
        ThreadDetailsFragment fragment = new ThreadDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_THREAD_ID, threadId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            threadId = getArguments().getString(ARG_THREAD_ID);
        }
        db = FirebaseFirestore.getInstance();
        replyList = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_thread_details, container, false);
        titleTextView = view.findViewById(R.id.thread_title);
        contentTextView = view.findViewById(R.id.thread_content);
        authorTextView = view.findViewById(R.id.thread_author);
        repliesRecyclerView = view.findViewById(R.id.recycler_view_replies);
        fabReply = view.findViewById(R.id.fabReply);
        likeButton = view.findViewById(R.id.button_like);
        likeCountTextView = view.findViewById(R.id.thread_likes);

        repliesAdapter = new RepliesAdapter(replyList);
        repliesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        repliesRecyclerView.setAdapter(repliesAdapter);


        if (threadId != null) {
            loadThreadDetails(threadId);
            loadReplies(threadId);
        } else {
            Toast.makeText(getContext(), "Thread ID not provided", Toast.LENGTH_SHORT).show();
        }

        fabReply.setOnClickListener(v -> showAddReplyDialog());
        likeButton.setOnClickListener(v -> updateLikeCount());

        return view;
    }

    private void loadThreadDetails(String threadId) {
        db.collection("threads").document(threadId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Thread thread = documentSnapshot.toObject(Thread.class);
                        if (thread != null) {
                            titleTextView.setText(thread.getTitle());
                            contentTextView.setText(thread.getContent());
                            authorTextView.setText(thread.getAuthor());
                            likeCount = thread.getLikes();
                            likeCountTextView.setText(String.valueOf(likeCount));
                        }
                    } else {
                        Toast.makeText(getContext(), "Thread not found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Failed to load thread details", Toast.LENGTH_SHORT).show();
                });
    }

    private void loadReplies(String threadId) {
        db.collection("threads").document(threadId).collection("replies").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    replyList.clear();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Reply reply = document.toObject(Reply.class);
                        replyList.add(reply);
                    }
                    repliesAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Failed to load replies", Toast.LENGTH_SHORT).show();
                });
    }

    private void showAddReplyDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Add Reply");

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_reply, null);
        builder.setView(dialogView);

        TextInputEditText replyContentInput = dialogView.findViewById(R.id.reply_content_input);

        builder.setPositiveButton("Add", (dialog, which) -> {
            String content = replyContentInput.getText().toString().trim();
            if (!content.isEmpty()) {
                addReplyToDatabase(content);
            } else {
                Toast.makeText(getContext(), "Please enter a reply", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void addReplyToDatabase(String content) {
        if (threadId != null) {
            DocumentReference threadRef = db.collection("threads").document(threadId);
            DocumentReference newReplyRef = threadRef.collection("replies").document();
            Reply reply = new Reply(content);

            newReplyRef.set(reply)
                    .addOnSuccessListener(aVoid -> {
                        replyList.add(reply);
                        repliesAdapter.notifyDataSetChanged();
                        Toast.makeText(getContext(), "Reply added successfully", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getContext(), "Failed to add reply: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }

    private void updateLikeCount() {
        likeCount++;
        likeCountTextView.setText(String.valueOf(likeCount));
        if (threadId != null) {
            db.collection("threads").document(threadId)
                    .update("likes", likeCount)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(getContext(), "Like added", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getContext(), "Failed to add like", Toast.LENGTH_SHORT).show();
                    });
        }
    }
}
