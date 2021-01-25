package fr.axelallain.batch.dto;

import fr.axelallain.batch.model.Copy;

import java.time.LocalDateTime;

public class UpdateLoanDto {

    private int id;

    private LocalDateTime lastReminderEmail;

    private String status;

    private LocalDateTime startingDate;

    private LocalDateTime endingDate;

    private String tokenuserid;

    private String tokenuseremail;

    private Copy copy;

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

    public Copy getCopy() {
        return copy;
    }

    public void setCopy(Copy copy) {
        this.copy = copy;
    }

    public String getTokenuseremail() {
        return tokenuseremail;
    }

    public void setTokenuseremail(String tokenuseremail) {
        this.tokenuseremail = tokenuseremail;
    }

    public LocalDateTime getEndingDate() {
        return endingDate;
    }

    public void setEndingDate(LocalDateTime endingDate) {
        this.endingDate = endingDate;
    }
}
