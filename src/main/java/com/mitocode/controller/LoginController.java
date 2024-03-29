package com.mitocode.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.mitocode.model.User;
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
    @PostMapping(value = "/sendMail")
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
            mail.setFrom("email.prueba.demo@gmail.com");
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

}
