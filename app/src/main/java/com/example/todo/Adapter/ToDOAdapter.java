package com.example.todo.Adapter;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.todo.AlarmReceiver;

import com.example.todo.MainActivity;
import com.example.todo.Model.ToDoModel;
import com.example.todo.R;
import com.example.todo.newTask;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ToDOAdapter extends RecyclerView.Adapter<ToDOAdapter.MyViewHolder> {
    private List<ToDoModel> todolist;
    private MainActivity activity;
    private FirebaseFirestore firestore;



    public class MyViewHolder extends RecyclerView.ViewHolder{

        CheckBox checkbox;
        TextView taskDateTextView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            checkbox = itemView.findViewById(R.id.RViewCheckBox);
            taskDateTextView = itemView.findViewById(R.id.due_date_tv);

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
        holder.taskDateTextView.setText(toDoModel.getDue());

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

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        try {
            Date dueDate = dateFormat.parse(toDoModel.getDue());

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dueDate);

            // Call the method to set the alarm for the due date
            setAlarmForDueDate(calendar);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void setAlarmForDueDate(Calendar calendar) {
        AlarmManager alarmManager = (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(activity, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(activity, 0, intent, 0);

        if (alarmManager != null) {
            // Set the alarm to trigger at the specified time
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }


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

    }



    @Override
    public int getItemCount() {
        return todolist.size();
    }


}
