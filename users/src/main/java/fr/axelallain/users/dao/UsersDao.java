package fr.axelallain.users.dao;

import fr.axelallain.users.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersDao extends JpaRepository<User, Integer> {

    User findByNumber(String email);
}
