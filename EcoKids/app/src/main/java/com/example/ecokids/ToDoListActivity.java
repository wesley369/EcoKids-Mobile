package com.example.ecokids;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ToDoListActivity extends AppCompatActivity {

    private RecyclerView todoRecyclerView;
    private DatabaseReference tasksRef;
    private DatabaseReference userTasksRef;
    private FirebaseAuth mAuth;
    private List<Task> taskList;
    private TaskAdapter taskAdapter;
    private Map<String, Boolean> userTasksStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);

        todoRecyclerView = findViewById(R.id.todoRecyclerView);
        todoRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            String userId = user.getUid();
            tasksRef = FirebaseDatabase.getInstance().getReference("tasks");
            userTasksRef = FirebaseDatabase.getInstance().getReference("userTasks").child(userId);

            taskList = new ArrayList<>();
            taskAdapter = new TaskAdapter(this, taskList);
            todoRecyclerView.setAdapter(taskAdapter);

            loadTasks();
            loadUserTasks(userId);
        }
    }

    private void loadTasks() {
        tasksRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                taskList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Task task = snapshot.getValue(Task.class);
                    if (task != null) {
                        task.setId(snapshot.getKey());
                        taskList.add(task);
                    }
                }
                taskAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ToDoListActivity.this, "Falha ao carregar tarefas.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadUserTasks(String userId) {
        userTasksRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userTasksStatus = new HashMap<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    userTasksStatus.put(snapshot.getKey(), snapshot.getValue(Boolean.class));
                }
                taskAdapter.setUserTasksStatus(userTasksStatus);
                taskAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ToDoListActivity.this, "Falha ao carregar status das tarefas do usuário.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
