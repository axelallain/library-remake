package fr.axelallain.books.controller;

import fr.axelallain.books.dao.LoanDao;
import fr.axelallain.books.model.Loan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RestController
public class LoanController {

    @Autowired
    private LoanDao loanDao;

    @GetMapping("/loan/user/{tokenuserid}")
    public List<Loan> findByTokenuserid(@PathVariable String tokenuserid) {

        return loanDao.findByTokenuserid(tokenuserid);
    }

    @GetMapping("/loan")
    public List<Loan> findAll(HttpServletResponse response) {

        List<Loan> loans = loanDao.findAll();

        if (!loans.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_OK);
            return loans;
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return loans;
        }
    }

    @PostMapping("/loan/{id}/extension")
    public void extensionDate(@PathVariable int id, HttpServletResponse response) {

        Loan loan = loanDao.findById(id);

        if (!loan.isExtended() && ZonedDateTime.now().toInstant().isBefore(loan.getEndingDate().toInstant())) {

            // convert date to calendar
            Calendar c = Calendar.getInstance();
            c.setTime(loan.getEndingDate());

            // manipulate date
            c.add(Calendar.HOUR, 672); // 28 Days (4 weeks)

            // convert calendar to date
            Date endingDateExtended = c.getTime();

            loan.setEndingDate(endingDateExtended);

            loan.setExtended(true);

            loanDao.save(loan);

            response.setStatus(HttpServletResponse.SC_CREATED, "Loan " + id + " has been extended for 28 days");

        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED, "Loan " + id + " cannot be extended (already extended or ending date not reached)");
        }
    }
}
