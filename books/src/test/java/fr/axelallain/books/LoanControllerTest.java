package fr.axelallain.books;

import fr.axelallain.books.controller.LoanController;
import fr.axelallain.books.dao.LoanDao;
import fr.axelallain.books.dto.UpdateCopyDto;
import fr.axelallain.books.dto.UpdateLoanDto;
import fr.axelallain.books.model.Loan;
import fr.axelallain.books.service.LoanServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.Rollback;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LoanControllerTest {

    @InjectMocks
    LoanController loanController;

    @Mock
    LoanServiceImpl loanServiceImpl;

    @Mock
    LoanDao loanDao;

    @Test
    public void findByTokenuserid() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        MockHttpServletResponse response = new MockHttpServletResponse();

        // GIVEN
        // Création des emprunts de test.
        Loan loan = new Loan();
        loan.setTokenuserid("testing");
        Loan loan2 = new Loan();
        loan2.setTokenuserid("testing");
        List<Loan> loans = new ArrayList<>();
        loans.add(loan);
        loans.add(loan2);
        // Quand on exécute la méthode findByTokenuserid alors on retourne la liste d'emprunts de test.
        when(loanDao.findByTokenuserid("testing")).thenReturn(loans);

        // WHEN
        // On exécute la méthode findByTokenuserid.
        List<Loan> result = loanController.findByTokenuserid("testing", response);

        // THEN
        // On vérifie si le tokenuserid des emprunts de test est égal à celui de test "testing".
        assertThat(result.get(0).getTokenuserid()).isEqualTo("testing");
        assertThat(result.get(1).getTokenuserid()).isEqualTo("testing");
    }

    @Test
    public void findById() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        MockHttpServletResponse response = new MockHttpServletResponse();

        // GIVEN
        // Création de l'emprunt de test.
        Loan loan = new Loan();
        loan.setId(400);
        // Quand on exécute la méthode findById avec l'id de l'emprunt de test alors retourne l'emprunt de test.
        when(loanDao.findById(loan.getId())).thenReturn(loan);

        // WHEN
        // On exécute la méthode findById avec l'id de l'emprunt de test.
        Loan result = loanController.findById(loan.getId());

        // THEN
        // On vérifie si l'id de l'emprunt retourné par le findById correspond à l'id de l'emprunt de test.
        assertEquals(result.getId(), loan.getId());
    }

    @Test
    public void findAll() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        MockHttpServletResponse response = new MockHttpServletResponse();

        // GIVEN
        // Création des emprunts de test.
        Loan loan = new Loan();
        loan.setId(400);
        Loan loan2 = new Loan();
        loan2.setId(401);
        List<Loan> loans = new ArrayList<>();
        loans.add(loan);
        loans.add(loan2);

        // Quand on exécute la méthode findAll alors on retourne la liste d'emprunts de test.
        when(loanDao.findAll()).thenReturn(loans);

        // WHEN
        // On exécute la méthode findAll.
        List<Loan> result = (List<Loan>) loanController.findAll(response);

        // THEN
        // On vérifie si le nombre d'emprunts de test est de 2.
        assertThat(result.size()).isEqualTo(2);
        // On vérifie si l'id du premier emprunt de test est 1.
        assertThat(result.get(0).getId()).isEqualTo(400);
        // On vérifie si l'id du second emprunt de test est 2.
        assertThat(result.get(1).getId()).isEqualTo(401);
    }

    @Test
    @Rollback
    public void loanAdd() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        MockHttpServletResponse response = new MockHttpServletResponse();

        // GIVEN
        UpdateLoanDto updateLoanDto = new UpdateLoanDto();
        updateLoanDto.setId(1);
        updateLoanDto.setStatus("testing");

        // WHEN
        ResponseEntity<Object> responseEntity = loanController.loanAdd(updateLoanDto);

        // THEN
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(201);
    }

    @Test
    @Rollback
    public void copyAdd() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        MockHttpServletResponse response = new MockHttpServletResponse();

        // GIVEN
        UpdateCopyDto updateCopyDto = new UpdateCopyDto();
        updateCopyDto.setAvailable(true);

        // WHEN
        ResponseEntity<Object> responseEntity = loanController.copyAdd(updateCopyDto);

        // THEN
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(201);
    }

    @Test
    @Rollback
    public void loanEnded() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        MockHttpServletResponse response = new MockHttpServletResponse();

        // GIVEN
        Loan loan = new Loan();
        loan.setId(400);
        loan.setEnded(false);
        when(loanDao.findById(loan.getId())).thenReturn(loan);

        // WHEN
        ResponseEntity<Object> responseEntity = loanController.loanEnded(loan.getId(), response);

        // THEN
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(201);
    }

    @Test
    @Rollback
    public void extensionDate() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        MockHttpServletResponse response = new MockHttpServletResponse();

        // GIVEN
        Loan loan = new Loan();
        loan.setId(400);
        LocalDateTime endingDate = LocalDateTime.of(2077,
                Month.APRIL, 18, 18, 00, 00);
        loan.setEndingDate(endingDate);
        loan.setExtended(false);
        when(loanDao.findById(loan.getId())).thenReturn(loan);

        // WHEN
        ResponseEntity<Object> responseEntity = loanController.extensionDate(loan.getId(), response);

        // THEN
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(201);
    }

    @Test
    @Rollback
    public void extensionDateAlreadyExtended() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        MockHttpServletResponse response = new MockHttpServletResponse();

        // GIVEN
        Loan loan = new Loan();
        loan.setId(400);
        LocalDateTime endingDate = LocalDateTime.of(2077,
                Month.APRIL, 18, 18, 00, 00);
        loan.setEndingDate(endingDate);
        loan.setExtended(true);
        when(loanDao.findById(loan.getId())).thenReturn(loan);

        // WHEN
        ResponseEntity<Object> responseEntity = loanController.extensionDate(loan.getId(), response);

        // THEN
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(403);
    }

    @Test
    public void findAllByOrderByEndingDateDesc() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        MockHttpServletResponse response = new MockHttpServletResponse();

        // GIVEN
        // Création des emprunts de test.
        Loan loan = new Loan();
        loan.setId(1);
        LocalDateTime endingDateOld = LocalDateTime.of(1999,
                Month.APRIL, 18, 18, 00, 00);
        loan.setEndingDate(endingDateOld);
        Loan loan2 = new Loan();
        loan2.setId(2);
        loan2.setEndingDate(LocalDateTime.now());
        List<Loan> loans = new ArrayList<>();
        loans.add(loan);
        loans.add(loan2);

        // Quand on exécute la méthode findAll alors on retourne la liste d'emprunts de test.
        when(loanDao.findAllByOrderByEndingDateDesc()).thenReturn(loans);

        // WHEN
        // On exécute la méthode findAll.
        List<Loan> result = (List<Loan>) loanController.findAllByOrderByEndingDateDesc(response);

        // THEN
        // On vérifie si le nombre d'emprunts de test est de 2.
        assertThat(result.size()).isEqualTo(2);
        // On vérifie si les emprunts de test sont triés par date de fin en ordre décroissant.
        // L'emprunt 2 passe premier car la date de fin est la plus récente des deux.
        assertThat(result.get(0).getId()).isEqualTo(2);
    }
}
