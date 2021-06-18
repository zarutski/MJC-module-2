package com.epam.esm.web.security.handler;

import com.epam.esm.web.exception.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.epam.esm.web.util.SecurityValue.MESSAGE_EMPTY;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private static final int CODE_ACCESS_DENIED = 40302;
    private static final String ACCESS_DENIED = "access_denied";

    private final ResponseProvider responseProvider;

    public CustomAccessDeniedHandler(ResponseProvider responseProvider) {
        this.responseProvider = responseProvider;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        ErrorResponse errorResponse = responseProvider
                .getErrorResponse(ACCESS_DENIED, CODE_ACCESS_DENIED, MESSAGE_EMPTY, request);
        responseProvider.provideResponse(response, errorResponse);
    }
}