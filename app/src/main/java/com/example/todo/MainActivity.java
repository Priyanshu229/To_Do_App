package com.example.todo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.todo.Adapter.ToDOAdapter;
import com.example.todo.Model.ToDoModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button logoutBtn;
    FirebaseAuth mAuth;
    FirebaseUser User;

    private FirebaseFirestore fireStore;
    private ToDOAdapter adapter;
    private List<ToDoModel> mList;



    RecyclerView recyclerView;
    FloatingActionButton floatingButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        User = mAuth.getCurrentUser();
        fireStore = FirebaseFirestore.getInstance();


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

        mList = new ArrayList<>();
        adapter = new ToDOAdapter(MainActivity.this, mList);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new TouchHelper(adapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);

        recyclerView.setAdapter(adapter);
        showData();






        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mAuth.signOut();
                Intent intent = new Intent(getApplicationContext(), login.class);
                startActivity(intent);

            }
        });
    }

    private void showData(){
        fireStore.collection("task").orderBy("time", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for(DocumentChange documentChange : value.getDocumentChanges()){
                    if(documentChange.getType()== DocumentChange.Type.ADDED){
                        String id=documentChange.getDocument().getId();
                        ToDoModel toDoModel = documentChange.getDocument().toObject(ToDoModel.class).withId(id);

                        mList.add(toDoModel);
                        adapter.notifyDataSetChanged();

                    }
                }

            }
        });

    }
}