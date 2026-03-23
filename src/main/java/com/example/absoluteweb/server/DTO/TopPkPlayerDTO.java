package com.example.absoluteweb.server.DTO;

public class TopPkPlayerDTO {

    private final String characterName;
    private final Integer pkKills;

    public TopPkPlayerDTO(String characterName, Integer pkKills) {
        this.characterName = characterName;
        this.pkKills = pkKills;
    }

    public String getCharacterName() {
        return characterName;
    }

    public Integer getPkKills() {
        return pkKills;
    }
}