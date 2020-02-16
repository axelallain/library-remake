package fr.axelallain.clientui.proxy;

import fr.axelallain.clientui.model.Book;
import fr.axelallain.clientui.model.Loan;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.QueryParam;
import java.util.Date;
import java.util.List;

@FeignClient(name = "zuul")
@RibbonClient(name = "books")
public interface BooksProxy {

    @GetMapping(value = "/books/books")
    Iterable<Book> books();

    @GetMapping(value = "/books/books/{id}")
    Book booksById(@PathVariable("id") int id);

    @GetMapping(value = "/books/books/search")
    List<Book> findByNameContainingIgnoreCase(@RequestParam("name") String name);

    @GetMapping("/books/loan/user/{tokenuserid}")
    List<Loan> findByTokenuserid(@PathVariable String tokenuserid);

    @PostMapping("/books/loan/{id}/extension")
    void extensionDate(@PathVariable int id);
}
