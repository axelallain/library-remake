package fr.axelallain.books.controller;

import fr.axelallain.books.dao.CopyDao;
import fr.axelallain.books.dao.ReservationDao;
import fr.axelallain.books.dao.ReservationDaoCustom;
import fr.axelallain.books.model.Copy;
import fr.axelallain.books.model.Loan;
import fr.axelallain.books.model.Reservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import java.io.IOException;
import java.util.List;

@RestController
public class ReservationController {

    @Autowired
    private ReservationDao reservationDao;

    @Autowired
    private ReservationDaoCustom reservationDaoCustom;

    @Autowired
    private CopyDao copyDao;

    @PostMapping("/reservations")
    public void reservationsAdd(@RequestBody Reservation reservation, HttpServletResponse response) {

        // Find all reservations by book id and tokenuserid ???
        List<Reservation> verificationList = reservationDao.findByBookIdAndTokenuserid(reservation.getBook().getId(), reservation.getTokenuserid());

        // Récupérer toutes les copies existantes pour ce livre (pour le 2x max le nombre de copies dans la file d'attente..)
        List<Copy> copiesList = copyDao.findByBookId(reservation.getBook().getId());

        // Récupérer toutes les réservations pour ce livre (pour vérifier combien il y en a déjà savoir si c'est complet ou non pour réserver)
        List<Reservation> reservationsList = reservationDao.findByBookId(reservation.getBook().getId());

        // Tri de la liste en retirant les réservations terminées
        for (int i = 0; i < verificationList.size(); i++) {
            if (verificationList.get(i).getStatus().equals("Ended")) {
                verificationList.remove(i);
            }
        }

        // Si la liste est vide (donc aucune reservation deja en cours pour cet user sur ce livre), save la reservation.
        if (reservation == null) {
            try {
                response.sendError(204, "No reservation found.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (!verificationList.isEmpty()) {
            try {
                response.sendError(403, "This user already have a reservation for this book.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (reservationsList.size() >= copiesList.size() * 2) {
            try {
                response.sendError(403, "Reservations list for this book is full.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            response.setStatus(HttpServletResponse.SC_CREATED);
            reservationDao.save(reservation);
        }
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

        List<Reservation> reservations = reservationDao.findByTokenuserid(tokenuserid);

        if (reservations.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_OK);
            return reservations;
        } else {
            response.setStatus(HttpServletResponse.SC_OK);
            return reservations;
        }
    }

    @GetMapping("/reservations/delete/{id}")
    public void deleteReservationById(@PathVariable Long id) {
        reservationDao.deleteById(id);
    }
}
