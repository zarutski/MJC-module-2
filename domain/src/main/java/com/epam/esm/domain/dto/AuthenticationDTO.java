package com.epam.esm.domain.dto;

import lombok.Data;
import javax.validation.constraints.NotEmpty;

@Data
public class AuthenticationDTO {

    @NotEmpty
    private String login;
    @NotEmpty
    private String password;

}