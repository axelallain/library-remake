package fr.axelallain.books.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "reservation")
public class Reservation {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "book_id")
    @JsonBackReference
    private Book book;

    @Column(name = "creationDate", columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable = false, updatable = false, nullable = false)
    private Timestamp creationDate;

    @Column(name = "tokenuserid", nullable = false)
    private String tokenuserid;

    @Column(name = "tokenuseremail", nullable = false)
    private String tokenuseremail;

    @Column(name = "status")
    private String status = "Pending";

    public Reservation() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }

    public String getTokenuserid() {
        return tokenuserid;
    }

    public void setTokenuserid(String tokenuserid) {
        this.tokenuserid = tokenuserid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTokenuseremail() {
        return tokenuseremail;
    }

    public void setTokenuseremail(String tokenuseremail) {
        this.tokenuseremail = tokenuseremail;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", book=" + book +
                ", creationDate=" + creationDate +
                ", tokenuserid='" + tokenuserid + '\'' +
                ", tokenuseremail='" + tokenuseremail + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
