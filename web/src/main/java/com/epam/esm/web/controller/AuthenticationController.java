package com.epam.esm.web.controller;

import com.epam.esm.domain.dto.AuthenticationDTO;
import com.epam.esm.domain.dto.UserDTO;
import com.epam.esm.domain.validation.CreateGroup;
import com.epam.esm.service.UserService;
import com.epam.esm.web.security.JwtProvider;
import com.epam.esm.web.util.HateoasProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * The class {@code AuthenticationController} handles requests to perform authentication operations
 *
 * @author Maksim Zarutski
 */
@RestController
@RequestMapping("/api/v1.3/authentication")
public class AuthenticationController {

    private static final String KEY_LOGIN = "login";
    private static final String KEY_TOKEN = "token";

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final HateoasProvider hateoasProvider;

    public AuthenticationController(UserService userService, AuthenticationManager authenticationManager,
                                    JwtProvider jwtProvider, HateoasProvider hateoasProvider) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
        this.hateoasProvider = hateoasProvider;
    }

    /**
     * Performs user registration
     *
     * @param user parameter containing info user registration
     * @return {@code UserDTO} object for registered user
     */
    @PostMapping("/signup")
    public UserDTO register(@Validated(CreateGroup.class) @RequestBody UserDTO user) {
        UserDTO userDTO = userService.create(user);
        hateoasProvider.addLinksToUser(userDTO);
        return userDTO;
    }

    /**
     * Performs user authentication
     *
     * @param authenticationDTO parameter containing info user authentication
     * @return response with login and token generated for authenticated user
     */
    @PostMapping("/login")
    public ResponseEntity<?> auth(@Validated @RequestBody AuthenticationDTO authenticationDTO) {
        String token = getAuthenticationToken(authenticationDTO);
        return buildResponse(authenticationDTO.getLogin(), token);
    }

    private String getAuthenticationToken(AuthenticationDTO authenticationDTO) {
        String login = authenticationDTO.getLogin();
        String password = authenticationDTO.getPassword();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login, password));
        return generateUserToken(login);
    }

    private String generateUserToken(String login) {
        UserDTO user = userService.findByLogin(login);
        return jwtProvider.createToken(login, user.getRole().getName());
    }

    private ResponseEntity<?> buildResponse(String login, String token) {
        Map<Object, Object> response = new HashMap<>();
        response.put(KEY_LOGIN, login);
        response.put(KEY_TOKEN, token);
        return ResponseEntity.ok(response);
    }
}