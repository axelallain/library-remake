package fr.axelallain.books.dto;

import fr.axelallain.books.model.Copy;
import fr.axelallain.books.model.Reservation;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public class UpdateBookDto {

    private Long id;

    private String name;

    private String author;

    private String publisher;

    private List<Copy> copies;

    private Collection<Reservation> reservations;

    private LocalDateTime nextReturnDate;

    public UpdateBookDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public List<Copy> getCopies() {
        return copies;
    }

    public void setCopies(List<Copy> copies) {
        this.copies = copies;
    }

    public Collection<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(Collection<Reservation> reservations) {
        this.reservations = reservations;
    }

    public LocalDateTime getNextReturnDate() {
        return nextReturnDate;
    }

    public void setNextReturnDate(LocalDateTime nextReturnDate) {
        this.nextReturnDate = nextReturnDate;
    }
}
