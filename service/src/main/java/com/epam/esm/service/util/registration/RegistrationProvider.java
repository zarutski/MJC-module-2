package com.epam.esm.service.util.registration;

import com.epam.esm.domain.dto.UserDTO;

public interface RegistrationProvider {

    void register(UserDTO userDTO);
}