package fr.axelallain.batch.proxy;

import fr.axelallain.batch.model.Loan;
import fr.axelallain.batch.model.Reservation;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "zuul")
@RibbonClient(name = "books")
public interface BooksProxy {

    @GetMapping("/books/loan")
    Iterable<Loan> findAll();

    @PutMapping("/books/loan")
    @Transactional
    void loanAdd(@RequestBody Loan loan);

    @GetMapping("/books/loan/{id}")
    Loan findById(@PathVariable Long id);

    @GetMapping("/books/reservations/{bookid}")
    List<Reservation> findByBookIdOrderByCreationDateDesc(@PathVariable Long bookid);

    @GetMapping("/books/reservations")
    public List<Reservation> findAllReservations();
}
