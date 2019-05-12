package com.example.charin.koboapplication;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.charin.koboapplication.Adapter.BookAdapter;
import com.example.charin.koboapplication.Object.Book;
import com.example.charin.koboapplication.Object.Publisher;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BookActivity extends AppCompatActivity {
    private RecyclerView recycler;
    private BookAdapter adapter;
    private ArrayList<Book> bookList,userBookList;
    private String publisherDBRef;
    private ImageView icon;
    private FloatingActionButton addBook;
    private Button backBtn;
    private DatabaseReference bookRef;
    private DatabaseReference urlRef;
    private DatabaseReference userBookRef;
    private String iconRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        recycler = (RecyclerView) findViewById(R.id.bookList);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        icon = (ImageView) findViewById(R.id.publisherBookIMG);
        addBook = (FloatingActionButton) findViewById(R.id.addBooktoDBBtn);
        backBtn = (Button) findViewById(R.id.backBtn);

        publisherDBRef = getIntent().getStringExtra("DBReference");
        bookRef = FirebaseDatabase.getInstance().getReference().child("Publishers").child(publisherDBRef).child("Books");
        urlRef = FirebaseDatabase.getInstance().getReference().child("Publishers").child(publisherDBRef);
        userBookRef = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Like_Book");

        urlRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Publisher publisher = dataSnapshot.getValue(Publisher.class);
                    iconRef = publisher.getPublisher_img();
                    Glide.with(getApplicationContext()).load(iconRef).crossFade().placeholder(R.drawable.googleg_disabled_color_18).thumbnail(01.f).diskCacheStrategy(DiskCacheStrategy.ALL).into(icon);
                    Log.d("IMG",iconRef);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        userBookRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userBookList = new ArrayList<Book>();

                for(DataSnapshot data: dataSnapshot.getChildren()) {
                    Book userBook = data.getValue(Book.class);

                    userBookList.add(userBook);
                    Log.d("UserB",userBook.getName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Wait for userBookRef
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                bookRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        bookList = new ArrayList<Book>();

                        for(DataSnapshot data: dataSnapshot.getChildren()) {
                            Book book = data.getValue(Book.class);
                            bookList.add(book);
                            Log.d("B",book.getName());
                        }

                        adapter = new BookAdapter(BookActivity.this,bookList,userBookList,publisherDBRef);
                        recycler.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }, 2000);


        addBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(BookActivity.this, AddBookActivity.class);
                i.putExtra("DBReference",publisherDBRef);
                startActivity(i);
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(BookActivity.this, PublisherActivity.class);
                startActivity(i);
                finish();
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(BookActivity.this, PublisherActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();

    }
}
