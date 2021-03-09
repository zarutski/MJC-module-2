package com.epam.esm.web.security.verifier;

import com.epam.esm.domain.dto.JwtUser;
import org.springframework.security.access.AccessDeniedException;

public interface PermissionVerifier {

    String ACCESS_DENIED = "access denied";

    void verifyPermission(Long id);

    default void verifyIdMatch(JwtUser user, Long id){
        if (!user.getId().equals(id)) {
            throw new AccessDeniedException(ACCESS_DENIED);
        }
    }
}