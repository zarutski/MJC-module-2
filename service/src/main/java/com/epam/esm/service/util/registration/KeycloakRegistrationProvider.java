package com.epam.esm.service.util.registration;

import com.epam.esm.domain.dto.UserDTO;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@Profile("dev")
public class KeycloakRegistrationProvider implements RegistrationProvider {

    private static final String REALM_NAME = "Demo-Realm";
    private static final String REALM_ROLE_USER = "app-user";

    private final RealmResource realmResource;
    private final UsersResource usersResource;
    private final List<RoleRepresentation> rolesToAdd;

    public KeycloakRegistrationProvider(Keycloak keycloak) {
        this.realmResource = keycloak.realm(REALM_NAME);
        this.usersResource = realmResource.users();
        this.rolesToAdd = rolesToAdd();
    }

    @Override
    public void register(UserDTO user) {
        String userLogin = user.getLogin();
        UserRepresentation userRealm = findByUsername(userLogin);
        if (userRealm == null) {
            UserRepresentation userRepresentation = createUserRepresentation(user);
            usersResource.create(userRepresentation);
            addRealmRoleToUser(userLogin);
        }
    }

    private UserRepresentation findByUsername(String username) {
        List<UserRepresentation> search = usersResource.search(username);
        return search.stream().findFirst().orElse(null);
    }

    private UserRepresentation createUserRepresentation(UserDTO userDTO) {
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername(userDTO.getLogin());
        userRepresentation.setCredentials(Collections.singletonList(createPasswordCredentials(userDTO.getPassword())));
        userRepresentation.setEnabled(true);
        return userRepresentation;
    }

    private CredentialRepresentation createPasswordCredentials(String password) {
        CredentialRepresentation passwordCredentials = new CredentialRepresentation();
        passwordCredentials.setTemporary(false);
        passwordCredentials.setType(CredentialRepresentation.PASSWORD);
        passwordCredentials.setValue(password);
        return passwordCredentials;
    }

    private void addRealmRoleToUser(String userName) {
        UserRepresentation userRealm = findByUsername(userName);
        UserResource user = usersResource.get(userRealm.getId());
        user.roles().realmLevel().add(rolesToAdd);
    }

    private List<RoleRepresentation> rolesToAdd() {
        RoleRepresentation roleRepresentation = realmResource.roles().get(REALM_ROLE_USER).toRepresentation();
        List<RoleRepresentation> roles = new ArrayList<>();
        roles.add(roleRepresentation);
        return roles;
    }

}