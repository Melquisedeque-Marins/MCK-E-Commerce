package com.melck.userservice.dto;

import com.melck.userservice.enuns.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserNotification {
    private String fullName;
    private String email;
}
