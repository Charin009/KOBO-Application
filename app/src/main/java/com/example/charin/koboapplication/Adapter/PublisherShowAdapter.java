package com.example.charin.koboapplication.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.charin.koboapplication.BookActivity;
import com.example.charin.koboapplication.Object.Publisher;
import com.example.charin.koboapplication.PublisherActivity;
import com.example.charin.koboapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.ArrayList;

public class PublisherShowAdapter extends RecyclerView.Adapter<PublisherShowAdapter.PublisherShowViewHolder> {

    //Parameter
    private Context context ;
    private ArrayList<Publisher> publishersList;
    private ArrayList<String> publisherDB;
    private DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users");
    private static String dbRef;

    //Constructor
    public PublisherShowAdapter(Context context, ArrayList<Publisher> publishersList,ArrayList<String> publisherDB) {
        this.context = context;
        this.publishersList = publishersList;
        this.publisherDB = publisherDB;
    }

    // Override OnCreateViewHolder
    @NonNull
    @Override
    public PublisherShowViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new PublisherShowViewHolder(LayoutInflater.from(context).inflate(R.layout.publisher_show_card,viewGroup,false));
    }

    //Set behavior of viewHolder
    @Override
    public void onBindViewHolder(@NonNull PublisherShowViewHolder viewHolder, final int position) {
            final String publisherName = publishersList.get(position).getPublisher_name();

            viewHolder.Publisher_Name(publisherName);
            viewHolder.Publisher_IMG(publishersList.get(position).getPublisher_img());
            viewHolder.Publisher_Amount(publishersList.get(position).getPublisher_amount());

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dbRef = publisherDB.get(position);
                    Intent i = new Intent(context, BookActivity.class);
                    i.putExtra("DBReference",dbRef);
                    context.startActivity(i);
                }
            });

            viewHolder.removeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dbRef = publisherDB.get(position);
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("this publisher will disappear from you publisher list").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            userRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("User_Publisher")
                                    .child(dbRef).removeValue();
                            publishersList.remove(position);
                            publisherDB.remove(position);
                            notifyItemRemoved(position);
                            Intent i = new Intent(context, PublisherActivity.class);
                            context.startActivity(i);
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.setTitle("ัDelete Publisher?");
                    dialog.show();
                }
            });


    }

    @Override
    public int getItemCount() {
        return publishersList.size();
    }


    //ViewHolder class
    class PublisherShowViewHolder extends RecyclerView.ViewHolder {
        private  TextView publisher_name;
        private  ImageView publisher_img;
        private  TextView publisher_amount;
        public ImageButton removeBtn;

        public PublisherShowViewHolder(View itemView) {
            super(itemView);
            publisher_name = (TextView) itemView.findViewById(R.id.publisherNameShow);
            publisher_img = (ImageView) itemView.findViewById(R.id.publisherShowIMG);
            publisher_amount = (TextView) itemView.findViewById(R.id.publisherAmountShow);
            removeBtn = (ImageButton) itemView.findViewById(R.id.removePublisherBtn);
        }

        private void Publisher_Name(String name){
            publisher_name.setText(name);
        }

        private void Publisher_IMG(String url){
            Glide.with(itemView.getContext()).load(url).crossFade().placeholder(R.drawable.googleg_disabled_color_18).thumbnail(01.f).diskCacheStrategy(DiskCacheStrategy.ALL).into(publisher_img);
        }

        private void Publisher_Amount(int amount){
            publisher_amount.setText("New Book:  " + amount);
        }
    }
}
