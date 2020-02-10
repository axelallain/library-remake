package fr.axelallain.clientui.model;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class Loan {

    private int id;

    private String status;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startingDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endingDate;

    private Copy copy;

    private String test;

    public Loan() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    @Override
    public String toString() {
        return "Loan{" +
                "id=" + id +
                ", status='" + status + '\'' +
                ", startingDate=" + startingDate +
                ", endingDate=" + endingDate +
                ", copy=" + copy +
                ", test='" + test + '\'' +
                '}';
    }
}

