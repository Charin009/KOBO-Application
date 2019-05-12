package com.example.charin.koboapplication.Object;

public class Publisher {
    private String publisher_name;
    private  String publisher_img;
    private int publisher_amount;

    public Publisher(){

    }

    public Publisher(String publisher_name, String publisher_img, int publisher_amount){
        this.publisher_name = publisher_name;
        this.publisher_img = publisher_img;
        this.publisher_amount = publisher_amount;

    }

    public String getPublisher_name() {
        return publisher_name;
    }

    public void setPublisher_name(String publisher_name) {
        this.publisher_name = publisher_name;
    }

    public String getPublisher_img() {
        return publisher_img;
    }

    public void setPublisher_img(String publisher_img) {
        this.publisher_img = publisher_img;
    }

    public int getPublisher_amount() {
        return publisher_amount;
    }

    public void setPublisher_amount(int publisher_amount) {
        this.publisher_amount = publisher_amount;
    }
}
