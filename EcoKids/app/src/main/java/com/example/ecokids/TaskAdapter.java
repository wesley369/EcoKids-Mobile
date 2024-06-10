package com.example.ecokids;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private Context context;
    private List<Task> taskList;
    private Map<String, Boolean> userTasksStatus = new HashMap<>();
    private DatabaseReference userTasksRef;
    private DatabaseReference userRef;

    public TaskAdapter(Context context, List<Task> taskList) {
        this.context = context;
        this.taskList = taskList;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            userTasksRef = FirebaseDatabase.getInstance().getReference("userTasks").child(userId);
            userRef = FirebaseDatabase.getInstance().getReference("users").child(userId);
        }
    }

    public void setUserTasksStatus(Map<String, Boolean> userTasksStatus) {
        this.userTasksStatus = userTasksStatus;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_task, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.taskDescription.setText(task.getDescription());

        // Remove the listener before setting the checked state
        holder.taskCompleted.setOnCheckedChangeListener(null);

        Boolean completed = userTasksStatus.get(task.getId());
        holder.taskCompleted.setChecked(completed != null && completed);

        holder.taskCompleted.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (userTasksRef != null) {
                userTasksStatus.put(task.getId(), isChecked);
                userTasksRef.child(task.getId()).setValue(isChecked);
                updateUserPoints(isChecked, task.getPoints());
            }
        });
    }

    private void updateUserPoints(boolean isChecked, int points) {
        userRef.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                User user = currentData.getValue(User.class);
                if (user == null) {
                    return Transaction.success(currentData);
                }

                if (isChecked) {
                    user.setPoints(user.getPoints() + points);
                } else {
                    user.setPoints(user.getPoints() - points);
                }

                currentData.setValue(user);
                return Transaction.success(currentData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean committed, DataSnapshot dataSnapshot) {
                if (databaseError != null) {
                    Toast.makeText(context, "Erro ao atualizar pontos: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView taskDescription;
        CheckBox taskCompleted;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            taskDescription = itemView.findViewById(R.id.taskDescription);
            taskCompleted = itemView.findViewById(R.id.taskCompleted);
        }
    }
}
