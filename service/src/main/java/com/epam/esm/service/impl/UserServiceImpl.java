package com.epam.esm.service.impl;

import com.epam.esm.dao.UserDao;
import com.epam.esm.domain.dto.RoleDTO;
import com.epam.esm.domain.dto.UserDTO;
import com.epam.esm.domain.entity.User;
import com.epam.esm.service.exception.UserAlreadyExists;
import com.epam.esm.service.util.registration.RegistrationProvider;
import com.epam.esm.service.RoleService;
import com.epam.esm.service.UserService;
import com.epam.esm.service.exception.CreateEntityInternalException;
import com.epam.esm.service.exception.IdNotExistException;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private static final Long ROLE_USER_ID = 1L;

    private final UserDao userDao;
    private final ModelMapper modelMapper;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final RegistrationProvider registrationProvider;

    public UserServiceImpl(UserDao userDao, ModelMapper modelMapper, RoleService roleService,
                           PasswordEncoder passwordEncoder, RegistrationProvider registrationProvider) {
        this.userDao = userDao;
        this.modelMapper = modelMapper;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        this.registrationProvider = registrationProvider;
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
    public UserDTO findByLogin(String login) {
        return userDao.findByLogin(login)
                .map(user -> modelMapper.map(user, UserDTO.class))
                .orElseThrow(() -> new IdNotExistException(login));
    }

    @Override
    public UserDTO create(UserDTO userDto) {
        String login = userDto.getLogin();
        if (userDao.findByLogin(login).isPresent()) {
            throw new UserAlreadyExists(login);
        }
        User user = prepareUser(userDto);
        User createdUser = userDao.create(user)
                .orElseThrow(() -> new CreateEntityInternalException(login));
        return modelMapper.map(createdUser, UserDTO.class);
    }

    private User prepareUser(UserDTO userDto) {
        registrationProvider.register(userDto);
        setCredentials(userDto);
        return modelMapper.map(userDto, User.class);
    }

    private void setCredentials(UserDTO userDto) {
        RoleDTO userRole = roleService.read(ROLE_USER_ID);
        userDto.setRole(userRole);
        String password = userDto.getPassword();
        String encoded = passwordEncoder.encode(password);
        userDto.setPassword(encoded);
    }
}