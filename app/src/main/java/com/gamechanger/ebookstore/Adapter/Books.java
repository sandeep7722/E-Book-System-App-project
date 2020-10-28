package com.gamechanger.ebookstore.Adapter;

public class Books
{
    String category;
    String bookUrl;

    public Books(String category, String bookUrl) {
        this.category = category;
        this.bookUrl = bookUrl;
    }

    public Books() {
    }

    public String getBookName() {
        return category;
    }

    public void setBookName(String bookName) {
        this.category = bookName;
    }


    public String getBookUrl() {
        return bookUrl;
    }

    public void setBookUrl(String bookUrl) {
        this.bookUrl = bookUrl;
    }
}
