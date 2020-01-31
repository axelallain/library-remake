package fr.axelallain.books.model;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "book")
public class Book {

    @Id
    @GeneratedValue
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "author")
    private String author;

    @Column(name = "publisher")
    private String publisher;

    @OneToMany(mappedBy = "spot", cascade = CascadeType.ALL)
    private Collection<Copy> copies;

    public Book() {
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

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", publisher='" + publisher + '\'' +
                ", copies=" + copies +
                '}';
    }
}
