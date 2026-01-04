package in.kenz.bookmyshow.notification.email.service;

import in.kenz.bookmyshow.booking.entity.Booking;
import in.kenz.bookmyshow.donation.entity.Donation;
import in.kenz.bookmyshow.notification.email.entity.EmailTemplate;
import in.kenz.bookmyshow.notification.email.enums.EmailTemplateCode;
import in.kenz.bookmyshow.notification.email.repository.EmailTemplateRepository;
import in.kenz.bookmyshow.show.repository.ShowRepository;
import in.kenz.bookmyshow.theatre.event.TheatreCreatedEvent;
import in.kenz.bookmyshow.theatre.event.TheatreProfileUpdatedEvent;
import in.kenz.bookmyshow.user.event.UserProfileUpdatedEvent;
import in.kenz.bookmyshow.show.entity.Show;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final EmailTemplateRepository templateRepository;
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final ShowRepository showRepository;

    //from email
    @Value("${spring.mail.username}")
    private String senderEmail;
    private static final String senderName = "JALIB CODES";

    //logger
    private static final Logger EMAIL_LOGGER = LoggerFactory.getLogger("EMAIL_LOGGER");


    //send
    private void send(String to, String subject, String body, boolean html) {

        try {
            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper =
                    new MimeMessageHelper(
                            message,
                            MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                            "UTF-8"
                    );

            helper.setFrom(senderEmail, senderName);   // ✅ IMPORTANT
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, html);

            mailSender.send(message);

            EMAIL_LOGGER.info(
                    "EMAIL_SENT | TO={} | SUBJECT={}",
                    to,
                    subject
            );

        } catch (Exception e) {

            EMAIL_LOGGER.error(
                    "EMAIL_FAILED | TO={} | SUBJECT={} | ERROR={}",
                    to,
                    subject,
                    e.getMessage(),
                    e
            );

            throw new IllegalStateException("Email sending failed", e);
        }
    }



    public void sendUserCreatedMail(String to, String name) {
        EMAIL_LOGGER.info("Sender email resolved as {}", senderEmail);
        EMAIL_LOGGER.info("EMAIL SERVICE ENTERED(sendUserCreatedMail) for {}", to);

        EmailTemplate template = templateRepository
                .findByIdAndEnabledTrue(EmailTemplateCode.USER_CREATED.name())
                .orElseThrow(() ->
                        new IllegalStateException("Email template USER_CREATED not found"));

        Context context = new Context();
        context.setVariable("name", name);
        context.setVariable(
                "timestamp",
                LocalDateTime.now()
                        .format(DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a"))
        );

        String subject = templateEngine.process(template.getSubject(), context);
        String body = templateEngine.process(template.getBody(), context);

        send(to, subject, body, template.isHtml());
    }





    public void sendUserProfileUpdatedMail(UserProfileUpdatedEvent event) {
        EMAIL_LOGGER.info("(sendUserProfileUpdatedMail) Sender email resolved as {}", senderEmail);
        EMAIL_LOGGER.info("EMAIL SERVICE ENTERED(sendUserProfileUpdatedMail) for {}", event.getEmail());
        EmailTemplate template = templateRepository
                .findByIdAndEnabledTrue(
                        EmailTemplateCode.USER_PROFILE_UPDATED.name()
                )
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Email template USER_PROFILE_UPDATED not found"
                        )
                );

        Context context = new Context();
        context.setVariable("oldName", event.getOldName());
        context.setVariable("newName", event.getNewName());
        context.setVariable("oldEmergencyContactName", event.getOldEmergencyContactName());
        context.setVariable("newEmergencyContactName", event.getNewEmergencyContactName());
        context.setVariable("oldEmergencyContactEmail", event.getOldEmergencyContactEmail());
        context.setVariable("newEmergencyContactEmail", event.getNewEmergencyContactEmail());
        context.setVariable("oldEmergencyContactMobile", event.getOldEmergencyContactMobile());
        context.setVariable("newEmergencyContactMobile", event.getNewEmergencyContactMobile());

        context.setVariable(
                "timestamp",
                LocalDateTime.now()
                        .format(DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a"))
        );

        String subject = templateEngine.process(template.getSubject(), context);
        String body = templateEngine.process(template.getBody(), context);

        send(event.getEmail(), subject, body, template.isHtml());
    }


    public void sendDonationSuccessMail(Donation donation, byte[] pdfBytes) {

        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper =
                    new MimeMessageHelper(message, true, "UTF-8");

        /* =============================
           1. Load email template from MongoDB
           ============================= */
            EmailTemplate template = templateRepository
                    .findByIdAndEnabledTrue("DONATION_THANK_YOU")
                    .orElseThrow(() ->
                            new IllegalStateException(
                                    "Email template DONATION_THANK_YOU not found or disabled"
                            )
                    );

        /* =============================
           2. Build Thymeleaf context
           ============================= */
            Context context = new Context();
            context.setVariable("name", donation.getName());
            context.setVariable("amount", donation.getAmount());
            context.setVariable("receiptNumber", donation.getId());
            context.setVariable("donationTimestamp", donation.getDonatedAt());
            context.setVariable("donationId", donation.getId());

        /* =============================
           3. Render HTML from Mongo template
           ============================= */
            String body = templateEngine.process(
                    template.getBody(),   // ✅ IMPORTANT: body, not template name
                    context
            );

        /* =============================
           4. Prepare email
           ============================= */
            helper.setTo(donation.getEmail());
            helper.setSubject(template.getSubject());
            helper.setText(body, true);

        /* =============================
           5. Attach PDF certificate
           ============================= */
            helper.addAttachment(
                    "Donation-Certificate.pdf",
                    new ByteArrayResource(pdfBytes)
            );

        /* =============================
           6. Send email
           ============================= */
            mailSender.send(message);

        } catch (Exception ex) {
            throw new IllegalStateException(
                    "Failed to send donation success email", ex
            );
        }
    }


    public void sendTheatreCreatedEmail(TheatreCreatedEvent event) {

        EMAIL_LOGGER.info("EMAIL SERVICE ENTERED(sendTheatreCreatedEmail) for {}", event.getEmail());

        EmailTemplate template = templateRepository
                .findByIdAndEnabledTrue(EmailTemplateCode.THEATRE_CREATED.name())
                .orElseThrow(() ->
                        new IllegalStateException("Email template THEATRE_CREATED not found"));

        Context context = new Context();

        // Theatre details (FULL)
        context.setVariable("name", event.getName());
        context.setVariable("city", event.getCity());
        context.setVariable("state", event.getState());
        context.setVariable("country", event.getCountry());
        context.setVariable("email", event.getEmail());
        context.setVariable("mobile", event.getMobile());
        context.setVariable(
                "timestamp",
                LocalDateTime.now()
                        .format(DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a"))
        );

        String subject = templateEngine.process(template.getSubject(), context);
        String body = templateEngine.process(template.getBody(), context);

        send(event.getEmail(), subject, body, template.isHtml());
    }

    public void sendTheatreProfileUpdatedEmail(TheatreProfileUpdatedEvent event) {

        EMAIL_LOGGER.info(
                "EMAIL SERVICE ENTERED(sendTheatreProfileUpdatedEmail) for {}",
                event.getNewEmail()
        );

        EmailTemplate template = templateRepository
                .findByIdAndEnabledTrue(EmailTemplateCode.THEATRE_UPDATED.name())
                .orElseThrow(() ->
                        new IllegalStateException("Email template THEATRE_UPDATED not found"));

        Context context = new Context();

        context.setVariable("nameOld", event.getOldName());
        context.setVariable("nameNew", event.getNewName());

        context.setVariable("addressOld", event.getOldAddress());
        context.setVariable("addressNew", event.getNewAddress());

        context.setVariable("cityOld", event.getOldCity());
        context.setVariable("cityNew", event.getNewCity());

        context.setVariable("stateOld", event.getOldState());
        context.setVariable("stateNew", event.getNewState());

        context.setVariable("countryOld", event.getOldCountry());
        context.setVariable("countryNew", event.getNewCountry());

        context.setVariable("emailOld", event.getOldEmail());
        context.setVariable("emailNew", event.getNewEmail());

        context.setVariable("mobileOld", event.getOldMobile());
        context.setVariable("mobileNew", event.getNewMobile());

        context.setVariable("profileStatusOld", event.getOldProfileStatus());
        context.setVariable("profileStatusNew", event.getNewProfileStatus());

        context.setVariable("emergencyContactNameOld", event.getOldEmergencyContactName());
        context.setVariable("emergencyContactNameNew", event.getNewEmergencyContactName());

        context.setVariable("emergencyContactEmailOld", event.getOldEmergencyContactEmail());
        context.setVariable("emergencyContactEmailNew", event.getNewEmergencyContactEmail());

        context.setVariable("emergencyContactMobileOld", event.getOldEmergencyContactMobile());
        context.setVariable("emergencyContactMobileNew", event.getNewEmergencyContactMobile());

        context.setVariable(
                "timestamp",
                LocalDateTime.now()
                        .format(DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a"))
        );

        String subject = templateEngine.process(template.getSubject(), context);
        String body = templateEngine.process(template.getBody(), context);

        send(event.getNewEmail(), subject, body, template.isHtml());
    }

    public void sendBookingTicketEmail(String to, String name, Booking booking, byte[] pdfBytes) {
        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            // Try to load template from MongoDB
            EmailTemplate template = templateRepository
                    .findByIdAndEnabledTrue("BOOKING_TICKET")
                    .orElse(null);

            Context context = new Context();
            context.setVariable("name", name);
            context.setVariable("bookingId", booking.getId());
            context.setVariable("seatNumber", booking.getSeatNumber());
            context.setVariable("amount", booking.getAmount());

            // attempt to load show details
            try {
                showRepository.findById(booking.getShowId()).ifPresent(show -> {
                    context.setVariable("screenName", show.getScreenName());
                    context.setVariable("showStart", show.getStartTime().format(DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a")));
                    context.setVariable("showEnd", show.getEndTime().format(DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a")));
                });
            } catch (Exception ignored) {
            }

            String subject;
            String body;

            if (template != null) {
                subject = templateEngine.process(template.getSubject(), context);
                body = templateEngine.process(template.getBody(), context);
                helper.setText(body, template.isHtml());
                helper.setSubject(subject);
            } else {
                // fallback to simple subject/body
                subject = "Your Ticket - Booking " + booking.getId();
                body = templateEngine.process("booking-ticket", context);
                helper.setSubject(subject);
                helper.setText(body, true);
            }

            helper.setFrom(senderEmail, senderName);
            helper.setTo(to);

            helper.addAttachment("ticket.pdf", new ByteArrayResource(pdfBytes));

            mailSender.send(message);

            EMAIL_LOGGER.info("BOOKING_TICKET_EMAIL_SENT | TO={} | BOOKING={}", to, booking.getId());

        } catch (Exception ex) {
            EMAIL_LOGGER.error("BOOKING_TICKET_EMAIL_FAILED | BOOKING={}", booking.getId(), ex);
            throw new IllegalStateException("Failed to send booking ticket email", ex);
        }
    }

}
