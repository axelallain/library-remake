package fr.axelallain.books.dao;

import fr.axelallain.books.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanDao extends JpaRepository<Loan, Integer> {


}
