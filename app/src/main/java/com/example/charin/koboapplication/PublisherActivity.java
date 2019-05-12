package com.example.charin.koboapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.charin.koboapplication.Adapter.PublisherShowAdapter;
import com.example.charin.koboapplication.Object.Publisher;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class PublisherActivity extends AppCompatActivity {
    private RecyclerView recycler;
    private PublisherShowAdapter adapter;
    private ArrayList<Publisher> publisherList;
    private ArrayList<String> checkList;
    private ArrayList<String> publisherDBList;
    private DatabaseReference userPub;
    private DatabaseReference pubRef;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publisher);


        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        recycler = (RecyclerView) findViewById(R.id.publisherListUser);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        userPub = FirebaseDatabase.getInstance().getReference().child("Users").child(userID).child("User_Publisher");
        pubRef = FirebaseDatabase.getInstance().getReference().child("Publishers");


        FloatingActionButton fab = findViewById(R.id.addBtn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PublisherActivity.this, AddPublisherActivity.class);
                startActivity(i);
            }
        });

        Button signOut = findViewById(R.id.signoutBtn);
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(PublisherActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        userPub.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                checkList = new ArrayList<String>();

                for(DataSnapshot data: dataSnapshot.getChildren()) {
                    String name = data.getValue(String.class);
                    checkList.add(name);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                pubRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        publisherList = new ArrayList<Publisher>();
                        publisherDBList = new ArrayList<String>();

                        for(DataSnapshot data: dataSnapshot.getChildren()) {
                            Publisher publisher = data.getValue(Publisher.class);
                            String name = data.getKey();

                            if(checkList == null)return;
                            if(checkList.contains(publisher.getPublisher_name())){
                                publisherList.add(publisher);
                                publisherDBList.add(name);
                            }
                        }

                        adapter = new PublisherShowAdapter(PublisherActivity.this,publisherList,publisherDBList);
                        recycler.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(PublisherActivity.this, "Opsss.... Something is wrong", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }, 1000);

    }
}

