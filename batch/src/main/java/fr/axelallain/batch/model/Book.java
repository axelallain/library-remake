package fr.axelallain.batch.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collection;

public class Book {

    private int id;

    private String name;

    private String author;

    private String publisher;

    private Collection<Copy> copies;

    private LocalDateTime nextReturnDate;

    public Book() {
    }

    public Book(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Collection<Copy> getCopies() {
        return copies;
    }

    public void setCopies(Collection<Copy> copies) {
        this.copies = copies;
    }

    public LocalDateTime getNextReturnDate() {
        return nextReturnDate;
    }

    public void setNextReturnDate(LocalDateTime nextReturnDate) {
        this.nextReturnDate = nextReturnDate;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", publisher='" + publisher + '\'' +
                ", copies=" + copies +
                ", nextReturnDate=" + nextReturnDate +
                '}';
    }
}
