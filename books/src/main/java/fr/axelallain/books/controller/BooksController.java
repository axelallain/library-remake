package fr.axelallain.books.controller;

import fr.axelallain.books.dao.BooksDao;
import fr.axelallain.books.exception.BookNotFoundException;
import fr.axelallain.books.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.QueryParam;
import java.util.List;
import java.util.Optional;

@RestController
public class BooksController {

    @Autowired
    private BooksDao booksDao;

    @GetMapping("/books")
    public Iterable<Book> books() {

        return booksDao.findAll();
    }

    @GetMapping("/books/{id}")
    public Optional<Book> booksById(@PathVariable int id) {

        Optional<Book> book = booksDao.findById(id);

        if (book.isEmpty()) {
            throw new BookNotFoundException("The book with id " + id + " cannot be found");
        }

        return book;
    }

    @PostMapping("/books")
    public void booksAdd(Book book) {

        booksDao.save(book);
    }

    @PutMapping("/books/{id}")
    public void booksEdit(@RequestBody Book book, @PathVariable int id) {

        Optional<Book> bookOptional = booksDao.findById(id);

        if (bookOptional.isEmpty()) {
            throw new BookNotFoundException("The book with id " + id + " cannot be found");
        }

        book.setId(id);

        booksDao.save(book);
    }

    @DeleteMapping("/books/{id}")
    public void booksDelete(@PathVariable int id) {

        booksDao.deleteById(id);
    }

    @GetMapping("/books/search")
    public List<Book> findByNameContainingIgnoreCase(@QueryParam("name") String name) {

        return booksDao.findByNameContainingIgnoreCase(name);
    }
}