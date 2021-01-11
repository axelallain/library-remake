package fr.axelallain.batch;

import fr.axelallain.batch.dto.UpdateBookDto;
import fr.axelallain.batch.dto.UpdateLoanDto;
import fr.axelallain.batch.dto.UpdateReservationDto;
import fr.axelallain.batch.model.Book;
import fr.axelallain.batch.model.Copy;
import fr.axelallain.batch.model.Loan;
import fr.axelallain.batch.model.Reservation;
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

                        UpdateLoanDto updateLoanDto = new UpdateLoanDto();
                        updateLoanDto.setId(loan.getId());
                        updateLoanDto.setLastReminderEmail(LocalDateTime.now());

                        booksProxy.loanAdd(updateLoanDto);

                        return "Email Sent!";

                    }

                } else if (LocalDateTime.now().isAfter(loan.getEndingDate()) && loan.getLastReminderEmail() == null && !loan.isEnded()) {

                    logger.info("Reminder Mail :: Date - {}", dateTimeFormatter.format(LocalDateTime.now()));

                    SimpleMailMessage message = new SimpleMailMessage();

                    message.setTo(loan.getTokenuseremail());
                    message.setSubject("Rappel - Emprunt expiré");
                    message.setText("La date de fin de votre emprunt a été atteinte. Bibliothèque de Fausseville");

                    this.emailSender.send(message);

                    UpdateLoanDto updateLoanDto = new UpdateLoanDto();

                    updateLoanDto.setId(loan.getId());
                    updateLoanDto.setLastReminderEmail(LocalDateTime.now());
                    updateLoanDto.setStatus("Expired");
                    booksProxy.loanAdd(updateLoanDto);

                    return "Email Sent!";
                }
            }
            return "No loan has expired";
        }
    }

    @Scheduled(fixedDelay = 10000)
    public String nomProvisoireReservation() {

        System.out.println("Checking reservations..");

        // Recuperer toutes les reservations existantes triées par creationDate
        List<Reservation> reservations = booksProxy.findAllReservations();

        // ATTRIBUTION DES POSITIONS DANS LA FILE
        for (int i = 0; i < reservations.size(); i++) {
            UpdateReservationDto updateReservationDto = new UpdateReservationDto();
            updateReservationDto.setId(reservations.get(i).getId());
            updateReservationDto.setPosition((long) i);
            // SET BOOKID
            updateReservationDto.setBook(reservations.get(i).getBook());
            // SET TOKENUSERID
            updateReservationDto.setTokenuserid(reservations.get(i).getTokenuserid());
            updateReservationDto.setTokenuseremail(reservations.get(i).getTokenuseremail());
            booksProxy.reservationsAdd(updateReservationDto);
        }

        System.out.println(reservations);

        // ATTRIBUTION DES DATES DE RETOUR LES PLUS PROCHES POUR CHAQUE LIVRE PEU IMPORTE L'EXEMPLAIRE
        Iterable<Book> booksIterable = booksProxy.books();
        List<Book> booksList = new ArrayList<>();
        booksIterable.forEach(booksList::add);

        Iterable <Loan> loansIterable = booksProxy.findAll();
        List<Loan> loansList = new ArrayList<>();
        loansIterable.forEach(loansList::add);

        for (int i = 0; i < booksList.size(); i++) {
            for (int j = 0; j < loansList.size(); j++) {
                if (loansList.get(j).getCopy().getBook().getId() == booksList.get(i).getId()) {

                    UpdateBookDto updateBookDto = new UpdateBookDto();
                    updateBookDto.setId(booksList.get(i).getId());
                    updateBookDto.setName(booksList.get(i).getName());
                    updateBookDto.setAuthor(booksList.get(i).getAuthor());
                    updateBookDto.setPublisher(booksList.get(i).getPublisher());
                    updateBookDto.setCopies((List<Copy>) booksList.get(i).getCopies());
                    updateBookDto.setReservations(booksList.get(i).getReservations());
                    updateBookDto.setNextReturnDate(loansList.get(j).getEndingDate());

                    booksProxy.booksAdd(updateBookDto);
                }
            }
        }


        for(int i = 0; i < reservations.size(); i++) {

            if(reservations.get(i).getBook().getCopies() != null) {

                // Verifier si l'utilisateur de la reservation est prioritaire
                if(i == 0) {

                    // Changer l'utilisateur de l'exemplaire
                    UpdateLoanDto updateLoanDto = new UpdateLoanDto();
                    updateLoanDto.setTokenuserid(reservations.get(i).getTokenuserid());

                    // Changement du statut de la reservation (supprimé car statut changé manuellement lorsque le nouvel utilisateur vient chercher l'exemplaire)
                    // reservations.get(i).setStatus("Validé");

                    // Envoi du mail
                    SimpleMailMessage message = new SimpleMailMessage();
                    message.setTo(reservations.get(i).getTokenuseremail());
                    message.setSubject("Votre exemplaire est disponible");
                    message.setText("Vous avez 48h pour venir chercher votre exemplaire !");
                    this.emailSender.send(message);

                    // Creer une date sur l'heure locale
			        updateLoanDto.setStartingDate(LocalDateTime.now());

                    // Persister le nouvel objet Loan
                    booksProxy.loanAdd(updateLoanDto);

                    // Comparer heure locale et date de creation de l'emprunt
                    // convert date to calendar
                    Calendar c = Calendar.getInstance();
                    c.setTime(Date.from(updateLoanDto.getStartingDate().atZone(ZoneId.systemDefault()).toInstant()));
                    // manipulate date
                    c.add(Calendar.HOUR, 48);
                    // convert calendar to date
                    Date startingDateExtended = c.getTime();

                    LocalDateTime LdtStartingDateExtended = startingDateExtended.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

                    Duration duration = Duration.between(LocalDateTime.now(), LdtStartingDateExtended);
                    long durationHours = duration.toHours();

                    // Comparer heure locale et date de creation de l'emprunt, Si temps écoulé > 48h && reservations.get(i).getStatus.equals("Pending")
                    if (durationHours <= 0 && reservations.get(i).getStatus().equals("Pending")) {
                        reservations.get(i).setStatus("Annulé");
                        return "L'utilisateur n'est pas allé chercher l'exemplaire, la réservation est annulée";
                    }

                    return "Mail envoyé";

                } else {
                    return "Cette réservation n'est pas prioritaire dans la file d'attente des réservations de ce livre";
                }

            } else {
                return "Aucun exemplaire disponible pour le livre demandé dans cette réservation";
            }
        }

        return "";
    }
}
