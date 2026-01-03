package in.kenz.bookmyshow.donation.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateDonationRequest {

    @NotNull
    @Min(1)
    private Integer amount;        // INR (minimum ₹1)

    @NotBlank(message = "Name is mandatory")
    private String name;

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Mobile number is mandatory")
    @Pattern(
            regexp = "^[6-9]\\d{9}$",
            message = "Enter a valid 10-digit Indian mobile number"
    )
    private String mobile;

    @NotBlank(message = "Gender is mandatory")
    private String gender;   // MALE / FEMALE / OTHER
}