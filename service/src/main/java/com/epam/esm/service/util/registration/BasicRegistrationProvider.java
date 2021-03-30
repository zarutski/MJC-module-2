package com.epam.esm.service.util.registration;

import com.epam.esm.domain.dto.UserDTO;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("basic")
public class BasicRegistrationProvider implements RegistrationProvider {

    @Override
    public void register(UserDTO userDTO) {
    }
}