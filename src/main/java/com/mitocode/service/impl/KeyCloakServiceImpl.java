package com.mitocode.service.impl;

import java.util.Arrays;
import java.util.List;

import com.mitocode.service.IKeyCloakService;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import com.mitocode.security.KeyCloakConfig;
import com.mitocode.model.User;

@Service
public class KeyCloakServiceImpl implements IKeyCloakService {

    @Override
    public boolean addUser(User user) throws Exception{
        boolean rpta;
        //USUARIO CON PRIVILEGIOS de manejo total de realm para poder agregar, debe ser un ADMIN, algun usuario fijo //usuario.getUsername() falla, porque ese usuario no existe
        RealmResource realmResource = KeyCloakConfig.getInstance("magdiellinares@gmail.com").realm(KeyCloakConfig.realm);
        UsersResource usersResource = realmResource.users();

        List<UserRepresentation> lista = usersResource.search(user.getUsername(), true);
        rpta = lista.isEmpty();

        if (rpta) {
            //Si lista vacia, significa que usuario no existe, entonces crearlo
            UserRepresentation ur = new UserRepresentation();
            ur.setUsername(user.getUsername());
            ur.setCredentials(Arrays.asList(generatePassword(user.getPassword())));
            //ur.setFirstName("");
            //ur.setLastName("");
            //ur.setRealmRoles(Arrays.asList("USER"));//NO FUNCIONA
            ur.setEmail(user.getUsername());
            ur.setEnabled(true);
            ur.setEmailVerified(true);
            usersResource.create(ur);

            //Agregar un rol por defecto para que funcione las opciones de menu
            //Se busca nuevamente al usuario creado para obtener su ID
            lista = usersResource.search(user.getUsername(), true);
            String userId = lista.get(0).getId();
            RoleRepresentation rr = realmResource.roles().get("USER").toRepresentation();
            usersResource.get(userId).roles().realmLevel().add(Arrays.asList(rr));
        }

        return rpta;
    }

    private CredentialRepresentation generatePassword(String password) {
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);
        return credential;
    }

}
