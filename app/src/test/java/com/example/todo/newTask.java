package com.example.todo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.todo.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class newTask extends BottomSheetDialogFragment {

    public static final String TAG = "newTask";

    EditText addTask;
    Button saveBtn;


    public static newTask newInstance(){
        return new newTask();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.new_task, container , false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        addTask = view.findViewById(R.id.addTaskET);
        saveBtn = view.findViewById(R.id.saveBtn);
    }
}
