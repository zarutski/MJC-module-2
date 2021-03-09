package com.epam.esm.web.security.verifier;

import com.epam.esm.domain.dto.JwtUser;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import static com.epam.esm.web.util.SecurityValue.ROLE_USER;

@Component
@Profile("basic")
public class BasicPermissionVerifier implements PermissionVerifier {

    @Override
    public void verifyPermission(Long id) {
        JwtUser user = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user.getAuthorities().stream().map(String::valueOf).anyMatch(role -> role.equals(ROLE_USER))) {
            verifyIdMatch(user, id);
        }
    }
}