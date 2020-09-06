package fr.axelallain.clientui.controller;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import fr.axelallain.clientui.model.Book;
import fr.axelallain.clientui.model.Loan;
import fr.axelallain.clientui.model.Reservation;
import fr.axelallain.clientui.proxy.BooksProxy;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.RefreshableKeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.account.SimpleKeycloakAccount;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import java.io.IOException;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;

@Controller
public class ClientUiController {

    @Autowired
    private BooksProxy booksProxy;

    @Context
    SecurityContext securityContext;

    public boolean isAuthenticated(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();
    }

    @GetMapping("/")
    public String index(Model model) {

        Iterable<Book> books = booksProxy.books();

        model.addAttribute("books", books);

        return "index";
    }

    @GetMapping("/ouvrages")
    public String ouvrages(Model model, HttpServletRequest request, HttpServletResponse response) {

        Iterable<Book> books = booksProxy.books();

        model.addAttribute("books", books);

        if (isAuthenticated()) {
            model.addAttribute("cuserid", request.getUserPrincipal().getName());

            KeycloakAuthenticationToken token = (KeycloakAuthenticationToken) request.getUserPrincipal();
            KeycloakPrincipal principal=(KeycloakPrincipal)token.getPrincipal();
            KeycloakSecurityContext session = principal.getKeycloakSecurityContext();
            AccessToken accessToken = session.getToken();
            model.addAttribute("cuseremail", accessToken.getEmail());
        }

        return "books";
    }

    @GetMapping("/prets")
    public String prets(Model model, ClientUiTokenController clientUiTokenController, HttpServletRequest request, HttpServletResponse response) {

        List<Loan> loans = booksProxy.findByTokenuserid(clientUiTokenController.currentUserId(request, response));

        model.addAttribute("loans", loans);

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

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) throws ServletException {
        request.logout();

        return "redirect:/";
    }

    @GetMapping("/recherche-ouvrages")
    public String rechercheOuvrages(Model model, @QueryParam("name") String name, HttpServletRequest request, HttpServletResponse response) throws IOException {

        List<Book> books = booksProxy.findByNameContainingIgnoreCase(name);

        model.addAttribute("books", books);
        model.addAttribute("reservation", new Reservation());

        if (isAuthenticated()) {
            model.addAttribute("cuserid", request.getUserPrincipal().getName());

            KeycloakAuthenticationToken token = (KeycloakAuthenticationToken) request.getUserPrincipal();
            KeycloakPrincipal principal=(KeycloakPrincipal)token.getPrincipal();
            KeycloakSecurityContext session = principal.getKeycloakSecurityContext();
            AccessToken accessToken = session.getToken();
            model.addAttribute("cuseremail", accessToken.getEmail());
        }

        return "books";
    }

    @PostMapping("/loan/{id}/extension")
    public String extensionDate(@PathVariable Long id) {

        booksProxy.extensionDate(id);

        return "redirect:/prets";
    }

    @PostMapping("/reservation")
    public String reservation(int bookid, String tokenuserid, String tokenuseremail, HttpServletResponse response) {

        if (isAuthenticated()) {
            Reservation reservation = new Reservation();
            Book book = new Book();
            book.setId(bookid);
            reservation.setBook(book);
            reservation.setTokenuserid(tokenuserid);
            reservation.setTokenuseremail(tokenuseremail);
            booksProxy.reservationsAdd(reservation);
        } else {
            try {
                response.sendError(401, "No authenticated user found.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return "redirect:/reservations";
    }

    @GetMapping("/reservations")
    public String reservations(Model model, ClientUiTokenController clientUiTokenController, HttpServletRequest request, HttpServletResponse response) {

        List<Reservation> reservations = booksProxy.findAllReservationsByTokenuserid(clientUiTokenController.currentUserId(request, response));

        model.addAttribute("reservations", reservations);

        return "reservations";
    }

    @GetMapping("/reservations/delete/{id}")
    public String deleteReservationById(@PathVariable Long id) {

        booksProxy.deleteReservationById(id);

        return "redirect:/reservations";
    }
}
