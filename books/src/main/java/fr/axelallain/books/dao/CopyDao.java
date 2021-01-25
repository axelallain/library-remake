package fr.axelallain.books.dao;

import fr.axelallain.books.model.Copy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CopyDao extends JpaRepository<Copy, Long> {

    List<Copy> findByBookId(Long id);
}
