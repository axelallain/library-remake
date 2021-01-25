package fr.axelallain.batch;

import fr.axelallain.batch.dto.UpdateBookDto;
import fr.axelallain.batch.dto.UpdateCopyDto;
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

                if (LocalDateTime.now().isAfter(loan.getEndingDate()) && loan.getLastReminderEmail() != null && !loan.isEnded() && !loan.getStatus().equals("Canceled")) {

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

                } else if (LocalDateTime.now().isAfter(loan.getEndingDate()) && loan.getLastReminderEmail() == null && !loan.isEnded() && !loan.getStatus().equals("Canceled")) {

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

                    // Lorsque la copie en retard est retournée physiquement, c'est au personnel de la bibliothèque de mettre fin à l'emprunt

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

        System.out.println("Les positions dans la file ont été assignées");

        // Changement de locataire pour la copie, création d'un nouvel emprunt

        for(int i = 0; i < reservations.size(); i++) {

            if(reservations.get(i).getBook().getCopies() != null && reservations.get(i).getStatus().equals("Started")) {

                // Verifier si l'utilisateur de la reservation est prioritaire
                if(i == 0) {

                    // Changer l'utilisateur de l'exemplaire
                    UpdateLoanDto updateLoanDto = new UpdateLoanDto();
                    updateLoanDto.setTokenuserid(reservations.get(i).getTokenuserid());
                    updateLoanDto.setTokenuseremail(reservations.get(i).getTokenuseremail());

                    // Assigner l'id de la copie concernée (première copie de la liste qui est available pour ce livre)
                    List<Copy> bookCopies = (List<Copy>) reservations.get(i).getBook().getCopies();

                    for (int k = 0; k < bookCopies.size(); k++) {
                        if (bookCopies.get(k).isAvailable()) {
                            updateLoanDto.setCopy(bookCopies.get(k));
                        }
                    }

                    // Passer la copie sur indisponible une fois la copie assignée au loan
                    UpdateCopyDto updateCopyDto = new UpdateCopyDto();
                    updateCopyDto.setId(updateLoanDto.getCopy().getId());
                    updateCopyDto.setAvailable(false);
                    booksProxy.copyAdd(updateCopyDto);

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

			        // Ajouter l'endingDate (un prêt à sa création est d'une durée de 4 semaines)
                    Calendar c = Calendar.getInstance();
                    c.setTime(Date.from(updateLoanDto.getStartingDate().atZone(ZoneId.systemDefault()).toInstant()));
                    c.add(Calendar.HOUR, 672); // 28 Days (4 weeks)
                    Date endingDate = c.getTime();
                    LocalDateTime LdtEndingDate = endingDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                    updateLoanDto.setEndingDate(LdtEndingDate);

                    // Persister le nouvel objet Loan
                    booksProxy.loanAdd(updateLoanDto);

                    // Changement du statut de la réservation (elle est devenue un loan)
                    UpdateReservationDto updateReservationDto = new UpdateReservationDto();
                    updateReservationDto.setId(reservations.get(i).getId());
                    updateReservationDto.setStatus("Ended");
                    updateReservationDto.setPosition(reservations.get(i).getPosition());
                    updateReservationDto.setBook(reservations.get(i).getBook());
                    updateReservationDto.setTokenuserid(reservations.get(i).getTokenuserid());
                    updateReservationDto.setTokenuseremail(reservations.get(i).getTokenuseremail());
                    booksProxy.reservationsAdd(updateReservationDto);

                    System.out.println("Le statut de la réservation est désormais Ended");

                } else {
                    System.out.println("Cette réservation n'est pas prioritaire dans la file d'attente des réservations pour ce livre");
                }

            } else {
                System.out.println("Aucune réservation sur Started ou aucun exemplaire disponible pour le livre demandé dans cette réservation");
            }
        }

        // ATTRIBUTION DES DATES DE RETOUR LES PLUS PROCHES POUR CHAQUE LIVRE PEU IMPORTE L'EXEMPLAIRE
        Iterable<Book> booksIterable = booksProxy.books();
        List<Book> booksList = new ArrayList<>();
        booksIterable.forEach(booksList::add);

        Iterable <Loan> loansIterable = booksProxy.findAll();
        List<Loan> loansList = new ArrayList<>();
        loansIterable.forEach(loansList::add);

        for (int w = 0; w < booksList.size(); w++) {
            for (int x = 0; x < loansList.size(); x++) {
                if (loansList.get(x).getCopy().getBook().getId() == booksList.get(w).getId()) {

                    UpdateBookDto updateBookDto = new UpdateBookDto();
                    updateBookDto.setId(booksList.get(w).getId());
                    updateBookDto.setName(booksList.get(w).getName());
                    updateBookDto.setAuthor(booksList.get(w).getAuthor());
                    updateBookDto.setPublisher(booksList.get(w).getPublisher());
                    updateBookDto.setCopies((List<Copy>) booksList.get(w).getCopies());
                    updateBookDto.setReservations(booksList.get(w).getReservations());
                    updateBookDto.setNextReturnDate(loansList.get(x).getEndingDate());

                    booksProxy.booksAdd(updateBookDto);
                }
            }
        }

        System.out.println("Les dates de retour les plus proches pour chaque livre peu importe l'exemplaire ont été assignées");

        for (int l = 0; l < loansList.size(); l++) {
            // Comparer heure locale et date de creation de l'emprunt
            // convert date to calendar
            Calendar cal = Calendar.getInstance();
            cal.setTime(Date.from(loansList.get(l).getStartingDate().atZone(ZoneId.systemDefault()).toInstant()));
            // manipulate date
            cal.add(Calendar.HOUR, 48);
            // convert calendar to date
            Date startingDateExtended = cal.getTime();

            LocalDateTime LdtStartingDateExtended = startingDateExtended.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

            Duration duration = Duration.between(LocalDateTime.now(), LdtStartingDateExtended);
            long durationHours = duration.toHours();

            // Comparer heure locale et date de creation de l'emprunt, Si temps écoulé > 48h && reservations.get(i).getStatus.equals("Pending")
            if (durationHours <= 0 && loansList.get(l).getStatus().equals("Started")) {
                // Le personnel de la bibliothèque doit passer le statut manuellement sur "Validé" lorsque la copie est récupérée physiquement.
                UpdateLoanDto updateLoanDtoAnnule = new UpdateLoanDto();
                updateLoanDtoAnnule.setId(loansList.get(l).getId());
                updateLoanDtoAnnule.setStatus("Canceled");
                booksProxy.loanAdd(updateLoanDtoAnnule);

                UpdateCopyDto updateCopyDtoAnnule = new UpdateCopyDto();
                updateCopyDtoAnnule.setId(loansList.get(l).getCopy().getId());
                updateCopyDtoAnnule.setAvailable(true);
                booksProxy.copyAdd(updateCopyDtoAnnule);
                System.out.println("La copie " + loansList.get(l).getCopy().getId() + " est de nouveau disponible suite à un emprunt annulé");
                return "L'utilisateur n'est pas allé chercher l'exemplaire, l'emprunt est annulé";
            }
        }

        return "Fin du batch";
    }
}
