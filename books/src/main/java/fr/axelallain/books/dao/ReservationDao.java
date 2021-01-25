package fr.axelallain.books.dao;

import fr.axelallain.books.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationDao extends JpaRepository<Reservation, Long> {

    @Override
    <S extends Reservation> S save(S s);

    List<Reservation> findAllByOrderByCreationDateDesc();

    @Override
    void deleteById(Long id);

    List<Reservation> findByBookIdAndTokenuserid(Long bookid, String tokenuserid);

    List<Reservation> findByBookId(Long id);
}
