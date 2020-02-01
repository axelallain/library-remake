package fr.axelallain.books.dao;

import fr.axelallain.books.model.Book;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
public class BooksRepositoryCustomImpl implements BooksRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;


}
