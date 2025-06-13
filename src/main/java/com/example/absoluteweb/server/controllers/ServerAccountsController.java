package com.example.absoluteweb.server.controllers;

import com.example.absoluteweb.server.DTO.CharacterDTO;
import com.example.absoluteweb.server.entity.GameCharacter;
import com.example.absoluteweb.server.services.ServerAccountsService;
import com.example.absoluteweb.site.DTO.SiteRegistrationRequest;
import com.example.absoluteweb.site.exceptions.AccountExceptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/server/accounts")
public class ServerAccountsController {

    @Autowired
    private ServerAccountsService accountsService;

    @Autowired
    public ServerAccountsController(ServerAccountsService accountsService) {
        this.accountsService = accountsService;
    }

    @PostMapping("/register")
    private ResponseEntity createAccount(@RequestBody SiteRegistrationRequest regAcc) {
        try {
            accountsService.createAccaunt(regAcc);
            return ResponseEntity.ok("Акаунт зарегистрирован на сервере");
        }
        catch (AccountExceptions e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/characters")
    public ResponseEntity<List<CharacterDTO>> getCharacters(@RequestBody String accountName) {
        return accountsService.getCharacters(accountName);
    }




}
