package com.epam.esm.service.impl;

import com.epam.esm.domain.dto.UserDTO;
import com.epam.esm.service.util.JwtUserBuilder;
import com.epam.esm.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("userDetailsServiceImpl")
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;

    public UserDetailsServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        UserDTO user = userService.findByLogin(login);
        return JwtUserBuilder.buildFromUser(user);
    }
}