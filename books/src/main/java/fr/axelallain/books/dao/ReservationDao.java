package fr.axelallain.books.dao;

import fr.axelallain.books.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationDao extends JpaRepository<Reservation, Long> {

    @Override
    <S extends Reservation> S save(S s);
}
