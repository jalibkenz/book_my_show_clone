package in.kenz.bookmyshow.notification.email.service;

import in.kenz.bookmyshow.donation.entity.Donation;
import in.kenz.bookmyshow.notification.email.entity.EmailTemplate;
import in.kenz.bookmyshow.notification.email.enums.EmailTemplateCode;
import in.kenz.bookmyshow.notification.email.repository.EmailTemplateRepository;
import in.kenz.bookmyshow.user.event.UserProfileUpdatedEvent;
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





}
