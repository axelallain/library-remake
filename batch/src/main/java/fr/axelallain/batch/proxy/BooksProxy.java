package fr.axelallain.batch.proxy;

import fr.axelallain.batch.model.Loan;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "zuul")
@RibbonClient(name = "books")
public interface BooksProxy {

    @GetMapping("/books/loan")
    Iterable<Loan> findAll();

    @PostMapping("/books/loan")
    void loanAdd(Loan loan);

    @GetMapping("/books/loan/{id}")
    Loan findById(@PathVariable int id);
}
