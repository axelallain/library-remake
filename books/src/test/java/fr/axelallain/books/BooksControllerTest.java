package fr.axelallain.books;

import fr.axelallain.books.controller.BooksController;
import fr.axelallain.books.dao.BooksDao;
import fr.axelallain.books.dto.UpdateBookDto;
import fr.axelallain.books.model.Book;
import fr.axelallain.books.service.BooksService;
import fr.axelallain.books.service.BooksServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

import javax.servlet.http.HttpServletResponse;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class BooksControllerTest {

    @InjectMocks
    BooksController booksController;

    @Mock
    BooksServiceImpl booksServiceImpl;

    @Mock
    BooksDao booksDao;

    @Test
    public void books() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        MockHttpServletResponse response = new MockHttpServletResponse();

        // GIVEN
        // Création des livres de test.
        Book book = new Book();
        book.setId(1L);
        Book book2 = new Book();
        book2.setId(2L);
        List<Book> books = new ArrayList<>();
        books.add(book);
        books.add(book2);
        // Quand on exécute la méthode books (findAll) alors on retourne la liste de livres de test.
        when(booksServiceImpl.findAll()).thenReturn(books);

        // WHEN
        // On exécute la méthode books (findAll).
        List<Book> result = (List<Book>) booksController.books(response);

        // THEN
        // On vérifie si le nombre de livres de test est de 2.
        assertThat(result.size()).isEqualTo(2);
        // On vérifie si l'id du premier livre de test est 1.
        assertThat(result.get(0).getId()).isEqualTo(1L);
        // On vérifie si l'id du second livre de test est 2.
        assertThat(result.get(1).getId()).isEqualTo(2L);
    }

    @Test
    public void booksById() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        MockHttpServletResponse response = new MockHttpServletResponse();

        // GIVEN
        // Création du livre de test.
        Book book = new Book();
        book.setId(4L);
        // Quand on exécute la méthode booksById avec l'id du livre de test alors retourne le livre de test.
        when(booksServiceImpl.findById(book.getId())).thenReturn(Optional.of(book));

        // WHEN
        // On exécute la méthode booksById avec l'id du livre de test.
        Optional<Book> result = booksController.booksById(book.getId(), response);

        // THEN
        // On vérifie si l'id du livre retourné par le booksById correspond à l'id du livre de test.
        assertEquals(result.get().getId(), book.getId());
    }

    @Test
    @Rollback
    public void booksAdd() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        MockHttpServletResponse response = new MockHttpServletResponse();

        // GIVEN
        UpdateBookDto updateBookDto = new UpdateBookDto();
        updateBookDto.setName("Testing book");
        updateBookDto.setAuthor("Testing author");
        updateBookDto.setPublisher("Testing publisher");
        updateBookDto.setCopies(new ArrayList<>());
        updateBookDto.setReservations(new ArrayList<>());
        updateBookDto.setNextReturnDate(LocalDateTime.now());

        // WHEN
        ResponseEntity<Object> responseEntity = booksController.booksAdd(updateBookDto, response);

        // THEN
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(201);
    }

    @Test
    @Rollback
    public void booksDelete() {
        Book book = new Book();
        book.setId(1L);
        when(booksServiceImpl.findById(1L)).thenReturn(Optional.of(book));

        ResponseEntity<Object> responseEntity = booksController.booksDelete(book.getId());

        verify(booksServiceImpl, times(1)).deleteById(eq(book.getId()));
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        // On vérifie que la méthode deleteById a bien été exécutée.
        verify(booksServiceImpl).deleteById(anyLong());
    }

    @Test
    public void findByNameContainingIgnoreCase() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(booksController).build();
        mockMvc.perform(get("/books/search").requestAttr("name", "deux")).andExpect(status().isOk());
    }
}
