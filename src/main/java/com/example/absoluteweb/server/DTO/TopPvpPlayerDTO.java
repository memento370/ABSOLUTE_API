package com.example.absoluteweb.server.DTO;

public class TopPvpPlayerDTO {

    private final String characterName;
    private final Integer pvpKills;

    public TopPvpPlayerDTO(String characterName, Integer pvpKills) {
        this.characterName = characterName;
        this.pvpKills = pvpKills;
    }

    public String getCharacterName() {
        return characterName;
    }

    public Integer getPvpKills() {
        return pvpKills;
    }
}