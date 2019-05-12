package com.example.charin.koboapplication;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.charin.koboapplication.Adapter.PublisherDBAdapter;
import com.example.charin.koboapplication.Object.Publisher;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddPublisherActivity extends AppCompatActivity {
    private RecyclerView recycler;
    private PublisherDBAdapter adapter;
    private ArrayList<Publisher> publisherList;
    private ArrayList<String> publisherDBList;
    private ArrayList<String> checkList;
    private DatabaseReference myRef;
    private DatabaseReference userPub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_publisher);

        recycler = (RecyclerView) findViewById(R.id.publisherList);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        userPub = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("User_Publisher");
        myRef = FirebaseDatabase.getInstance().getReference().child("Publishers");
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

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                publisherList = new ArrayList<Publisher>();
                publisherDBList = new ArrayList<String>();

                for(DataSnapshot data: dataSnapshot.getChildren()) {

                    Publisher publisher = data.getValue(Publisher.class);
                    if(!checkList.contains(publisher.getPublisher_name())){
                        publisherList.add(publisher);
                        String name = data.getKey();
                        publisherDBList.add(name);
                    }
                }

                adapter = new PublisherDBAdapter(AddPublisherActivity.this,publisherList,publisherDBList);
                recycler.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AddPublisherActivity.this, "Opsss.... Something is wrong", Toast.LENGTH_SHORT).show();
            }
        });


    }

}
