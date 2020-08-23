package fr.axelallain.books.controller;

import fr.axelallain.books.dao.BooksDao;
import fr.axelallain.books.exception.BookNotFoundException;
import fr.axelallain.books.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import java.util.List;
import java.util.Optional;

@RestController
public class BooksController {

    @Autowired
    private BooksDao booksDao;

    @GetMapping("/books")
    public Iterable<Book> books(HttpServletResponse response) {

        Iterable<Book> books = booksDao.findAll();

        if (((List<Book>) books).isEmpty()) {
            response.setStatus(HttpServletResponse.SC_OK);
            return books;
        } else {
            response.setStatus(HttpServletResponse.SC_OK);
            return books;
        }
    }

    @GetMapping("/books/{id}")
    public Optional<Book> booksById(@PathVariable Long id, HttpServletResponse response) {

        Optional<Book> book = booksDao.findById(id);

        if (book.isEmpty()) {
            throw new BookNotFoundException("No book found for id " + id);
        } else {
            response.setStatus(HttpServletResponse.SC_OK);
            return book;
        }
    }

    @PostMapping("/books")
    public void booksAdd(Book book, HttpServletResponse response) {

        if (book == null) {
            response.setStatus(HttpServletResponse.SC_CREATED, "Your request has an empty body");
        } else {
            response.setStatus(HttpServletResponse.SC_CREATED);
            booksDao.save(book);
        }
    }

    @PutMapping("/books/{id}")
    public void booksEdit(@RequestBody Book book, @PathVariable Long id, HttpServletResponse response) {

        Optional<Book> bookOptional = booksDao.findById(id);

        if (bookOptional.isEmpty()) {
            throw new BookNotFoundException("No book found for id " + id);
        } else {
            response.setStatus(HttpServletResponse.SC_CREATED);
            book.setId(id);
            booksDao.save(book);
        }
    }

    @DeleteMapping("/books/{id}")
    public void booksDelete(@PathVariable Long id) {

        booksDao.deleteById(id);
    }

    @GetMapping("/books/search")
    public List<Book> findByNameContainingIgnoreCase(@QueryParam("name") String name, HttpServletResponse response) {

        List<Book> books = booksDao.findByNameContainingIgnoreCase(name);

        if (books.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_OK);
            return books;
        } else {
            response.setStatus(HttpServletResponse.SC_OK);
            return books;
        }
    }
}