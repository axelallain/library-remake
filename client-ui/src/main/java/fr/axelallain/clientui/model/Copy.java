package fr.axelallain.clientui.model;

import java.util.Collection;

public class Copy {

    private int id;

    private boolean available;

    private Loan loan;

    private Book book;

    public Copy() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
