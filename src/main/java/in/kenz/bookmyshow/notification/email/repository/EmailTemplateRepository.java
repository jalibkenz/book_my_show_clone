package in.kenz.bookmyshow.notification.email.repository;

import in.kenz.bookmyshow.notification.email.entity.EmailTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface EmailTemplateRepository extends MongoRepository<EmailTemplate, String> {
    Optional<EmailTemplate> findByIdAndEnabledTrue(String id);
}