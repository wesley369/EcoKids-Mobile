package com.example.ecokids;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ToDoListActivity extends AppCompatActivity {

    private RecyclerView todoRecyclerView;
    private TaskAdapter taskAdapter;
    private List<Task> taskList = new ArrayList<>();
    private Map<String, Boolean> userTasksStatus = new HashMap<>();

    private Toolbar toolbar;
    private DatabaseReference usersRef;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        todoRecyclerView = findViewById(R.id.todoRecyclerView);
        todoRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        taskAdapter = new TaskAdapter(this, taskList);
        todoRecyclerView.setAdapter(taskAdapter);

        usersRef = FirebaseDatabase.getInstance().getReference("users");
        db = FirebaseFirestore.getInstance();

        loadTasks();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.home) {
            startActivity(new Intent(this, MainActivity.class));
            return true;
        } else if (itemId == R.id.Metas) {
            startActivity(new Intent(this, ToDoListActivity.class));
            return true;
        } else if (itemId == R.id.Ranking) {
            startActivity(new Intent(this, RankingActivity.class));
            return true;
        } else if (itemId == R.id.Login) {
            startActivity(new Intent(this, LoginActivity.class));
            return true;
        } else if (itemId == R.id.Forum) {
            startActivity(new Intent(this, ForumActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadTasks() {
        FirebaseDatabase.getInstance().getReference("tasks")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        taskList.clear();
                        for (DataSnapshot taskSnapshot : snapshot.getChildren()) {
                            Task task = taskSnapshot.getValue(Task.class);
                            if (task != null) {
                                task.setId(taskSnapshot.getKey());
                                taskList.add(task);
                            }
                        }
                        taskAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            loadUserData(userId);
        }
    }

    private void loadUserData(String userId) {
        loadUserDataFromFirestore(userId);
        loadUserPointsFromRealtimeDatabase(userId);
    }

    private void loadUserDataFromFirestore(String userId) {
        DocumentReference docRef = db.collection("users").document(userId);

        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String name = documentSnapshot.getString("name");
                    String avatarUrl = documentSnapshot.getString("avatarUrl");


                    updateUIWithUserData(name, avatarUrl);


                    updateUserDataInRealtimeDatabase(userId, name, avatarUrl);
                } else {
                    Log.d("Firestore", "Documento não encontrado.");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Firestore", "Erro ao acessar Firestore: " + e.getMessage());
            }
        });
    }

    private void updateUserDataInRealtimeDatabase(String userId, String name, String avatarUrl) {
        // Atualizar os dados do usuário no Realtime Database
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userId);
        userRef.child("name").setValue(name);
        userRef.child("avatarUrl").setValue(avatarUrl)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Firebase", "Erro ao atualizar dados do usuário no Realtime Database: " + e.getMessage());
                        Toast.makeText(ToDoListActivity.this, "Erro ao atualizar dados do usuário no Realtime Database. Tente novamente.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loadUserPointsFromRealtimeDatabase(String userId) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userId);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    if (dataSnapshot.hasChild("points")) {
                        Integer points = dataSnapshot.child("points").getValue(Integer.class);

                        if (points != null) {
                            updateUIWithUserPoints(points.intValue());
                        } else {
                            Log.d("RealtimeDatabase", "Pontos do usuário são nulos.");
                        }
                    } else {
                        userRef.child("points").setValue(0)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("RealtimeDatabase", "Nó 'points' inicializado para o usuário.");
                                        updateUIWithUserPoints(0);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.e("RealtimeDatabase", "Erro ao inicializar nó 'points': " + e.getMessage());

                                    }
                                });
                    }
                } else {
                    Log.d("RealtimeDatabase", "Usuário não encontrado.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("RealtimeDatabase", "Erro ao acessar Realtime Database: " + databaseError.getMessage());

            }
        });
    }

    private void updateUIWithUserData(String name, String avatarUrl) {

    }

    private void updateUIWithUserPoints(int points) {

    }
}