package fr.axelallain.books.service;

import fr.axelallain.books.dto.UpdateLoanDto;
import fr.axelallain.books.model.Loan;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

public interface LoanService {

    void loanAdd(Loan loan);
}
