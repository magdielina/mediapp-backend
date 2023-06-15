package com.mitocode.controller;

import java.util.*;

import com.mitocode.model.User;
import com.mitocode.security.KeyCloakConfig;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mitocode.model.ResetToken;
import com.mitocode.service.ILoginService;
import com.mitocode.service.IResetTokenService;
import com.mitocode.util.EmailUtil;
import com.mitocode.util.Mail;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private ILoginService service;

    @Autowired
    private IResetTokenService tokenService;

    @Autowired
    private EmailUtil emailUtil;

    //[Example username: mitotest21@gmail.com]
    /*@PostMapping(value = "/sendMail")
    public ResponseEntity<Integer> sendMail(@RequestBody String username) throws Exception {
        int rpta = 0;

        User us = service.checkUsername(username);
        if(us != null && us.getUserId() > 0) {
            ResetToken token = new ResetToken();
            token.setToken(UUID.randomUUID().toString());
            token.setUser(us);
            token.setExpiration(10);
            tokenService.save(token);

            Mail mail = new Mail();
            mail.setFrom("medi.app.service@gmail.com");
            mail.setTo(us.getUsername());
            mail.setSubject("RESTABLECER CONTRASEÑA - MEDIAPP");

            Map<String, Object> model = new HashMap<>();
            String url = "http://localhost:4200/forgot/" + token.getToken();
            model.put("user", token.getUser().getUsername());
            model.put("resetUrl", url);
            mail.setModel(model);

            emailUtil.sendMail(mail);

            rpta = 1;
        }
        return new ResponseEntity<>(rpta, HttpStatus.OK);
    }*/

    //KeyCloak
    @PostMapping(value = "/sendMail", consumes = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<Integer> sendMailKeycloak(@RequestBody String username) throws Exception {
        UsersResource usersResource = KeyCloakConfig.getInstance(username).realm(KeyCloakConfig.realm).users();
        List<UserRepresentation> lista = usersResource.search(username, true);
        boolean rpta = lista.isEmpty();

        if (!rpta) {
            //Si lista no vacia, significa que usuario existe, entonces enviar correo
            UserRepresentation user = lista.get(0);
            usersResource.get(user.getId()).executeActionsEmail(Arrays.asList("UPDATE_PASSWORD")); //.resetPasswordEmail();
            return new ResponseEntity<>(1, HttpStatus.OK);
        }
        return new ResponseEntity<>(0, HttpStatus.OK);
    }

    @GetMapping(value = "/reset/check/{token}")
    public ResponseEntity<Integer> checkToken(@PathVariable("token") String token) {
        int rpta = 0;
        try {
            if (token != null && !token.isEmpty()) {
                ResetToken rt = tokenService.findByToken(token);
                if (rt != null && rt.getId() > 0) {
                    if (!rt.isExpired()) {
                        rpta = 1;
                    }
                }
            }
        } catch (Exception e) {
            return new ResponseEntity<>(rpta, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(rpta, HttpStatus.OK);
    }

    @PostMapping(value = "/reset/{token}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> resetPassword(@PathVariable("token") String token, @RequestBody String password) {
        try {
            ResetToken rt = tokenService.findByToken(token);
            service.changePassword(password, rt.getUser().getUsername());
            tokenService.delete(rt);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // KeyCloak
    @PostMapping("/logout")
    public void logout(@RequestBody String username) {
        UsersResource usersResource = KeyCloakConfig.getInstance(username).realm(KeyCloakConfig.realm).users();
        UserRepresentation user = usersResource.search(username, true).get(0);
        usersResource.get(user.getId()).logout();

        //Cerrar sesion al iniciar y luego poder iniciar, con eso limito a 1 sesion activa, es decir mato a todos para permitir al nuevo
        //RealmResource realmResource = KeyCloakConfig.getInstance("").realm(KeyCloakConfig.realm).clients().get("mediapp-backend").getUserSessions(firstResult, maxResults)
    }

}
