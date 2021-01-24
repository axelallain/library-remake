package fr.axelallain.batch.dto;

import fr.axelallain.batch.model.Book;

public class UpdateReservationDto {

    private Long id;

    private Long position;

    private Book book;

    private String tokenuserid;

    private String tokenuseremail;

    private String status;

    public UpdateReservationDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPosition() {
        return position;
    }

    public void setPosition(Long position) {
        this.position = position;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public String getTokenuserid() {
        return tokenuserid;
    }

    public void setTokenuserid(String tokenuserid) {
        this.tokenuserid = tokenuserid;
    }

    public String getTokenuseremail() {
        return tokenuseremail;
    }

    public void setTokenuseremail(String tokenuseremail) {
        this.tokenuseremail = tokenuseremail;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
