package fr.axelallain.books.controller;

import fr.axelallain.books.dao.LoanDao;
import fr.axelallain.books.model.Loan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RestController
public class LoanController {

    @Autowired
    private LoanDao loanDao;

    @GetMapping("/loan/user/{test}")
    public List<Loan> findByTest(@PathVariable String test) {

        return loanDao.findByTest(test);
    }

    @PutMapping("/loan/{id}/extension")
    public void extensionDate(@PathVariable int id) {

        Loan loan = loanDao.findById(id);

        // convert date to calendar
        Calendar c = Calendar.getInstance();
        c.setTime(loan.getEndingDate());

        // manipulate date
        c.add(Calendar.HOUR, 672); // 28 Days (4 weeks)

        // convert calendar to date
        Date endingDateExtended = c.getTime();

        loan.setEndingDate(endingDateExtended);

        loanDao.save(loan);
    }
}
