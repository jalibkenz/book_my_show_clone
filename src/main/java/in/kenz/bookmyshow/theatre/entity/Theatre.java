package in.kenz.bookmyshow.theatre.entity;

import in.kenz.bookmyshow.theatre.enums.ProfileStatus;
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
@Table(name = "theatres")
@DynamicUpdate
public class Theatre {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;
    private String Address;

    private String city;
    private String state;
    private String country;

    private String email;
    private String mobile;


    @Enumerated(EnumType.STRING)
    private ProfileStatus profileStatus;


    private String emergencyContactName;
    private String emergencyContactEmail;
    private String emergencyContactMobile;
}
