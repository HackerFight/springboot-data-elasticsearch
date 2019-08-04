package com.hacker.boot.elasticsearch.bean;

import org.springframework.data.elasticsearch.annotations.Document;


/**
 * 索引名字，类型名字，如果一个内容要储存，这些是必须要指定的
 */
@Document(indexName = "hacker", type = "book")
public class Book {

    private Integer id;

    private String bookName;

    private String author;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", bookName='" + bookName + '\'' +
                ", author='" + author + '\'' +
                '}';
    }
}
