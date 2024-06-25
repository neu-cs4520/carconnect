package com.example.sun_daniel_finalproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ForumsFragment extends Fragment {

    private Spinner carMakeSpinner;
    private RecyclerView threadsRecyclerView;
    private ThreadAdapter threadAdapter;
    private List<Thread> threadList;
    private FirebaseFirestore db;
    private ExtendedFloatingActionButton addThreadFab;
    FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forums, container, false);

        carMakeSpinner = view.findViewById(R.id.car_make_spinner);
        threadsRecyclerView = view.findViewById(R.id.threads_recycler_view);
        addThreadFab = view.findViewById(R.id.add_thread_fab);

        threadsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Set up the car make spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.car_makes_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        carMakeSpinner.setAdapter(adapter);

        threadList = new ArrayList<>();
        threadAdapter = new ThreadAdapter(threadList);
        threadsRecyclerView.setAdapter(threadAdapter);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        loadThreads("All");

        carMakeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedMake = parent.getItemAtPosition(position).toString();
                loadThreads(selectedMake);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                loadThreads("All");
            }
        });

        addThreadFab.setOnClickListener(v -> showAddThreadDialog());

        return view;
    }

    private void loadThreads(String carMake) {
        CollectionReference threadsRef = db.collection("threads");
        threadsRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                threadList.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Thread thread = document.toObject(Thread.class);
                    if (carMake.equals("All") || thread.getCarMake().equals(carMake)) {
                        threadList.add(thread);
                    }
                }
                threadAdapter.notifyDataSetChanged();
            } else {
                // Handle the error
            }
        });
    }

    private void showAddThreadDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Add New Post");

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_thread, null);
        builder.setView(dialogView);

        TextInputEditText threadTitleInput = dialogView.findViewById(R.id.thread_title_input);
        Spinner threadCarMakeSpinner = dialogView.findViewById(R.id.thread_car_make_spinner);
        TextInputEditText threadContentInput = dialogView.findViewById(R.id.thread_content_input);

        // Set up the car make spinner in the dialog
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.car_makes_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        threadCarMakeSpinner.setAdapter(adapter);

        builder.setPositiveButton("Add", (dialog, which) -> {
            String title = threadTitleInput.getText().toString().trim();
            String carMake = threadCarMakeSpinner.getSelectedItem().toString();
            String content = threadContentInput.getText().toString().trim();

            if (!title.isEmpty() && !carMake.isEmpty() && !content.isEmpty()) {
                Thread thread = new Thread(title, content, mAuth.getUid(), carMake);
                addThreadToDatabase(thread);
            } else {
                Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void addThreadToDatabase(Thread thread) {
        DocumentReference newThreadRef = db.collection("threads").document();
        String threadId = newThreadRef.getId();
        thread.setThreadId(threadId);

        newThreadRef.set(thread)
                .addOnSuccessListener(aVoid -> {
                    threadList.add(thread);
                    threadAdapter.notifyDataSetChanged();
                    Toast.makeText(getContext(), "Thread added successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Failed to add thread: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
