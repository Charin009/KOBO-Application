package com.example.charin.koboapplication.Object;

public class Book {
    private String name;
    private String author;
    private int volume;
    private int price;
    private String image;
    private int likeAmount;


    public Book(){

    }

    public Book(String name, String author, int volume, int price, String image,int likeAmount) {
        this.name = name;
        this.author = author;
        this.volume = volume;
        this.price = price;
        this.image = image;
        this.likeAmount = likeAmount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getLikeAmount() {
        return likeAmount;
    }

    public void setLikeAmount(int likeAmount) {
        this.likeAmount = likeAmount;
    }
}
