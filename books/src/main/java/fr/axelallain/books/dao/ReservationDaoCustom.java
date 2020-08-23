package fr.axelallain.books.dao;

import fr.axelallain.books.model.Reservation;

import java.util.List;

public interface ReservationDaoCustom {

    List<Reservation> findByBookIdOrderByCreationDateDesc(Long id);
}
