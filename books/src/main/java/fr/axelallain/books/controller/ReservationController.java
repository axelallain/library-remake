package fr.axelallain.books.controller;

import fr.axelallain.books.dao.ReservationDao;
import fr.axelallain.books.dao.ReservationDaoCustom;
import fr.axelallain.books.model.Loan;
import fr.axelallain.books.model.Reservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import java.util.List;

@RestController
public class ReservationController {

    @Autowired
    private ReservationDao reservationDao;

    @Autowired
    private ReservationDaoCustom reservationDaoCustom;

    @PostMapping("/reservations")
    public void reservationsAdd(@RequestBody Reservation reservation, HttpServletResponse response) {

        if (reservation == null) {
            response.setStatus(HttpServletResponse.SC_CREATED, "Your request has an empty body");
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
}
