package fr.axelallain.books.controller;

import fr.axelallain.books.dao.ReservationDao;
import fr.axelallain.books.model.Reservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
public class ReservationController {

    @Autowired
    private ReservationDao reservationDao;

    @PostMapping("/reservations")
    public void reservationsAdd(Reservation reservation, HttpServletResponse response) {

        if (reservation == null) {
            response.setStatus(HttpServletResponse.SC_CREATED, "Your request has an empty body");
        } else {
            response.setStatus(HttpServletResponse.SC_CREATED);
            reservationDao.save(reservation);
        }
    }
}
