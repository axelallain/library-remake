package fr.axelallain.batch;

import fr.axelallain.batch.model.Loan;
import fr.axelallain.batch.proxy.BooksProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
public class SchedulingTasks {

    @Autowired
    public JavaMailSender emailSender;

    @Autowired
    private BooksProxy booksProxy;

    private static final Logger logger = LoggerFactory.getLogger(SchedulingTasks.class);
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    @Scheduled(fixedDelay = 10000)
    public String reminderMail() {

        System.out.println("Checking dates");

        Iterable<Loan> loansIterable = booksProxy.findAll();
        List<Loan> loans = new ArrayList<>();
        loansIterable.forEach(loans::add);

        if (loans.isEmpty()) {
            return "No loan found";

        } else {
            for (Loan loan : loans) {

                if (LocalDateTime.now().isAfter(loan.getEndingDate()) && loan.getLastReminderEmail() != null && !loan.isEnded()) {

                    // convert date to calendar
                    Calendar c = Calendar.getInstance();
                    c.setTime(Date.from(loan.getLastReminderEmail().atZone(ZoneId.systemDefault()).toInstant()));

                    // manipulate date
                    c.add(Calendar.HOUR, 24);

                    // convert calendar to date
                    Date lastReminderEmailExtended = c.getTime();

                    LocalDateTime LdtLastReminderEmailExtended = lastReminderEmailExtended.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

                    Duration duration = Duration.between(LocalDateTime.now(), LdtLastReminderEmailExtended);
                    long durationHours = duration.toHours();

                    if (durationHours <= 0) {
                        logger.info("Reminder Mail :: Date - {}", dateTimeFormatter.format(LocalDateTime.now()));

                        SimpleMailMessage message = new SimpleMailMessage();

                        message.setTo(loan.getTokenuseremail());
                        message.setSubject("Rappel - Emprunt expiré");
                        message.setText("La date de fin de votre emprunt a été atteinte. Bibliothèque de Fausseville");

                        this.emailSender.send(message);

                        loan.setLastReminderEmail(LocalDateTime.now());
                        booksProxy.loanAdd(loan);

                        return "Email Sent!";

                    }

                } else if (LocalDateTime.now().isAfter(loan.getEndingDate()) && loan.getLastReminderEmail() == null && !loan.isEnded()) {

                    logger.info("Reminder Mail :: Date - {}", dateTimeFormatter.format(LocalDateTime.now()));

                    SimpleMailMessage message = new SimpleMailMessage();

                    message.setTo(loan.getTokenuseremail());
                    message.setSubject("Rappel - Emprunt expiré");
                    message.setText("La date de fin de votre emprunt a été atteinte. Bibliothèque de Fausseville");

                    this.emailSender.send(message);

                    loan.setId(loan.getId());
                    loan.setLastReminderEmail(LocalDateTime.now());
                    loan.setStatus("Expired");
                    booksProxy.loanAdd(loan);

                    return "Email Sent!";
                }
            }
            return "No loan has expired";
        }
    }
}
