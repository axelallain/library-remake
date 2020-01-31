package fr.axelallain.books.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "loan")
public class Loan {

    @Id
    @GeneratedValue
    private int id;

    @Column(name = "status", insertable = false)
    private String status = "Started";

    @Column(name = "startingDate")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startingDate;

    @Column(name = "endingDate")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endingDate;

    @OneToOne
    private Copy copy;

    public Loan() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getStartingDate() {
        return startingDate;
    }

    public void setStartingDate(Date startingDate) {
        this.startingDate = startingDate;
    }

    public Date getEndingDate() {
        return endingDate;
    }

    public void setEndingDate(Date endingDate) {
        this.endingDate = endingDate;
    }

    public Copy getCopy() {
        return copy;
    }

    public void setCopy(Copy copy) {
        this.copy = copy;
    }

    @Override
    public String toString() {
        return "Loan{" +
                "id=" + id +
                ", startingDate=" + startingDate +
                ", endingDate=" + endingDate +
                ", copy=" + copy +
                '}';
    }
}
