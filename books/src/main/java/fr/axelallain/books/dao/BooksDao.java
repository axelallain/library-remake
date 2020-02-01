package fr.axelallain.books.dao;

import fr.axelallain.books.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BooksDao extends JpaRepository<Book, Integer>, BooksRepositoryCustom {

    List<Book> findByNameContainingIgnoreCase(String name);
}
