package fr.axelallain.clientui.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class ClientUiTokenController {

    @GetMapping("/currentuserid")
    public String currentUserId(HttpServletRequest request) {

        return request.getUserPrincipal().getName();
    }
}
