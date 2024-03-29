package com.epam.esm.web.util;

public final class SecurityValue {

    private SecurityValue() {
    }

    public static final String ROLE_USER = "ROLE_USER";
    public static final String ADMIN = "ADMIN";
    public static final String USER = "USER";
    public static final String KEYCLOAK_AUTH_SERVER_URL = "http://localhost:8080/auth";
    public static final String KEYCLOAK_MASTER_REALM = "master";
    public static final String KEYCLOAK_MASTER_USERNAME = "admin";
    public static final String KEYCLOAK_MASTER_PASSWORD = "9379992";
    public static final String KEYCLOAK_MASTER_CLIENT = "admin-cli";
    public static final String ENDPOINT_USERS = "/api/v1.3/users/**";
    public static final String ENDPOINT_ORDERS = "/api/v1.3/orders/**";

}