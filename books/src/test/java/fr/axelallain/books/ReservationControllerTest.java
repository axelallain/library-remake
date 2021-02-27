package fr.axelallain.books;

import fr.axelallain.books.controller.ReservationController;
import fr.axelallain.books.dao.CopyDao;
import fr.axelallain.books.dao.ReservationDao;
import fr.axelallain.books.dao.ReservationDaoCustom;
import fr.axelallain.books.dto.UpdateReservationDto;
import fr.axelallain.books.model.Book;
import fr.axelallain.books.model.Copy;
import fr.axelallain.books.model.Reservation;
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

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReservationControllerTest {

    @InjectMocks
    ReservationController reservationController;

    @Mock
    ReservationDao reservationDao;

    @Mock
    ReservationDaoCustom reservationDaoCustom;

    @Mock
    CopyDao copyDao;

    @Test
    @Rollback
    public void reservationsAddUpdate() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        MockHttpServletResponse response = new MockHttpServletResponse();

        // GIVEN
        // Création du DTO et de la réservation qui sera update.
        UpdateReservationDto updateReservationDto = new UpdateReservationDto();
        updateReservationDto.setId(400L);
        updateReservationDto.setStatus("after");

        Reservation reservation = new Reservation();
        reservation.setId(400L);
        reservation.setStatus("before");
        Book book = new Book();
        book.setId(1L);
        List<Copy> copies = new ArrayList<>();
        Copy copy = new Copy();
        copy.setId(1L);
        copies.add(copy);
        book.setCopies(copies);
        updateReservationDto.setBook(book);
        reservation.setBook(book);
        reservation.setTokenuserid("testing");
        reservation.setTokenuseremail("testing");
        reservation.setCreationDate(new Timestamp(System.currentTimeMillis()));

        List<Reservation> reservations = new ArrayList<>();
        reservations.add(reservation);

        when(copyDao.findByBookId(updateReservationDto.getBook().getId())).thenReturn(book.getCopies());
        when(reservationDao.findByBookId(book.getId())).thenReturn(reservations);
        when(reservationDaoCustom.findById(updateReservationDto.getId())).thenReturn(reservation);

        // WHEN
        ResponseEntity<Object> responseEntity = reservationController.reservationsAdd(updateReservationDto, response);

        // THEN
        // On vérifie que le statut de la réservation a bien été update via le DTO.
        assertThat(reservation.getStatus()).isEqualTo("after");
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(201);
    }

    @Test
    @Rollback
    public void reservationsAddNew() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        MockHttpServletResponse response = new MockHttpServletResponse();

        // GIVEN
        // Création du DTO.
        UpdateReservationDto updateReservationDto = new UpdateReservationDto();

        Book book = new Book();
        book.setId(1L);
        List<Copy> copies = new ArrayList<>();
        Copy copy = new Copy();
        copy.setId(1L);
        copies.add(copy);
        book.setCopies(copies);
        updateReservationDto.setBook(book);
        List<Reservation> reservations = new ArrayList<>();

        when(copyDao.findByBookId(updateReservationDto.getBook().getId())).thenReturn(book.getCopies());
        when(reservationDao.findByBookId(book.getId())).thenReturn(reservations);

        // WHEN
        ResponseEntity<Object> responseEntity = reservationController.reservationsAdd(updateReservationDto, response);

        // THEN
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(201);
    }

    @Test
    @Rollback
    public void reservationsAddButUserAlreadyHaveReservationForThisBook() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        MockHttpServletResponse response = new MockHttpServletResponse();

        // GIVEN
        // Création du DTO.
        UpdateReservationDto updateReservationDto = new UpdateReservationDto();

        Book book = new Book();
        book.setId(1L);
        List<Copy> copies = new ArrayList<>();
        Copy copy = new Copy();
        copy.setId(1L);
        copies.add(copy);
        book.setCopies(copies);
        updateReservationDto.setBook(book);
        List<Reservation> reservations = new ArrayList<>();

        // Création de la réservation déjà existante ce qui rend impossible la création de la deuxième réservation.
        Reservation reservation = new Reservation();
        reservation.setId(400L);
        reservation.setBook(book);
        reservation.setTokenuserid("testing");
        reservations.add(reservation);

        updateReservationDto.setBook(book);
        updateReservationDto.setTokenuserid("testing");

        when(copyDao.findByBookId(updateReservationDto.getBook().getId())).thenReturn(book.getCopies());
        when(reservationDao.findByBookId(book.getId())).thenReturn(reservations);
        // On retourne la liste reservations qui contient déjà une réservation pour cet user sur ce livre donc 403.
        when(reservationDao.findByBookIdAndTokenuserid(updateReservationDto.getBook().getId(), updateReservationDto.getTokenuserid())).thenReturn(reservations);

        // WHEN
        ResponseEntity<Object> responseEntity = reservationController.reservationsAdd(updateReservationDto, response);

        // THEN
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(403);
    }

    @Test
    @Rollback
    public void reservationsAddButReservationsListIsFull() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        MockHttpServletResponse response = new MockHttpServletResponse();

        // GIVEN
        // Création du DTO.
        UpdateReservationDto updateReservationDto = new UpdateReservationDto();

        Book book = new Book();
        book.setId(1L);
        List<Copy> copies = new ArrayList<>();
        Copy copy = new Copy();
        copy.setId(1L);
        copies.add(copy);
        book.setCopies(copies);
        updateReservationDto.setBook(book);
        List<Reservation> reservations = new ArrayList<>();

        // Création des réservations pour qu'il y ait plus de réservations que 2x le nombre de copies. (1 seule copie dans la liste copies)
        Reservation reservation = new Reservation();
        reservation.setId(400L);
        Reservation reservation2 = new Reservation();
        reservation2.setId(401L);
        Reservation reservation3 = new Reservation();
        reservation3.setId(402L);

        reservations.add(reservation);
        reservations.add(reservation2);
        reservations.add(reservation3);
        // Il y a désormais + de réservations que 2x le nombre de copies (3 réservations et 1 copie)

        when(copyDao.findByBookId(updateReservationDto.getBook().getId())).thenReturn(book.getCopies());
        when(reservationDao.findByBookId(book.getId())).thenReturn(reservations);

        // WHEN
        ResponseEntity<Object> responseEntity = reservationController.reservationsAdd(updateReservationDto, response);

        // THEN
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(403);
    }

    // Impossible de tester la méthode findByBookIdOrderByCreationDateDesc car
    // il est impossible de Mock la liste avant le OrderBy car la méthode fait tout en un.
    // Ou alors il faudrait implémenter manuellement la méthode pour séparer le find et le OrderBy.

    @Test
    public void findAllReservations() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        MockHttpServletResponse response = new MockHttpServletResponse();

        // GIVEN
        // Création des réservations de test.
        Reservation reservation = new Reservation();
        reservation.setId(400L);
        Reservation reservation2 = new Reservation();
        reservation2.setId(401L);
        List<Reservation> reservations = new ArrayList<>();
        reservations.add(reservation);
        reservations.add(reservation2);

        // Quand on exécute la méthode findAll alors on retourne la liste de réservations de test.
        when(reservationDao.findAllByOrderByCreationDateDesc()).thenReturn(reservations);

        // WHEN
        // On exécute la méthode findAll.
        List<Reservation> result = reservationController.findAllReservations();

        // THEN
        // On vérifie si le nombre de réservations de test est de 2.
        assertThat(result.size()).isEqualTo(2);
        // On vérifie si l'id de la première réservation de test est 1.
        assertThat(result.get(0).getId()).isEqualTo(400);
        // On vérifie si l'id de la seconde réservation de test est 2.
        assertThat(result.get(1).getId()).isEqualTo(401);
    }

    @Test
    public void findByTokenuserid() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        MockHttpServletResponse response = new MockHttpServletResponse();

        // GIVEN
        // Création des réservations de test.
        Reservation reservation = new Reservation();
        reservation.setTokenuserid("testing");
        Reservation reservation2 = new Reservation();
        reservation2.setTokenuserid("testing");
        List<Reservation> reservations = new ArrayList<>();
        reservations.add(reservation);
        reservations.add(reservation2);
        // Quand on exécute la méthode findByTokenuserid alors on retourne la liste de réservations de test.
        when(reservationDaoCustom.findByTokenuserid("testing")).thenReturn(reservations);

        // WHEN
        // On exécute la méthode findByTokenuserid.
        List<Reservation> result = reservationController.findByTokenuserid("testing", response);

        // THEN
        // On vérifie si le tokenuserid des réservations de test est égal à celui de test "testing".
        assertThat(result.get(0).getTokenuserid()).isEqualTo("testing");
        assertThat(result.get(1).getTokenuserid()).isEqualTo("testing");
        // On vérifie une seconde fois que les réservations de test partagent le même tokenuserid.
        assertThat(result.get(0).getTokenuserid()).isEqualTo(result.get(1).getTokenuserid());
    }

    @Test
    @Rollback
    public void deleteReservationById() {
        Reservation reservation = new Reservation();
        reservation.setId(1L);
        when(reservationDaoCustom.findById(1L)).thenReturn(reservation);

        ResponseEntity<Object> responseEntity = reservationController.deleteReservationById(1L);

        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        // On vérifie que la méthode deleteById a bien été exécutée.
        verify(reservationDao).deleteById(anyLong());
    }

    @Test
    @Rollback
    public void deleteReservationByIdNotExist() {
        Long reservationId = 700L;
        ResponseEntity<Object> responseEntity = reservationController.deleteReservationById(reservationId);

        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(404);
        // On vérifie que la méthode deleteById n'a jamais été exécutée.
        verify(reservationDao, never()).deleteById(anyLong());
    }

    @Test
    @Rollback
    public void deleteReservationByIdNull() {
        Long reservationId = null;
        ResponseEntity<Object> responseEntity = reservationController.deleteReservationById(reservationId);

        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(404);
        // On vérifie que la méthode deleteById n'a jamais été exécutée.
        verify(reservationDao, never()).deleteById(anyLong());
    }
}
