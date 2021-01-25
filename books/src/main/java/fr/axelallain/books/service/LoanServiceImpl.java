package fr.axelallain.books.service;

import fr.axelallain.books.dao.LoanDao;
import fr.axelallain.books.model.Loan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.transaction.Transactional;

@Service
@Transactional
public class LoanServiceImpl implements LoanService {

    @Autowired
    private LoanDao loanDao;

    @Override
    public void loanAdd(Loan loan) {
        loanDao.save(loan);
    }
}
