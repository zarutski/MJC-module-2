package com.epam.esm.service;

import com.epam.esm.domain.dto.UserDTO;

import java.util.List;

public interface UserService {

    UserDTO readById(Long id);

    List<UserDTO> readAll(int page, int size);

    Long getEntitiesCount();

}