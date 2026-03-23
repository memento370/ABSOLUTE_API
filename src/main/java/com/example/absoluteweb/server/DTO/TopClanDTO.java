package com.example.absoluteweb.server.DTO;

public class TopClanDTO {
    private final String clanName;
    private final Integer reputation;

    public TopClanDTO(String clanName, Integer reputation) {
        this.clanName = clanName;
        this.reputation = reputation;
    }

    public String getClanName() {
        return clanName;
    }

    public Integer getReputation() {
        return reputation;
    }
}
