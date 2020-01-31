package fr.axelallain.clientui.controller;

import fr.axelallain.clientui.model.Book;
import fr.axelallain.clientui.proxy.BooksProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.ws.rs.QueryParam;
import java.util.List;

@Controller
public class ClientUiController {

    @Autowired
    private BooksProxy booksProxy;

    @GetMapping("/")
    public String index(Model model) {

        Iterable<Book> books = booksProxy.books();

        model.addAttribute("books", books);

        return "index";
    }

    @GetMapping("/ouvrages")
    public String ouvrages(Model model) {

        Iterable<Book> books = booksProxy.books();

        model.addAttribute("books", books);

        return "books";
    }

    @GetMapping("/prets")
    public String prets(Model model) {

        return "borrowings";
    }

    @GetMapping("/inscription")
    public String inscription() {

        return "signup";
    }

    @GetMapping("/connexion")
    public String connexion() {

        return "login";
    }

    @GetMapping("/recherche-ouvrages")
    public String rechercheOuvrages(Model model, @QueryParam("name") String name) {

        List<Book> books = booksProxy.findByNameLikeNoDuplicates(name);

        model.addAttribute("books", books);

        return "books";
    }
}
