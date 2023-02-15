package com.melck.userservice.dto;

import com.melck.userservice.enuns.Gender;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequest {
    @NotBlank(message = "The name field is required")
    @Size(min = 5, max = 100)
    private String fullName;
    @NotNull(message = "This field is required")
    private Gender gender;
    @NotNull(message = "the birth date field is required")
    @Past(message = "enter a valid date of birth")
//    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate birthDate;
    @CPF
    @NotBlank(message = "The cpf field is required")
    private String cpf;
    @NotEmpty(message = "The phone number field is required")
    @Size(min = 13, max = 15)
    private String phoneNumber;
    private Boolean whatsappNotifications;
    @Email(message = "Use an email pattern containing @ and .com")
    @NotEmpty(message = "The email field is required")
    private String email;
    private Boolean emailNotifications;
    @NotBlank(message = "the password field is required")
    @Size(min = 8, max = 8, message = "The password must contain 8 characters")
    private String password;
    
}
