package com.example.charin.koboapplication.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.charin.koboapplication.AddPublisherActivity;
import com.example.charin.koboapplication.Object.Publisher;
import com.example.charin.koboapplication.PublisherActivity;
import com.example.charin.koboapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.ArrayList;

public class PublisherDBAdapter extends RecyclerView.Adapter<PublisherDBAdapter.PublisherViewHolder> {

    private Context context ;
    private ArrayList<Publisher> publishersList;
    private ArrayList<String> publisherDB;
    private DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users");
    public PublisherDBAdapter(Context context, ArrayList<Publisher> publishersList,ArrayList<String> publisherDB) {
        this.context = context;
        this.publishersList = publishersList;
        this.publisherDB = publisherDB;
    }

    @NonNull
    @Override
    public PublisherViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new PublisherViewHolder(LayoutInflater.from(context).inflate(R.layout.publisher_db_card,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull PublisherViewHolder viewHolder, final int position) {
        final String publisherName = publishersList.get(position).getPublisher_name();

        viewHolder.Publisher_Name(publisherName);
        viewHolder.Publisher_IMG(publishersList.get(position).getPublisher_img());
        viewHolder.Publisher_Amount(publishersList.get(position).getPublisher_amount());

        viewHolder.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("User_Publisher").child(publisherDB.get(position)).setValue(publisherName);
                Intent i = new Intent(context, AddPublisherActivity.class);
                context.startActivity(i);
                ((Activity)context).finish();
            }
        });

    }

    @Override
    public int getItemCount() {
        return publishersList.size();
    }

    class PublisherViewHolder extends RecyclerView.ViewHolder {
        private  TextView publisher_name;
        private  ImageView publisher_img;
        private  TextView publisher_amount;
        public ImageButton addBtn;

        public PublisherViewHolder(View itemView) {
            super(itemView);
            publisher_name = (TextView) itemView.findViewById(R.id.publisherNameDB);
            publisher_img = (ImageView) itemView.findViewById(R.id.publisherIMG);
            publisher_amount = (TextView) itemView.findViewById(R.id.publisherAmountDB);
            addBtn = (ImageButton) itemView.findViewById(R.id.addPublisherBtn);
        }

        private void Publisher_Name(String name){
            publisher_name.setText(name);
        }

        private void Publisher_IMG(String url){
            Glide.with(itemView.getContext()).load(url).crossFade().placeholder(R.drawable.googleg_disabled_color_18).thumbnail(01.f).diskCacheStrategy(DiskCacheStrategy.ALL).into(publisher_img);
        }

        private void Publisher_Amount(int amount){
            publisher_amount.setText("New Book:" + amount);
        }
    }
}
