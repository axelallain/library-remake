package fr.axelallain.books.controller;

import fr.axelallain.books.dao.BooksDao;
import fr.axelallain.books.dao.CopyDao;
import fr.axelallain.books.dao.ReservationDao;
import fr.axelallain.books.dao.ReservationDaoCustom;
import fr.axelallain.books.dto.UpdateReservationDto;
import fr.axelallain.books.model.Book;
import fr.axelallain.books.model.Copy;
import fr.axelallain.books.model.Loan;
import fr.axelallain.books.model.Reservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@RestController
public class ReservationController {

    @Autowired
    private ReservationDao reservationDao;

    @Autowired
    private ReservationDaoCustom reservationDaoCustom;

    @Autowired
    private CopyDao copyDao;

    @Autowired
    private BooksDao booksDao;

    @PostMapping("/reservations")
    public ResponseEntity<Object> reservationsAdd(@RequestBody UpdateReservationDto updateReservationDto, HttpServletResponse response) {

        // Initialisation de la réservation pour une future attribution. Nécessaire pour augmenter son scope.
        Reservation reservation = null;

        // Déclaration de la liste findByBookIdAndTokenuserid dès le début pour augmenter son scope.
        List<Reservation> findByBookIdAndTokenuserid = new ArrayList<>();

        // Liste des copies existantes pour ce livre (pour que la taille de la file pour ce livre soit de maximum 2x le nombre de copies).
        List<Copy> copiesList = copyDao.findByBookId(updateReservationDto.getBook().getId());
        // Liste des réservations pour ce livre (pour vérifier combien il y en a déjà, pour vérifier si on a atteint 2x le nombre de copies).
        List<Reservation> reservationsList = reservationDao.findByBookId(updateReservationDto.getBook().getId());

        // Liste des réservations que cet user a fait pour ce livre.
        if (updateReservationDto.getBook() != null && updateReservationDto.getTokenuserid() != null) {
            findByBookIdAndTokenuserid = reservationDao.findByBookIdAndTokenuserid(updateReservationDto.getBook().getId(), updateReservationDto.getTokenuserid());
        }

        // Retrait des réservations terminées pour vérifier si l'user a une réservation EN COURS uniquement sans celles archivées.
        findByBookIdAndTokenuserid.removeIf(r -> "Ended".equals(r.getStatus()));

        // Vérifie si c'est un update ou une nouvelle réservation
        if (updateReservationDto.getId() != null) {
            // Si c'est un update, on sélectionne la réservation correspondante.
            if (reservationDaoCustom.findById(updateReservationDto.getId()) != null) {
                reservation = reservationDaoCustom.findById(updateReservationDto.getId());
            } else {
                // Si un id est bel et bien fourni dans la requête mais que ce n'est pas un update, on vérifie si il existe déjà
                // une réservation pour ce livre.
                if (!findByBookIdAndTokenuserid.isEmpty()) {
                    // Si c'est le cas, erreur 403 car un user est limité à une seule réservation pour un même livre.
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("This user already have a reservation for this book.");
                } else {
                    // Si ce n'est pas le cas alors tout est bon, on peut créer une nouvelle réservation.
                    reservation = new Reservation();
                }
            }
        } else {
            // Si aucun id n'est fourni dans la requête.
            // Vérifie si le nombre de réservations existantes pour ce livre n'a pas atteint 2x le nombre de copies pour ce livre.
            if (reservationsList.size() >= copiesList.size() * 2) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Reservations list for this book is full.");
            } else {
                // On vérifie également si l'user n'a pas déjà une réservation en cours pour ce livre.
                if (!findByBookIdAndTokenuserid.isEmpty()) {
                    // Si c'est le cas, erreur 403 car un user est limité à une seule réservation pour un même livre.
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("This user already have a reservation for this book.");
                } else {
                    // Si ce n'est pas le cas alors tout est bon, on peut créer une nouvelle réservation.
                    reservation = new Reservation();
                }
            }
        }

        // Si la réservation n'est ni un update ni une nouvelle réservation.
        if (reservation == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No reservation found.");
        }

        // Vérifie si les attributs sont null un à un pour éviter un NullPointerException lors du set.
        if (updateReservationDto.getStatus() != null) {
            reservation.setStatus(updateReservationDto.getStatus());
        }

        if (updateReservationDto.getPosition() != null) {
            reservation.setPosition(updateReservationDto.getPosition());
        }

        if (updateReservationDto.getBook() != null) {
            reservation.setBook(updateReservationDto.getBook());
        }

        if (updateReservationDto.getTokenuserid() != null) {
            reservation.setTokenuserid(updateReservationDto.getTokenuserid());
        }

        if (updateReservationDto.getTokenuseremail() != null) {
            reservation.setTokenuseremail(updateReservationDto.getTokenuseremail());
        }

        reservationDao.save(reservation);
        return new ResponseEntity<>(HttpStatus.CREATED);

        /*
        Iterator<Reservation> findByBookIdAndTokenUserIdIterator = findByBookIdAndTokenuserid.iterator();
        while(findByBookIdAndTokenUserIdIterator.hasNext()) {
            Reservation r = findByBookIdAndTokenUserIdIterator.next();
            if ("Ended".equals(r.getStatus())) {
                findByBookIdAndTokenUserIdIterator.remove();
            }
        }
        */
    }

    @GetMapping("/reservations/{bookid}")
    public List<Reservation> findByBookIdOrderByCreationDateDesc(@PathVariable Long bookid) {
        return reservationDaoCustom.findByBookIdOrderByCreationDateDesc(bookid);
    }

    @GetMapping("/reservations")
    public List<Reservation> findAllReservations() {
        return reservationDao.findAllByOrderByCreationDateDesc();
    }

    @GetMapping("/reservations/user/{tokenuserid}")
    public List<Reservation> findByTokenuserid(@PathVariable String tokenuserid, HttpServletResponse response) {

        List<Reservation> reservations = reservationDaoCustom.findByTokenuserid(tokenuserid);

        if (reservations.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_OK);
            return reservations;
        } else {
            response.setStatus(HttpServletResponse.SC_OK);
            return reservations;
        }
    }

    @GetMapping("/reservations/delete/{id}")
    public ResponseEntity<Object> deleteReservationById(@PathVariable Long id) {
        if (id != null) {
            if (reservationDaoCustom.findById(id) != null) {
                reservationDao.deleteById(id);
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
