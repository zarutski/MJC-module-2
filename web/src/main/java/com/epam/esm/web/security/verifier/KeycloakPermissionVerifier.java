package com.epam.esm.web.security.verifier;

import com.epam.esm.domain.dto.JwtUser;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
public class KeycloakPermissionVerifier implements PermissionVerifier {

    private static final String KEYCLOAK_ROLE_USER = "app-user";

    private final UserDetailsService userDetailsService;

    public KeycloakPermissionVerifier(@Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public void verifyPermission(Long id) {
        KeycloakSecurityContext keycloakContext = getKeycloakContext();
        if (keycloakContext.getToken().getRealmAccess().isUserInRole(KEYCLOAK_ROLE_USER)) {
            JwtUser user = getJwtUserFromContext(keycloakContext);
            verifyIdMatch(user, id);
        }
    }

    private KeycloakSecurityContext getKeycloakContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        KeycloakPrincipal<?> principal = (KeycloakPrincipal<?>) authentication.getPrincipal();
        return principal.getKeycloakSecurityContext();
    }

    private JwtUser getJwtUserFromContext(KeycloakSecurityContext context) {
        String username = context.getToken().getPreferredUsername();
        return (JwtUser) userDetailsService.loadUserByUsername(username);
    }
}