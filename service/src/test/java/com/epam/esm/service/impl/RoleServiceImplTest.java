package com.epam.esm.service.impl;

import com.epam.esm.dao.RoleDao;
import com.epam.esm.domain.dto.RoleDTO;
import com.epam.esm.domain.entity.Role;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class RoleServiceImplTest {

    @Mock
    private RoleDao roleDao;
    @Mock
    private ModelMapper modelMapper;
    @InjectMocks
    private RoleServiceImpl roleService;

    private static final Long RECORD_ID = 1L;

    @Test
    void read(){
        RoleDTO roleDTO = new RoleDTO();
        Role role = new Role();

        when(modelMapper.map(role, RoleDTO.class)).thenReturn(roleDTO);
        when(roleDao.readById(RECORD_ID)).thenReturn(Optional.of(role));
        assertEquals(roleDTO, roleService.read(RECORD_ID));
    }
}