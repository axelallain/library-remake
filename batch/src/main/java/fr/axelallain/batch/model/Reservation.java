package fr.axelallain.batch.model;

import java.sql.Timestamp;

public class Reservation {

    private Long id;

    private Book book;

    private Timestamp creationDate;

    private String tokenuserid;

    private String tokenuseremail;

    private String status;

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
