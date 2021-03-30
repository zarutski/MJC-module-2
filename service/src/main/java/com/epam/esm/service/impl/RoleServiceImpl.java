package com.epam.esm.service.impl;

import com.epam.esm.dao.RoleDao;
import com.epam.esm.domain.dto.RoleDTO;
import com.epam.esm.service.RoleService;
import com.epam.esm.service.exception.IdNotExistException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleDao roleDao;
    private final ModelMapper modelMapper;

    public RoleServiceImpl(RoleDao roleDao, ModelMapper modelMapper) {
        this.roleDao = roleDao;
        this.modelMapper = modelMapper;
    }

    @Override
    public RoleDTO read(Long id) {
        return modelMapper.map(roleDao.readById(id)
                .orElseThrow(() -> new IdNotExistException(id.toString())), RoleDTO.class);
    }

}
