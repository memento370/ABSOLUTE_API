package com.example.absoluteweb.server.controllers;

import com.example.absoluteweb.server.DTO.CharacterDTO;
import com.example.absoluteweb.server.entity.GameCharacter;
import com.example.absoluteweb.server.services.ServerAccountsService;
import com.example.absoluteweb.site.DTO.SiteRegistrationRequest;
import com.example.absoluteweb.site.exceptions.AccountExceptions;
import com.example.absoluteweb.site.principals.AccountPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @PostMapping("/characters")
    public ResponseEntity<List<CharacterDTO>> getCharacters(@AuthenticationPrincipal AccountPrincipal account) {
        return accountsService.getCharacters(account.getUsername());
    }

    @GetMapping("/getOnline")
    public int getOnline(){
        return accountsService.getOnline();
    }

}
