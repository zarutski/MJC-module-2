package com.epam.esm.service.impl;

import com.epam.esm.dao.UserDao;
import com.epam.esm.domain.dto.UserDTO;
import com.epam.esm.service.UserService;
import com.epam.esm.service.exception.IdNotExistException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final ModelMapper modelMapper;

    public UserServiceImpl(UserDao userDao, ModelMapper modelMapper) {
        this.userDao = userDao;
        this.modelMapper = modelMapper;
    }

    @Override
    public UserDTO readById(Long id) {
        return userDao.readById(id)
                .map(user -> modelMapper.map(user, UserDTO.class))
                .orElseThrow(() -> new IdNotExistException(id.toString()));
    }

    @Override
    public List<UserDTO> readAll(int page, int size) {
        return userDao.readAll(page, size).stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public Long getEntitiesCount() {
        return userDao.getEntitiesCount();
    }

    @Override
    public Long getUserIdWithOrdersHighestCost() {
        return userDao.getUserIdWithOrdersHighestCost();
    }

}