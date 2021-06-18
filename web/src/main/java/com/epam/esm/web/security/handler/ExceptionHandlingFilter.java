package com.epam.esm.web.security.handler;

import com.epam.esm.web.exception.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class ExceptionHandlingFilter extends OncePerRequestFilter {

    private static final int CODE_AUTHENTICATION_ERROR = 40102;
    private static final String AUTHENTICATION_ERROR = "authentication_error";

    private final ResponseProvider responseProvider;

    public ExceptionHandlingFilter(ResponseProvider responseProvider) {
        this.responseProvider = responseProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (RuntimeException e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            ErrorResponse errorResponse = responseProvider
                    .getErrorResponse(AUTHENTICATION_ERROR, CODE_AUTHENTICATION_ERROR, e.toString(), request);
            responseProvider.provideResponse(response, errorResponse);
        }
    }
}