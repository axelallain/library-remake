package fr.axelallain.books.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

@Entity
@Table(name = "copy")
public class Copy {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "available")
    private boolean available;

    @OneToOne(cascade = CascadeType.ALL)
    private Loan loan;

    @ManyToOne
    @JsonBackReference
    private Book book;

    public Copy() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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
