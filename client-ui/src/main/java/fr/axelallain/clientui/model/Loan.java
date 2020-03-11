package fr.axelallain.clientui.model;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;

public class Loan {

    private int id;

    private String status;

    private LocalDateTime startingDate;

    private LocalDateTime endingDate;

    private boolean extended;

    private Copy copy;

    private String tokenuserid;

    private String tokenuseremail;

    private LocalDateTime lastReminderEmail;

    private boolean ended;

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

    public LocalDateTime getStartingDate() {
        return startingDate;
    }

    public void setStartingDate(LocalDateTime startingDate) {
        this.startingDate = startingDate;
    }

    public LocalDateTime getEndingDate() {
        return endingDate;
    }

    public void setEndingDate(LocalDateTime endingDate) {
        this.endingDate = endingDate;
    }

    public boolean isExtended() {
        return extended;
    }

    public void setExtended(boolean extended) {
        this.extended = extended;
    }

    public Copy getCopy() {
        return copy;
    }

    public void setCopy(Copy copy) {
        this.copy = copy;
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

    public LocalDateTime getLastReminderEmail() {
        return lastReminderEmail;
    }

    public void setLastReminderEmail(LocalDateTime lastReminderEmail) {
        this.lastReminderEmail = lastReminderEmail;
    }

    public boolean isEnded() {
        return ended;
    }

    public void setEnded(boolean ended) {
        this.ended = ended;
    }

    @Override
    public String toString() {
        return "Loan{" +
                "id=" + id +
                ", status='" + status + '\'' +
                ", startingDate=" + startingDate +
                ", endingDate=" + endingDate +
                ", extended=" + extended +
                ", copy=" + copy +
                ", tokenuserid='" + tokenuserid + '\'' +
                ", tokenuseremail='" + tokenuseremail + '\'' +
                ", lastReminderEmail=" + lastReminderEmail +
                ", ended=" + ended +
                '}';
    }
}

