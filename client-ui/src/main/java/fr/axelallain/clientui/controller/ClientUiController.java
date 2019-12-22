package fr.axelallain.clientui.controller;

import fr.axelallain.clientui.model.Book;
import fr.axelallain.clientui.proxy.BooksProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ClientUiController {

    @Autowired
    private BooksProxy booksProxy;

    @GetMapping("/")
    public String home(Model model) {

        Iterable<Book> books = booksProxy.books();

        model.addAttribute("books", books);

        return "home";
    }
}
