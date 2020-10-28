package com.gamechanger.ebookstore.Adapter;

public class MyAdapter
{
    String name,id,email;
    String imageURL;

    public MyAdapter(String name, String id, String email, String imageURL) {
        this.name = name;
        this.id = id;
        this.email = email;
        this.imageURL = imageURL;
    }

    public MyAdapter() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
