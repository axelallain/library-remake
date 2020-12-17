package fr.axelallain.books.service;

import fr.axelallain.books.model.Book;

import java.util.List;
import java.util.Optional;

public interface BooksService {

    List<Book> findByNameContainingIgnoreCase(String name);

    Iterable<Book> findAll();

    Optional<Book> findById(Long id);

    void save(Book book);

    void deleteById(Long id);
}
