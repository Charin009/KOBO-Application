package com.example.charin.koboapplication;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.charin.koboapplication.Object.Book;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class AddBookActivity extends AppCompatActivity {
    public static final int READ_EXTERNAL_STORAGE = 0;
    public static final int GALLERY_INTENT = 2;
    
    private String publisherRef,bookName,bookIMG,bookAuthor;
    private int bookPrice, bookVolume,lastID,publisherAmount;
    private EditText name, author, volume, price;
    private Button addIMG, addBook;
    private ImageView img;
    private ArrayList<String> bookIDList;

    private Uri mImageUri = null;
    private DatabaseReference bookDBRef ;
    private DatabaseReference publisherDBRef;
    private StorageReference mStorage;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        mProgressDialog = new ProgressDialog(AddBookActivity.this);

        publisherRef = getIntent().getStringExtra("DBReference");
        publisherDBRef = FirebaseDatabase.getInstance().getReference().child("Publishers").child(publisherRef).child("publisher_amount");
        bookDBRef = FirebaseDatabase.getInstance().getReference().child("Publishers").child(publisherRef).child("Books");
        mStorage = FirebaseStorage.getInstance().getReferenceFromUrl("gs://kobo-91dcf.appspot.com/");
        bookIMG="";

        name = (EditText) findViewById(R.id.bookNameAdd);
        author = (EditText) findViewById(R.id.bookAuthorAdd);
        volume = (EditText) findViewById(R.id.bookVolumeAdd);
        price = (EditText) findViewById(R.id.bookPriceAdd);
        addIMG = (Button) findViewById(R.id.addIMGBtn);
        addBook = (Button) findViewById(R.id.addBookBtn);
        img = (ImageView) findViewById(R.id.bookIMGAdd);

    }

    @Override
    protected void onStart() {
        super.onStart();

        publisherDBRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                publisherAmount = dataSnapshot.getValue(Integer.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        addIMG.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Call for permission", Toast.LENGTH_SHORT).show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE);
                    }
                }
                else {
                    callGallery();
                }
            }
        });

        addBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bookName = name.getText().toString();
                bookAuthor = author.getText().toString();

                if(bookName.isEmpty() ||bookAuthor.isEmpty()||volume.getText().toString().isEmpty()||price.getText().toString().isEmpty()||bookIMG.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Please fill all information", Toast.LENGTH_SHORT).show();
                }else{
                    bookVolume = Integer.valueOf(volume.getText().toString());
                    bookPrice = Integer.valueOf(price.getText().toString());

                    Book book = new Book(bookName,bookAuthor,bookVolume,bookPrice,bookIMG,0);
                    bookDBRef.child(bookName+"_"+bookVolume).setValue(book);
                    publisherDBRef.setValue(publisherAmount+1);

                    Intent i = new Intent(AddBookActivity.this, BookActivity.class);
                    i.putExtra("DBReference", publisherRef);
                    startActivity(i);
                }
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode) {
            case READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) callGallery();
                return;
        }
    }

    //    call Gallery
    public void callGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY_INTENT);
    }
    //    upload to firebase
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK) {
            mImageUri = data.getData();
            img.setImageURI(mImageUri);
            StorageReference filePath = mStorage.child("Book_Images").child(mImageUri.getLastPathSegment());

            mProgressDialog.setMessage("Uploading...");
            mProgressDialog.show();

            filePath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> downloadUri = taskSnapshot.getStorage().getDownloadUrl();

                    downloadUri.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri downloadUri) {
                            bookIMG = downloadUri.toString();
                            Glide.with(getApplicationContext())
                                    .load(downloadUri)
                                    .crossFade()
                                    .placeholder(R.drawable.googleg_disabled_color_18)
                                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                                    .into(img);
                            mProgressDialog.dismiss();
                        }
                    });


                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(AddBookActivity.this, BookActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();

    }
}
