package com.example.todo.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo.MainActivity;
import com.example.todo.Model.ToDoModel;
import com.example.todo.R;
import com.example.todo.newTask;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ToDOAdapter extends RecyclerView.Adapter<ToDOAdapter.MyViewHolder> {
    private List<ToDoModel> todolist;
    private MainActivity activity;
    private FirebaseFirestore firestore;


    public class MyViewHolder extends RecyclerView.ViewHolder{

        CheckBox checkbox;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            checkbox = itemView.findViewById(R.id.RViewCheckBox);
        }
    }

    public ToDOAdapter(MainActivity mainActivity, List<ToDoModel> todolist){
        this.todolist = todolist;
        activity = mainActivity;

    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.tasklayout, parent, false);
        firestore= FirebaseFirestore.getInstance();
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ToDoModel toDoModel = todolist.get(position);

       holder.checkbox.setText(toDoModel.getTask());

       holder.checkbox.setChecked(toBoolean(toDoModel.getStatus()));

       holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    firestore.collection("task").document(toDoModel.TaskId).update("status", 1);
                }else{
                    firestore.collection("task").document(toDoModel.TaskId).update("status", 0);
                }

            }
        });


    }

    private boolean toBoolean(int status){
        return status!=0;

    }

    public void deleteTask(int position){
        ToDoModel toDoModel = todolist.get(position);
        firestore.collection("task").document(toDoModel.TaskId).delete();
        todolist.remove(position);
        notifyItemRemoved(position);

    }

    public Context getContext(){
        return activity;
    }

    public void editTask(int position){
        ToDoModel toDoModel = todolist.get(position);

        Bundle bundle = new Bundle();
        bundle.putString("task", toDoModel.getTask());
        bundle.putString("id", toDoModel.TaskId);

        newTask newTask = new newTask();
        newTask.setArguments(bundle);
        newTask.show(activity.getSupportFragmentManager(), newTask.getTag());
        firestore.collection("task").document(toDoModel.TaskId).delete();
        todolist.remove(position);
        notifyItemRemoved(position);

    }



    @Override
    public int getItemCount() {
        return todolist.size();
    }


}
