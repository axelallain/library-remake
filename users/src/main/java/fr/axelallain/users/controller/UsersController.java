package fr.axelallain.users.controller;

import fr.axelallain.users.dao.UsersDao;
import fr.axelallain.users.exception.UserNotFoundException;
import fr.axelallain.users.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class UsersController {

    @Autowired
    private UsersDao usersDao;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @GetMapping("/users/{number}")
    public User findByNumber(@PathVariable String number) {

        User user = usersDao.findByNumber(number);

        if (user == null) {
            throw new UserNotFoundException("The user with number " + number + " cannot be found");
        }

        return user;
    }

    @GetMapping("/users")
    public Iterable<User> users() {

        return usersDao.findAll();
    }

    @GetMapping("/users/{id}")
    public Optional<User> usersById(@PathVariable int id) {

        Optional<User> user = usersDao.findById(id);

        if (user.isEmpty()) {
            throw new UserNotFoundException("The user with id " + id + " cannot be found");
        }

        return user;
    }

    @PostMapping("/users")
    public void usersAdd(User user) {
        user.setPassword(passwordEncoder().encode(user.getPassword()));
        usersDao.save(user);
    }

    @PutMapping("/users/{id}")
    public void usersEdit(@RequestBody User user, @PathVariable int id) {

        Optional<User> UserOptional = usersDao.findById(id);

        if (UserOptional.isEmpty()) {
            throw new UserNotFoundException("The user with id " + id + " cannot be found");
        }

        user.setId(id);

        usersDao.save(user);
    }

    @DeleteMapping("/users/{id}")
    public void usersDelete(@PathVariable int id) {

        usersDao.deleteById(id);
    }
}
