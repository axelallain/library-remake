package fr.axelallain.books.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class LoanExtensionException extends RuntimeException {

    public LoanExtensionException(String s) {
        super(s);
    }
}
