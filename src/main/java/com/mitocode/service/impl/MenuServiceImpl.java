package com.mitocode.service.impl;

import com.mitocode.model.Menu;
import com.mitocode.repo.IGenericRepo;
import com.mitocode.repo.IMenuRepo;
import com.mitocode.security.KeyCloakConfig;
import com.mitocode.service.IMenuService;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class MenuServiceImpl extends CRUDImpl<Menu, Integer> implements IMenuService {

    @Autowired
    private IMenuRepo repo;

    @Override
    protected IGenericRepo<Menu, Integer> getRepo() {
        return repo;
    }

    @Override
    public List<Menu> getMenusByUsername(String username) {
//    public List<Menu> getMenusByUsername() {
        String testUser = SecurityContextHolder.getContext().getAuthentication().getName();
        return repo.getMenusByUsername(testUser);
    }

    //Para KeyCloak
    @Override
    public List<Menu> getMenuRoles(String username) {
        UsersResource ur = KeyCloakConfig.getInstance(username).realm(KeyCloakConfig.realm).users();
        String userId = ur.search(username, true).get(0).getId(); //sin true, busca coincidencias

        List<RoleRepresentation> roles = ur.get(userId).roles().realmLevel().listAll();
//        List<Menu> menus = new ArrayList<>();
        Set<Menu> menus = new HashSet<>();

        repo.getMenuRoles().forEach(x -> {
            roles.forEach(r -> {
                //x[2] | nombre ROL en KeyCloak
                if(String.valueOf(x[2]).equalsIgnoreCase(r.getName())) {
                    Menu m = repo.findById(Integer.parseInt(String.valueOf(x[0]))).orElse(new Menu());
                    menus.add(m);
                }
            });
        });
        return List.copyOf(menus);
    }
}
