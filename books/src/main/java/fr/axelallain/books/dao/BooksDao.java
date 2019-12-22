package fr.axelallain.books.dao;

import fr.axelallain.books.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BooksDao extends JpaRepository<Book, Integer> {


}
