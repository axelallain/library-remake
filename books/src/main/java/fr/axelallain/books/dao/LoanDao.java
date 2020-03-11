package fr.axelallain.books.dao;

import fr.axelallain.books.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LoanDao extends JpaRepository<Loan, Integer> {

    List<Loan> findByTokenuserid(String tokenuserid);

    Loan findById(int id);
}
