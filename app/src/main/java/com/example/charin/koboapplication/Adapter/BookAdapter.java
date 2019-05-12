package com.example.charin.koboapplication.Adapter;

import android.content.Context;
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
import com.example.charin.koboapplication.Object.Book;
import com.example.charin.koboapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;


import java.util.ArrayList;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {

    //Parameter
    private Context context ;
    private ArrayList<Book> bookList;
    private ArrayList<Book> userBookList;
    private String publisherDBRef;

    //Constructor
    public BookAdapter(Context context, ArrayList<Book> bookList, ArrayList<Book> userBookList,String publisherDBRef) {
        this.context = context;
        this.bookList = bookList;
        this.userBookList = userBookList;
        this.publisherDBRef = publisherDBRef;
    }

    // Override OnCreateViewHolder
    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new BookViewHolder(LayoutInflater.from(context).inflate(R.layout.book_show_card,viewGroup,false));
    }

    //Set behavior of viewHolder
    @Override
    public void onBindViewHolder(@NonNull BookViewHolder viewHolder, final int position) {
        final Book book = bookList.get(position);

        if(userBookList != null) {
            for (Book userbook : userBookList) {
                if (userbook.getName().equals(book.getName()) && userbook.getVolume() == book.getVolume()) {
                    viewHolder.likeButton.setVisibility(View.INVISIBLE);
                }
            }
        }else{
            viewHolder.likeButton.setVisibility(View.VISIBLE);
        }

        viewHolder.setBookIMG(book.getImage());
        viewHolder.setBookName(book.getName());
        viewHolder.setBookAuthor(book.getAuthor());
        viewHolder.setBookVolume(book.getVolume());
        viewHolder.setBookPrice(book.getPrice());
        viewHolder.setLikeText(book.getLikeAmount());

        viewHolder.likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                book.setLikeAmount(book.getLikeAmount()+1);
                FirebaseDatabase.getInstance().getReference().child("Publishers")
                        .child(publisherDBRef).child("Books")
                        .child(book.getName()+"_"+book.getVolume())
                        .setValue(book);

                FirebaseDatabase.getInstance().getReference().child("Users")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("Like_Book").child(book.getName()+"_"+book.getVolume()).setValue(book);

            }
        });

    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }


    //ViewHolder class
    class BookViewHolder extends RecyclerView.ViewHolder {
        private  TextView book_name;
        private  ImageView book_img;
        private  TextView book_volume;
        private   TextView book_author;
        private  TextView book_price;
        public  ImageButton likeButton;
        private TextView likeText;

        public BookViewHolder(View itemView) {
            super(itemView);
            book_name = (TextView) itemView.findViewById(R.id.bookName);
            book_img = (ImageView) itemView.findViewById(R.id.bookIMG);
            book_volume = (TextView) itemView.findViewById(R.id.bookVolume);
            book_author = (TextView) itemView.findViewById(R.id.bookAuthor);
            book_price = (TextView) itemView.findViewById(R.id.bookPrice);
            likeButton = (ImageButton) itemView.findViewById(R.id.likeBtn);
            likeText = (TextView) itemView.findViewById(R.id.likeText);
        }

        private void setBookIMG(String url){
            Glide.with(itemView.getContext()).load(url).crossFade().placeholder(R.drawable.googleg_disabled_color_18).thumbnail(01.f).diskCacheStrategy(DiskCacheStrategy.ALL).into(book_img);
        }

        private void setBookName(String name){
            book_name.setText(name);
        }

        private void setBookAuthor(String name){
            book_author.setText("Author: "+name);
        }

        private void setBookVolume(int volume){
            book_volume.setText("Volume: " +volume );
        }

        private void setBookPrice(int price){
            book_price.setText("Price: "+price+" Bath");
        }

        private void setLikeText(int like){likeText.setText("Like: "+like);}
    }
}
