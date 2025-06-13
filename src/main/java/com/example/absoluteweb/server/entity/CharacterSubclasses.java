package com.example.absoluteweb.server.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "character_subclasses")
@IdClass(CharacterSubclassesId.class)
public class CharacterSubclasses {

    @Id
    @Column(name = "char_obj_id", nullable = false)
    private int charObjId;

    @Id
    @Column(name = "class_id", nullable = false)
    private int classId;

    @Column(name = "level", nullable = false)
    private int level; // tinyint unsigned (default 1)

    @Column(name = "exp", nullable = false)
    private long exp; // bigint unsigned (default 0)

    @Column(name = "sp", nullable = false)
    private long sp; // bigint unsigned (default 0)

    @Column(name = "curHp", nullable = false, precision = 11, scale = 4)
    private BigDecimal curHp; // decimal(11,4) unsigned (default 0.0000)

    @Column(name = "curMp", nullable = false, precision = 11, scale = 4)
    private BigDecimal curMp; // decimal(11,4) unsigned (default 0.0000)

    @Column(name = "curCp", nullable = false, precision = 11, scale = 4)
    private BigDecimal curCp; // decimal(11,4) unsigned (default 0.0000)

    @Column(name = "maxHp", nullable = false)
    private int maxHp; // mediumint unsigned (default 0)

    @Column(name = "maxMp", nullable = false)
    private int maxMp; // mediumint unsigned (default 0)

    @Column(name = "maxCp", nullable = false)
    private int maxCp; // mediumint unsigned (default 0)

    // Використовуємо boolean для tinyint(1). Значення true відповідає 1, false – 0.
    @Column(name = "active", nullable = false)
    private boolean active;

    @Column(name = "isBase", nullable = false)
    private boolean isBase;

    @Column(name = "death_penalty", nullable = false)
    private int deathPenalty; // tinyint (default 0)

    // Конструктор за замовчуванням
    public CharacterSubclasses() {
    }

    // Геттери та сеттери

    public int getCharObjId() {
        return charObjId;
    }

    public void setCharObjId(int charObjId) {
        this.charObjId = charObjId;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public long getExp() {
        return exp;
    }

    public void setExp(long exp) {
        this.exp = exp;
    }

    public long getSp() {
        return sp;
    }

    public void setSp(long sp) {
        this.sp = sp;
    }

    public BigDecimal getCurHp() {
        return curHp;
    }

    public void setCurHp(BigDecimal curHp) {
        this.curHp = curHp;
    }

    public BigDecimal getCurMp() {
        return curMp;
    }

    public void setCurMp(BigDecimal curMp) {
        this.curMp = curMp;
    }

    public BigDecimal getCurCp() {
        return curCp;
    }

    public void setCurCp(BigDecimal curCp) {
        this.curCp = curCp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }

    public int getMaxMp() {
        return maxMp;
    }

    public void setMaxMp(int maxMp) {
        this.maxMp = maxMp;
    }

    public int getMaxCp() {
        return maxCp;
    }

    public void setMaxCp(int maxCp) {
        this.maxCp = maxCp;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isBase() {
        return isBase;
    }

    public void setBase(boolean base) {
        isBase = base;
    }

    public int getDeathPenalty() {
        return deathPenalty;
    }

    public void setDeathPenalty(int deathPenalty) {
        this.deathPenalty = deathPenalty;
    }
}
