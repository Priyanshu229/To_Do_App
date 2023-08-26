package com.example.todo;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class newTask extends BottomSheetDialogFragment {

    public static final String TAG = "newTask";

    EditText addTask;
    private TextView setDueDate;
    private String dueDateUpdate = "";
    Button saveBtn;
    private String dueDate = "";
    private String id = "";

    private FirebaseFirestore firestore;
    private Context context;


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
        setDueDate = view.findViewById(R.id.set_due_tv);

        firestore = FirebaseFirestore.getInstance();
        context = getContext();
        boolean isUpdate = false;
        final Bundle bundle = getArguments();
        if(bundle!= null){
            isUpdate = true;
            String task = bundle.getString("task");
            id = bundle.getString("id");
            dueDateUpdate = bundle.getString("due");
            addTask.setText(task);
            setDueDate.setText(dueDateUpdate);

            if(task.length()>0){
                saveBtn.setEnabled(false);
                saveBtn.setBackgroundColor(Color.GRAY);
            }

        }

        setDueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();

                int MONTH = calendar.get(Calendar.MONTH);
                int YEAR = calendar.get(Calendar.YEAR);
                int DAY = calendar.get(Calendar.DATE);
                int HOUR = calendar.get(Calendar.HOUR_OF_DAY);
                int MINUTE = calendar.get(Calendar.MINUTE);

                DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month + 1;
                        dueDate = dayOfMonth + "/" + month + "/" + year;

                        TimePickerDialog timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                String formattedTime = String.format("%02d:%02d", hourOfDay, minute);
                                dueDate += " " + formattedTime;
                                setDueDate.setText(dueDate);
                            }
                        }, HOUR, MINUTE, true);

                        timePickerDialog.show();
                    }
                }, YEAR, MONTH, DAY);

                datePickerDialog.show();
            }
        });


        boolean finalIsUpdate = isUpdate;
        addTask.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals("")){
                    saveBtn.setEnabled(false);
                    saveBtn.setBackgroundColor(Color.GRAY);
                }else{
                    saveBtn.setEnabled(true);
                    saveBtn.setBackgroundColor(getResources().getColor(R.color.Orange));
                }

                saveBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String task;
                        task = addTask.getText().toString();
                        if(finalIsUpdate){
                            firestore.collection("task").document(id).update("task", task,  "due" , dueDate );
                            Toast.makeText(context, "Task Updated", Toast.LENGTH_SHORT).show();

                        }else {


                            if (task.isEmpty()) {
                                Toast.makeText(context, "Please enter the task", Toast.LENGTH_SHORT).show();

                            } else {
                                Map<String, Object> taskMap = new HashMap<>();
                                taskMap.put("task", task);
                                taskMap.put("due", dueDate);
                                taskMap.put("status", 0);
                                taskMap.put("time", FieldValue.serverTimestamp());

                                firestore.collection("task").add(taskMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentReference> task) {

                                        if (task.isSuccessful()) {
                                            Toast.makeText(context, "Task Saved", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();

                                    }
                                });


                            }
                        }
                        dismiss();

                    }
                });

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;

    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Activity activity = getActivity();
        if(activity instanceof OnDialogCloseListener){
            ((OnDialogCloseListener)activity).onDialogClose(dialog);
        }

    }
}
