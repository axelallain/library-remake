package fr.axelallain.books.service;

import fr.axelallain.books.dao.BooksDao;
import fr.axelallain.books.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BooksServiceImpl implements BooksService {

    @Autowired
    private BooksDao booksDao;

    @Override
    public List<Book> findByNameContainingIgnoreCase(String name) {
        return booksDao.findByNameContainingIgnoreCase(name);
    }

    @Override
    public Iterable<Book> findAll() {
        return booksDao.findAll();
    }

    @Override
    public Optional<Book> findById(Long id) {
        return booksDao.findById(id);
    }

    @Override
    public void save(Book book) {
        booksDao.save(book);
    }

    @Override
    public void deleteById(Long id) {
        booksDao.deleteById(id);
    }
}
