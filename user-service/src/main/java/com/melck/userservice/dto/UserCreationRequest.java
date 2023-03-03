package com.melck.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCreationRequest {

    private Boolean enabled;
    private String email;
    private String emailVerified;
    private String firstName;
    private String lastName;
    private String username;
    private List<Credential> credentials = new ArrayList<>();

}
