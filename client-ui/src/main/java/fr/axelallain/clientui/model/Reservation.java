package fr.axelallain.clientui.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;

public class Reservation {

    private Long id;

    private Book book;

    private Timestamp creationDate;

    private String tokenuserid;

    private String tokenuseremail;

    private String status = "Pending";

    private Long position;

    private Timestamp nextReturnDate;

    public Reservation() {
    }

    public Reservation(Long id) {
        this.id = id;
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

    public Long getPosition() {
        return position;
    }

    public void setPosition(Long position) {
        this.position = position;
    }

    public Timestamp getNextReturnDate() {
        return nextReturnDate;
    }

    public void setNextReturnDate(Timestamp nextReturnDate) {
        this.nextReturnDate = nextReturnDate;
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
                ", position=" + position +
                ", nextReturnDate=" + nextReturnDate +
                '}';
    }
}
