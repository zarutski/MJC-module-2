package com.epam.esm.web.security.handler;

import com.epam.esm.web.exception.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;

@Component
public class ResponseProvider {

    private static final String APPLICATION_JSON_UTF8 = "application/json;charset=UTF-8";

    private final ReloadableResourceBundleMessageSource resourceBundle;

    public ResponseProvider(ReloadableResourceBundleMessageSource resourceBundle) {
        this.resourceBundle = resourceBundle;
    }

    public ErrorResponse getErrorResponse(String key, int code, String exceptionMessage, HttpServletRequest request) {
        Locale locale = request.getLocale();
        return new ErrorResponse(resourceBundle.getMessage(key, null, locale) + exceptionMessage, code);
    }

    public void provideResponse(HttpServletResponse response, ErrorResponse errorResponse) throws IOException {
        response.setContentType(APPLICATION_JSON_UTF8);
        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().write(mapper.writeValueAsString(errorResponse));
    }
}