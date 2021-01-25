package fr.axelallain.books.controller;

import fr.axelallain.books.dao.CopyDao;
import fr.axelallain.books.dao.LoanDao;
import fr.axelallain.books.dto.UpdateCopyDto;
import fr.axelallain.books.dto.UpdateLoanDto;
import fr.axelallain.books.exception.LoanExtensionException;
import fr.axelallain.books.exception.LoanNotFoundException;
import fr.axelallain.books.model.Copy;
import fr.axelallain.books.model.Loan;
import fr.axelallain.books.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
public class LoanController {

    @Autowired
    private LoanDao loanDao;

    @Autowired
    private LoanService loanService;

    @Autowired
    private CopyDao copyDao;

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

    @PutMapping("/loan")
    public void loanAdd(@RequestBody UpdateLoanDto updateLoanDto) {

        if (updateLoanDto.getLastReminderEmail() != null && updateLoanDto.getStatus() != null && updateLoanDto.getId() != 0) {
            Loan loan = loanDao.findById(updateLoanDto.getId());
            loan.setLastReminderEmail(updateLoanDto.getLastReminderEmail());
            // NullPointerException, il faut soit passer un lastReminderEmail au DTO dans la deuxième méthode de SchedulingTasks.java
            // soit il faut isoler ce setLastReminderEmail pour qu'il ne soit utilisé que dans la première méthode
            loan.setStatus(updateLoanDto.getStatus());
            loanService.loanAdd(loan);
        } else if (updateLoanDto.getTokenuserid() != null && updateLoanDto.getTokenuseremail() != null && updateLoanDto.getCopy() != null && updateLoanDto.getStartingDate() != null && updateLoanDto.getEndingDate() != null && updateLoanDto.getId() == 0){
            Loan loan = new Loan();
            loan.setTokenuserid(updateLoanDto.getTokenuserid());
            loan.setTokenuseremail(updateLoanDto.getTokenuseremail());
            loan.setCopy(updateLoanDto.getCopy());
            loan.setStartingDate(updateLoanDto.getStartingDate());
            loan.setEndingDate(updateLoanDto.getEndingDate());
            loanService.loanAdd(loan);
        } else if (loanDao.findById(updateLoanDto.getId()) != null && updateLoanDto.getStatus() != null) {
            Loan loan = loanDao.findById(updateLoanDto.getId());
            loan.setStatus(updateLoanDto.getStatus());
            loanService.loanAdd(loan);
        } else if (loanDao.findById(updateLoanDto.getId()) != null && updateLoanDto.getLastReminderEmail() != null) {
            Loan loan = loanDao.findById(updateLoanDto.getId());
            loan.setLastReminderEmail(updateLoanDto.getLastReminderEmail());
            loanService.loanAdd(loan);
        } else if (loanDao.findById(updateLoanDto.getId()) != null && updateLoanDto.getLastReminderEmail() == null) {
            Loan loan = loanDao.findById(updateLoanDto.getId());
            loan.setLastReminderEmail(updateLoanDto.getLastReminderEmail());
            loanService.loanAdd(loan);
        }
    }

    @PutMapping("/copy")
    public void copyAdd(@RequestBody UpdateCopyDto updateCopyDto) {
        // si existe déjà update sinon new copy..
        if (copyDao.findById(updateCopyDto.getId()) != null) {
            Copy copy = copyDao.findById(updateCopyDto.getId()).get();
            copy.setAvailable(updateCopyDto.isAvailable());
            copyDao.save(copy);
        } else {
            Copy copy = new Copy();
            copy.setAvailable(updateCopyDto.isAvailable());
            // set book
            // set loan
            copyDao.save(copy);
        }
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

        /*
            TICKET 2
            Vérification heure locale / date de fin de l'emprunt.
        */
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
            throw new LoanExtensionException("Loan " + id + " cannot be extended. (already extended or expired)");
        }
    }

    @GetMapping("/loan/date")
    public Iterable<Loan> findAllByOrderByEndingDateDesc(HttpServletResponse response) {

        Iterable<Loan> loans = loanDao.findAllByOrderByEndingDateDesc();

        if (((List<Loan>) loans).isEmpty()) {
            response.setStatus(HttpServletResponse.SC_OK);
            return loans;
        } else {
            response.setStatus(HttpServletResponse.SC_OK);
            return loans;
        }
    }
}
