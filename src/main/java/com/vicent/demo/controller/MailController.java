package com.vicent.demo.controller;

import com.vicent.demo.config.MailConfig;
import com.vicent.demo.entity.SendMailRequest;
import com.vicent.demo.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController(value = "/mail")
public class MailController {

    @Autowired
    private MailService mailService;

    @PostMapping
    public ResponseEntity<Void> sendMail(@Valid @RequestBody SendMailRequest request){
        mailService.sendMail(request);
        return ResponseEntity.noContent().build();
    }
}
