package in.kenz.bookmyshow.notification.email.entity;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "email_templates")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailTemplate {

    @Id
    private String id; // USER_CREATED

    private String subject;
    private String body;
    private boolean html;
    private boolean enabled;
}
