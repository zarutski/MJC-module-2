package com.epam.esm.web.security.handler;

import com.google.common.net.HttpHeaders;
import org.keycloak.adapters.AdapterDeploymentContext;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationEntryPoint;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class KeycloakEntryPoint extends KeycloakAuthenticationEntryPoint {

    private static final String WWW_AUTHENTICATE_HEADER = "Bearer realm=\"Demo-realm\"";

    public KeycloakEntryPoint(AdapterDeploymentContext adapterDeploymentContext) {
        super(adapterDeploymentContext);
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.addHeader(HttpHeaders.WWW_AUTHENTICATE, WWW_AUTHENTICATE_HEADER);
    }
}