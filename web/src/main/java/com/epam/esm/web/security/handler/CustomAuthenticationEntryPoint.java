package com.epam.esm.web.security.handler;

import com.epam.esm.web.exception.ErrorResponse;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Profile("basic")
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final int CODE_UNAUTHORIZED = 40101;
    private static final String UNAUTHORIZED_MESSAGE = "unauthorized";

    private final ResponseProvider responseProvider;

    public CustomAuthenticationEntryPoint(ResponseProvider responseProvider) {
        this.responseProvider = responseProvider;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        ErrorResponse errorResponse = responseProvider
                .getErrorResponse(UNAUTHORIZED_MESSAGE, CODE_UNAUTHORIZED, authException.getMessage(), request);
        responseProvider.provideResponse(response, errorResponse);
    }
}