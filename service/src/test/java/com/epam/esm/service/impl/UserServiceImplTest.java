package com.epam.esm.service.impl;

import com.epam.esm.dao.UserDao;
import com.epam.esm.domain.dto.UserDTO;
import com.epam.esm.domain.entity.User;
import com.epam.esm.service.exception.IdNotExistException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserDao userDao;
    @Mock
    private ModelMapper modelMapper;
    @InjectMocks
    private UserServiceImpl userService;

    private static final Integer VALUE_PAGE = 1;
    private static final Integer VALUE_SIZE = 3;
    private static final Long LONG_VALUE_ONE = 1L;

    @Test
    void readByIdPositive() {
        UserDTO userDTO = new UserDTO();
        User user = new User();

        when(userDao.readById(LONG_VALUE_ONE)).thenReturn(Optional.of(user));
        when(modelMapper.map(user, UserDTO.class)).thenReturn(userDTO);
        assertEquals(userDTO, userService.readById(LONG_VALUE_ONE));
    }

    @Test
    void readByIdNegative() {
        when(userDao.readById(LONG_VALUE_ONE)).thenReturn(Optional.empty());
        assertThrows(IdNotExistException.class, () -> userService.readById(LONG_VALUE_ONE));
    }

    @Test
    void readAllPositive() {
        UserDTO userDTO = new UserDTO();
        User user = new User();
        List<User> userList = Stream.of(user).collect(Collectors.toList());
        List<UserDTO> userDTOList = Stream.of(userDTO).collect(Collectors.toList());

        when(userDao.readAll(VALUE_PAGE, VALUE_SIZE)).thenReturn(userList);
        when(modelMapper.map(user, UserDTO.class)).thenReturn(userDTO);
        assertEquals(userDTOList, userService.readAll(VALUE_PAGE, VALUE_SIZE));
    }

    @Test
    void getEntitiesCount() {
        when(userDao.getEntitiesCount()).thenReturn(LONG_VALUE_ONE);
        assertEquals(LONG_VALUE_ONE, userService.getEntitiesCount());
    }
}