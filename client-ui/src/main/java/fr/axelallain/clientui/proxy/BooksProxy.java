package fr.axelallain.clientui.proxy;

import fr.axelallain.clientui.model.Book;
import fr.axelallain.clientui.model.Loan;
import fr.axelallain.clientui.model.Reservation;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.QueryParam;
import java.util.Date;
import java.util.List;

@FeignClient(name = "zuul")
@RibbonClient(name = "books")
public interface BooksProxy {

    @GetMapping(value = "/books/books")
    Iterable<Book> books();

    @GetMapping(value = "/books/books/{id}")
    Book booksById(@PathVariable("id") Long id);

    @GetMapping(value = "/books/books/search")
    List<Book> findByNameContainingIgnoreCase(@RequestParam("name") String name);

    @GetMapping("/books/loan/user/{tokenuserid}")
    List<Loan> findByTokenuserid(@PathVariable String tokenuserid);

    @PostMapping("/books/loan/{id}/extension")
    void extensionDate(@PathVariable Long id);

    @PostMapping("/books/reservations")
    void reservationsAdd(@RequestBody Reservation reservation);
}
