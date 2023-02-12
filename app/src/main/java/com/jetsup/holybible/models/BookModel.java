package com.jetsup.holybible.models;

public class BookModel {
    String bookTitle, bookDescription;
    int bookCoverPic;

    public BookModel(String bookTitle, String bookDescription, int bookCoverPic) {
        this.bookTitle = bookTitle;
        this.bookDescription = bookDescription;
        this.bookCoverPic = bookCoverPic;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public String getBookDescription() {
        return bookDescription;
    }

    public int getBookCoverPic() {
        return bookCoverPic;
    }
}
