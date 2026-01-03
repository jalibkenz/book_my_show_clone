package in.kenz.bookmyshow.user.entity;

import in.kenz.bookmyshow.user.enums.ProfileStatus;
import in.kenz.bookmyshow.user.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
@DynamicUpdate
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;
    private String username;
    private String password;
    private String email;
    private String mobile;

    @Enumerated(EnumType.STRING)
    private UserRole roles;

    @Enumerated(EnumType.STRING)
    private ProfileStatus profileStatus;

    private String emergencyContactName;
    private String emergencyContactEmail;
    private String emergencyContactMobile;
}
