package com.epam.esm.service.impl;

import com.epam.esm.dao.UserDao;
import com.epam.esm.domain.dto.RoleDTO;
import com.epam.esm.domain.dto.UserDTO;
import com.epam.esm.domain.entity.User;
import com.epam.esm.service.RoleService;
import com.epam.esm.service.exception.IdNotExistException;
import com.epam.esm.service.exception.UserAlreadyExists;
import com.epam.esm.service.util.registration.RegistrationProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private RegistrationProvider registrationProvider;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private RoleService roleService;
    @Mock
    private UserDao userDao;
    @Mock
    private ModelMapper modelMapper;
    @InjectMocks
    private UserServiceImpl userService;

    private static final Integer PAGE_NUMBER = 1;
    private static final Integer RECORDS_PER_PAGE = 3;
    private static final Long RECORD_ID = 1L;
    private static final Long ENTITIES_COUNT = 2L;

    private static final String PASSWORD = "password";
    private static final String LOGIN = "user_login";

    @Test
    void readByIdPositive() {
        UserDTO userDTO = new UserDTO();
        User user = new User();

        when(userDao.readById(RECORD_ID)).thenReturn(Optional.of(user));
        when(modelMapper.map(user, UserDTO.class)).thenReturn(userDTO);
        assertEquals(userDTO, userService.readById(RECORD_ID));
    }

    @Test
    void readByIdNegative() {
        when(userDao.readById(RECORD_ID)).thenReturn(Optional.empty());
        assertThrows(IdNotExistException.class, () -> userService.readById(RECORD_ID));
    }

    @Test
    void readAllPositive() {
        UserDTO userDTO = new UserDTO();
        User user = new User();
        List<User> userList = Stream.of(user).collect(Collectors.toList());
        List<UserDTO> userDTOList = Stream.of(userDTO).collect(Collectors.toList());

        when(userDao.readAll(PAGE_NUMBER, RECORDS_PER_PAGE)).thenReturn(userList);
        when(modelMapper.map(user, UserDTO.class)).thenReturn(userDTO);
        assertEquals(userDTOList, userService.readAll(PAGE_NUMBER, RECORDS_PER_PAGE));
    }

    @Test
    void findByLogin() {
        UserDTO userDTO = new UserDTO();
        User user = new User();

        when(userDao.findByLogin(LOGIN)).thenReturn(Optional.of(user));
        when(modelMapper.map(user, UserDTO.class)).thenReturn(userDTO);
        assertEquals(userDTO, userService.findByLogin(LOGIN));
    }

    @Test
    void createPositive() {
        RoleDTO roleDTO = new RoleDTO();
        UserDTO userDTO = new UserDTO();
        userDTO.setLogin(LOGIN);
        userDTO.setRole(roleDTO);
        userDTO.setPassword(PASSWORD);
        User user = new User();
        user.setLogin(LOGIN);

        when(userDao.findByLogin(LOGIN)).thenReturn(Optional.empty());
        doNothing().when(registrationProvider).register(userDTO);
        when(roleService.read(RECORD_ID)).thenReturn(roleDTO);
        when(passwordEncoder.encode(PASSWORD)).thenReturn(PASSWORD);
        when(userDao.create(user)).thenReturn(Optional.of(user));
        when(modelMapper.map(userDTO, User.class)).thenReturn(user);
        when(modelMapper.map(user, UserDTO.class)).thenReturn(userDTO);

        assertEquals(userDTO, userService.create(userDTO));
    }

    @Test
    void createNegative() {
        UserDTO userDTO = new UserDTO();
        userDTO.setLogin(LOGIN);
        User user = new User();
        user.setLogin(LOGIN);
        when(userDao.findByLogin(LOGIN)).thenReturn(Optional.of(user));
        assertThrows(UserAlreadyExists.class, () -> userService.create(userDTO));
    }

    @Test
    void getEntitiesCount() {
        when(userDao.getEntitiesCount()).thenReturn(ENTITIES_COUNT);
        assertEquals(ENTITIES_COUNT, userService.getEntitiesCount());
    }
}