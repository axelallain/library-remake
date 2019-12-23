package fr.axelallain.clientui.proxy;

import fr.axelallain.clientui.model.Book;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "books", url = "localhost:9001")
@RibbonClient(name = "books")
public interface BooksProxy {

    @GetMapping(value = "/books")
    Iterable<Book> books();

    @GetMapping(value = "/books/{id}")
    Book booksById(@PathVariable("id") int id);
}
