package fr.axelallain.books.controller;

import fr.axelallain.books.dao.LoanDao;
import fr.axelallain.books.exception.LoanExtensionException;
import fr.axelallain.books.exception.LoanNotFoundException;
import fr.axelallain.books.model.Loan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RestController
public class LoanController {

    @Autowired
    private LoanDao loanDao;

    @GetMapping("/loan/user/{tokenuserid}")
    public List<Loan> findByTokenuserid(@PathVariable String tokenuserid, HttpServletResponse response) {

        List<Loan> loans = loanDao.findByTokenuserid(tokenuserid);

        if (loans.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_OK);
            return loans;
        } else {
            response.setStatus(HttpServletResponse.SC_OK);
            return loans;
        }
    }

    @GetMapping("/loan/{id}")
    public Loan findById(@PathVariable int id) {
        return loanDao.findById(id);
    }

    @GetMapping("/loan")
    public Iterable<Loan> findAll(HttpServletResponse response) {

        Iterable<Loan> loans = loanDao.findAll();

        if (((List<Loan>) loans).isEmpty()) {
            response.setStatus(HttpServletResponse.SC_OK);
            return loans;
        } else {
            response.setStatus(HttpServletResponse.SC_OK);
            return loans;
        }
    }

    @PostMapping("/loan")
    public void loanAdd(Loan loan) {

        loanDao.save(loan);
    }

    @PostMapping("/loan/{id}/ended")
    public void loanEnded(@PathVariable int id, HttpServletResponse response) {

        Loan loan = loanDao.findById(id);

        if (loan == null) {
            throw new LoanNotFoundException("No loan found for id " + id);
        } else {
            loan.setEnded(true);
            loan.setStatus("Ended");
            loanDao.save(loan);
        }
    }

    @PostMapping("/loan/{id}/extension")
    public void extensionDate(@PathVariable int id, HttpServletResponse response) {

        Loan loan = loanDao.findById(id);

        if (!loan.isExtended() && LocalDateTime.now().isBefore(loan.getEndingDate())) {

            // convert date to calendar
            Calendar c = Calendar.getInstance();
            c.setTime(Date.from(loan.getEndingDate().atZone(ZoneId.systemDefault()).toInstant()));

            // manipulate date
            c.add(Calendar.HOUR, 672); // 28 Days (4 weeks)

            // convert calendar to date
            Date endingDateExtended = c.getTime();

            LocalDateTime LdtEndingDateExtended = endingDateExtended.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

            loan.setEndingDate(LdtEndingDateExtended);

            loan.setExtended(true);

            loanDao.save(loan);

        } else {
            throw new LoanExtensionException("Loan " + id + " cannot be extended (already extended or expired)");
        }
    }
}
