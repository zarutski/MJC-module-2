package com.epam.esm.service.util;

import com.epam.esm.domain.dto.JwtUser;
import com.epam.esm.domain.dto.RoleDTO;
import com.epam.esm.domain.dto.UserDTO;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.HashSet;
import java.util.Set;

public final class JwtUserBuilder {

    private JwtUserBuilder() {
    }

    public static JwtUser buildFromUser(UserDTO user) {
        return new JwtUser(user.getId(), user.getLogin(), user.getPassword(), getUserAuthorities(user.getRole()));
    }

    private static Set<SimpleGrantedAuthority> getUserAuthorities(RoleDTO role) {
        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(role.getName());
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        authorities.add(simpleGrantedAuthority);
        return authorities;
    }
}