package fr.axelallain.books.dao;

import fr.axelallain.books.model.Book;

import java.util.List;

public interface BooksRepositoryCustom {

    List<Book> findByNameLike(String name);
}
