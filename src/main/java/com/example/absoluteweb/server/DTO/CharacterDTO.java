package com.example.absoluteweb.server.DTO;

import jakarta.persistence.Column;
import jakarta.persistence.Id;

public class CharacterDTO {

    private int objId;
    private String accountName;
    private String charName;
    private int baseClassId;
    private String raceEn;
    private String raceRu;
    private String raceUk;
    private String classEn;
    private String classRu;
    private String classUk;
    private int lvl;

    public CharacterDTO(int objId, String accountName,String charName, int baseClassId, String raceEn, String raceRu,String raceUk, String classEn, String classRu,String classUk,int lvl) {
        this.objId = objId;
        this.accountName = accountName;
        this.charName = charName;
        this.baseClassId = baseClassId;
        this.raceEn = raceEn;
        this.raceRu = raceRu;
        this.raceUk = raceUk;
        this.classEn = classEn;
        this.classRu = classRu;
        this.classUk = classUk;
        this.lvl = lvl;
    }

    public int getObjId() {
        return objId;
    }

    public void setObjId(int objId) {
        this.objId = objId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public int getBaseClassId() {
        return baseClassId;
    }

    public void setBaseClassId(int baseClassId) {
        this.baseClassId = baseClassId;
    }

    public String getRaceEn() {
        return raceEn;
    }

    public void setRaceEn(String raceEn) {
        this.raceEn = raceEn;
    }

    public String getRaceRu() {
        return raceRu;
    }

    public void setRaceRu(String raceRu) {
        this.raceRu = raceRu;
    }

    public String getClassEn() {
        return classEn;
    }

    public void setClassEn(String classEn) {
        this.classEn = classEn;
    }

    public String getCharName() {
        return charName;
    }

    public void setCharName(String charName) {
        this.charName = charName;
    }

    public String getClassRu() {
        return classRu;
    }

    public void setClassRu(String classRu) {
        this.classRu = classRu;
    }
    public int getLvl() {
        return lvl;
    }

    public void setLvl(int lvl) {
        this.lvl = lvl;
    }

    public String getRaceUk() {
        return raceUk;
    }

    public void setRaceUk(String raceUk) {
        this.raceUk = raceUk;
    }

    public String getClassUk() {
        return classUk;
    }

    public void setClassUk(String classUk) {
        this.classUk = classUk;
    }
}
