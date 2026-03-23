package com.example.absoluteweb.server.services;

import com.example.absoluteweb.server.DTO.CharacterDTO;
import com.example.absoluteweb.server.DTO.TopClanDTO;
import com.example.absoluteweb.server.DTO.TopPkPlayerDTO;
import com.example.absoluteweb.server.DTO.TopPvpPlayerDTO;
import com.example.absoluteweb.server.entity.Accounts;
import com.example.absoluteweb.server.entity.GameCharacter;
import com.example.absoluteweb.server.enums.ClassLocalization;
import com.example.absoluteweb.server.repository.CharacterSubclassesRep;
import com.example.absoluteweb.server.repository.ClanRepository;
import com.example.absoluteweb.server.repository.GameCharacterRep;
import com.example.absoluteweb.server.repository.ServerAccountsRep;
import com.example.absoluteweb.site.DTO.SiteRegistrationRequest;
import com.example.absoluteweb.site.exceptions.AccountExceptions;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.Security;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
public class ServerAccountsService {

    private final ServerAccountsRep serverAccountsRep;
    private final GameCharacterRep gameCharacterRep;
    private final CharacterSubclassesRep characterSubclassesRep;

    private final PasswordEncoder passwordEncoder;
    private final ClanRepository clanRepository;

    @Autowired
    public ServerAccountsService(ServerAccountsRep serverAccountsRep, GameCharacterRep gameCharacterRep,
                                 PasswordEncoder passwordEncoder, CharacterSubclassesRep characterSubclassesRep,
                                 ClanRepository clanRepository) {
        this.serverAccountsRep = serverAccountsRep;
        this.gameCharacterRep = gameCharacterRep;
        this.passwordEncoder = passwordEncoder;
        this.characterSubclassesRep = characterSubclassesRep;
        this.clanRepository = clanRepository;
    }


    public ResponseEntity createAccaunt(SiteRegistrationRequest regAcc) throws AccountExceptions {
            Accounts acc = new Accounts();
            acc.setL2email(regAcc.getL2email());
            acc.setLogin(regAcc.getLogin());
            if(regAcc.getLogin().length()>14 || regAcc.getPassword().length()>14 ) {
                throw new AccountExceptions("Логин и пароль не могут содержать более 14 символов");
            }
        try {
            if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
                Security.addProvider(new BouncyCastleProvider());
            }
            MessageDigest md = MessageDigest.getInstance("WHIRLPOOL", "BC");
            byte[] hashBytes = md.digest(regAcc.getPassword().getBytes(StandardCharsets.UTF_8));
            String encodedPassword = Base64.getEncoder().encodeToString(hashBytes);
            acc.setPassword(encodedPassword);
            } catch (Exception ex) {
                throw new AccountExceptions("Ошибка шифровки пароля");
            }
            acc.setBan_expire(0);
            acc.setAllow_ip("");
            if(serverAccountsRep.findByLogin(regAcc.getLogin()) != null || serverAccountsRep.findByL2email(regAcc.getL2email()) != null) {
                throw new AccountExceptions("Логин или Емеил уже зарегистрированы на игровом сервере");
            }

            try{
                serverAccountsRep.save(acc);
                return ResponseEntity.ok("Ваш акаунт зарегистрирован на сервере");
            }catch (Exception ex) {
                throw new AccountExceptions("Ошибка сохранения на игровом сервере.");
            }
    }
    public ResponseEntity<List<CharacterDTO>> getCharacters(String accountName) {
        List<GameCharacter> characters = gameCharacterRep.findByAccountName(accountName);
        if (characters.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        List<CharacterDTO> characterDTOs = new ArrayList<>();
        for (GameCharacter character : characters) {
            // Отримуємо локалізовані дані за baseClassId з enum'а
            ClassLocalization localization = ClassLocalization.getByBaseClassId(character.getBaseClassId());
            String raceEn = localization != null ? localization.getRaceEn() : "Unknown";
            String raceRu = localization != null ? localization.getRaceRu() : "Неизвестно";
            String raceUk = localization != null ? localization.getRaceUk() : "Невідомо";

            String classEn = localization != null ? localization.getClassEn() : "Unknown";
            String classRu = localization != null ? localization.getClassRu() : "Неизвестно";
            String classUk = localization != null ? localization.getClassUk() : "Невідомо";


            int level = characterSubclassesRep.findByCharObjId(character.getObjId())
                    .get(0)
                    .getLevel();
            // Формуємо DTO з необхідними даними
            CharacterDTO dto = new CharacterDTO(
                    character.getObjId(),
                    character.getAccountName(),
                    character.getCharName(),
                    character.getBaseClassId(),
                    raceEn,
                    raceRu,
                    raceUk,
                    classEn,
                    classRu,
                    classUk,

                    level
            );
            characterDTOs.add(dto);
        }
        return ResponseEntity.ok(characterDTOs);
    }

    public void changePasswordInternal(String login, String newPassword) throws AccountExceptions {
        Accounts acc = serverAccountsRep.findByLogin(login);
        if (acc == null) {
            throw new AccountExceptions("Game account not found: " + login);
        }
        try {
            if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
                Security.addProvider(new BouncyCastleProvider());
            }
            MessageDigest md = MessageDigest.getInstance("WHIRLPOOL", "BC");
            byte[] hashBytes = md.digest(newPassword.getBytes(StandardCharsets.UTF_8));
            String encodedPassword = Base64.getEncoder().encodeToString(hashBytes);

            acc.setPassword(encodedPassword);
            serverAccountsRep.save(acc);
        } catch (Exception ex) {
            throw new AccountExceptions("Game password update error");
        }
    }

    public int getOnline(){
        return gameCharacterRep.countByOnline(1);
    }

    public ResponseEntity<List<TopPvpPlayerDTO>> getTopPvpPlayers() {
        List<GameCharacter> characters = gameCharacterRep.findTop8ByOrderByPvpKillsDesc();

        List<TopPvpPlayerDTO> dtos = new ArrayList<>();
        for (GameCharacter character : characters) {
            dtos.add(new TopPvpPlayerDTO(
                    character.getCharName(),
                    character.getPvpKills()
            ));
        }

        return ResponseEntity.ok(dtos);
    }


    public ResponseEntity<List<TopPkPlayerDTO>> getTopPkPlayers() {
        List<GameCharacter> characters = gameCharacterRep.findTop8ByOrderByPkKillsDesc();

        List<TopPkPlayerDTO> dtos = new ArrayList<>();
        for (GameCharacter character : characters) {
            dtos.add(new TopPkPlayerDTO(
                    character.getCharName(),
                    character.getPkKills()
            ));
        }

        return ResponseEntity.ok(dtos);
    }

    public ResponseEntity<List<TopClanDTO>> getTopClans() {
        List<Object[]> rows = clanRepository.findTop10ClansRaw();

        List<TopClanDTO> dtos = new ArrayList<>();
        for (Object[] row : rows) {
            String clanName = (String) row[0];
            Integer reputation = (Integer) row[1];
            dtos.add(new TopClanDTO(clanName, reputation));
        }

        return ResponseEntity.ok(dtos);
    }

}
