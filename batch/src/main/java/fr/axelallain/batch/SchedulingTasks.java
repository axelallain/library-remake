package fr.axelallain.batch;

import fr.axelallain.batch.model.Loan;
import fr.axelallain.batch.proxy.BooksProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class SchedulingTasks {

    @Autowired
    public JavaMailSender emailSender;

    @Autowired
    private BooksProxy booksProxy;

    private static final Logger logger = LoggerFactory.getLogger(SchedulingTasks.class);
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    @Scheduled(fixedDelay = 2000)
    public String reminderMail() {

        List<Loan> loans = booksProxy.findAll();

        for (Loan loan : loans) {
            if (ZonedDateTime.now().toInstant().isAfter(loan.getEndingDate().toInstant())) {

                logger.info("Reminder Mail :: Date - {}", dateTimeFormatter.format(LocalDateTime.now()));

                SimpleMailMessage message = new SimpleMailMessage();

                message.setTo(loan.getTokenuseremail());
                message.setSubject("Rappel - Emprunt expiré");
                message.setText("La date de fin de votre emprunt a été atteinte. Bibliothèque de Fausseville");

                this.emailSender.send(message);

                return "Email Sent!";
            }
        }

        return "No loan has expired";
    }
}
