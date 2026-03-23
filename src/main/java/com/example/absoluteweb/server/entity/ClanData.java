package com.example.absoluteweb.server.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "clan_data")
public class ClanData {

    @Id
    @Column(name = "clan_id")
    private Integer clanId;

    @Column(name = "reputation_score")
    private Integer reputationScore;

    public Integer getClanId() { return clanId; }
    public void setClanId(Integer clanId) { this.clanId = clanId; }

    public Integer getReputationScore() { return reputationScore; }
    public void setReputationScore(Integer reputationScore) { this.reputationScore = reputationScore; }
}