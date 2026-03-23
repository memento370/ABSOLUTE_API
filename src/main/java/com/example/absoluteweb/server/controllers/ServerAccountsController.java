package com.example.absoluteweb.server.controllers;

import com.example.absoluteweb.server.DTO.CharacterDTO;
import com.example.absoluteweb.server.DTO.TopClanDTO;
import com.example.absoluteweb.server.DTO.TopPkPlayerDTO;
import com.example.absoluteweb.server.DTO.TopPvpPlayerDTO;
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

    @GetMapping("/top/pvp")
    public ResponseEntity<List<TopPvpPlayerDTO>> getTopPvpPlayers() {
        return accountsService.getTopPvpPlayers();
    }

    @GetMapping("/top/pk")
    public ResponseEntity<List<TopPkPlayerDTO>> getTopPkPlayers() {
        return accountsService.getTopPkPlayers();
    }

    @GetMapping("/top/clans")
    public ResponseEntity<List<TopClanDTO>> getTopClans() {
        return accountsService.getTopClans();
    }
}
