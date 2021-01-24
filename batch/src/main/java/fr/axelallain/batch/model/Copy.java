package fr.axelallain.batch.model;

import java.util.Collection;

public class Copy {

    private Long id;

    private boolean available;

    private Loan loan;

    private Book book;

    public Copy() {
    }

    public Copy(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public Loan getLoan() {
        return loan;
    }

    public void setLoan(Loan loan) {
        this.loan = loan;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    @Override
    public String toString() {
        return "Copy{" +
                "id=" + id +
                ", available=" + available +
                ", loan=" + loan +
                ", book=" + book +
                '}';
    }
}
