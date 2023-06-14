package com.mitocode.controller;

import com.mitocode.model.User;
import com.mitocode.service.impl.KeyCloakServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tokens")
public class TokenController {

//    @Autowired
//    private ConsumerTokenServices tokenServices;

    @Autowired
    private KeyCloakServiceImpl keycloakService;

//    @GetMapping("/anulate/{tokenId:.*}")
//    public void revokeToken(@PathVariable("tokenId") String token) {
//        tokenServices.revokeToken(token);
//    }

    // KeyCloak
    @PostMapping(value = "/user/add")
    public ResponseEntity<Boolean> createUser(@RequestBody User user) throws Exception {
        boolean rpta = keycloakService.addUser(user);
        return new ResponseEntity<>(rpta, HttpStatus.OK);
    }
}
