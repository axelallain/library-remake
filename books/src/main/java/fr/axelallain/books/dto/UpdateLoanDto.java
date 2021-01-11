package fr.axelallain.books.dto;

import java.time.LocalDateTime;

public class UpdateLoanDto {

    private int id;

    private LocalDateTime lastReminderEmail;

    private String status;

    private LocalDateTime startingDate;

    private String tokenuserid;

    public UpdateLoanDto() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getLastReminderEmail() {
        return lastReminderEmail;
    }

    public void setLastReminderEmail(LocalDateTime lastReminderEmail) {
        this.lastReminderEmail = lastReminderEmail;
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

    public String getTokenuserid() {
        return tokenuserid;
    }

    public void setTokenuserid(String tokenuserid) {
        this.tokenuserid = tokenuserid;
    }
}
