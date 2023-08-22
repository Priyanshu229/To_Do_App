package com.example.todo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    Button logoutBtn;
    FirebaseAuth mAuth;
    FirebaseUser User;



    RecyclerView recyclerView;
    FloatingActionButton floatingButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        User = mAuth.getCurrentUser();


        if(User == null){
            Intent intent = new Intent(getApplicationContext(), login.class);
            startActivity(intent);

        }



        recyclerView = findViewById(R.id.recyclerView);
        floatingButton = findViewById(R.id.floatingButton);
        logoutBtn = findViewById(R.id.logoutBtn);



        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));





        floatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               newTask.newInstance().show(getSupportFragmentManager(), newTask.TAG);

            }
        });




        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mAuth.signOut();
                Intent intent = new Intent(getApplicationContext(), login.class);
                startActivity(intent);

            }
        });
    }
}