package com.example.absoluteweb.server.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "characters")
public class GameCharacter {

    // Конструктор за замовчуванням
    public GameCharacter() {
    }

    @Id
    @Column(name = "obj_Id", nullable = false)
    private int objId;

    @Column(name = "account_name", nullable = false, length = 45)
    private String accountName;

    @Column(name = "char_name", nullable = false, length = 35, unique = true)
    private String charName;

    @Column(name = "face")
    private Integer face;

    @Column(name = "hairStyle")
    private Integer hairStyle;

    @Column(name = "hairColor")
    private Integer hairColor;

    @Column(name = "sex")
    private Integer sex;

    @Column(name = "heading")
    private Integer heading;

    @Column(name = "x")
    private Integer x;

    @Column(name = "y")
    private Integer y;

    @Column(name = "z")
    private Integer z;

    @Column(name = "karma")
    private Integer karma;

    @Column(name = "pvpkills")
    private Integer pvpKills;

    @Column(name = "pkkills")
    private Integer pkKills;

    @Column(name = "clanid")
    private Integer clanId;

    @Column(name = "createtime", nullable = false)
    private long createTime;

    @Column(name = "deletetime", nullable = false)
    private long deleteTime;

    @Column(name = "title", length = 16)
    private String title;

    @Column(name = "accesslevel")
    private Integer accessLevel;

    @Column(name = "online")
    private Integer online;

    @Column(name = "onlinetime", nullable = false)
    private long onlineTime;

    @Column(name = "lastAccess", nullable = false)
    private long lastAccess;

    @Column(name = "leaveclan", nullable = false)
    private long leaveClan;

    @Column(name = "deleteclan", nullable = false)
    private long deleteClan;

    @Column(name = "nochannel", nullable = false)
    private int noChannel;

    @Column(name = "pledge_type", nullable = false)
    private short pledgeType;

    @Column(name = "pledge_rank", nullable = false)
    private Integer pledgeRank;

    @Column(name = "lvl_joined_academy", nullable = false)
    private Integer lvlJoinedAcademy;

    @Column(name = "apprentice", nullable = false)
    private long apprentice;

    @Lob
    @Column(name = "key_bindings")
    private byte[] keyBindings;

    @Column(name = "pcBangPoints", nullable = false)
    private Integer pcBangPoints;

    @Column(name = "raidBossPoints", nullable = false)
    private Integer raidBossPoints;

    @Column(name = "vitality", nullable = false)
    private Integer vitality;

    @Column(name = "fame", nullable = false)
    private Integer fame;

    @Column(name = "base_class_id", nullable = false)
    private Integer baseClassId;

    @Column(name = "bookmarks", nullable = false)
    private Integer bookmarks;

    @Column(name = "rec_have", nullable = false)
    private Integer recHave;

    @Column(name = "rec_left", nullable = false)
    private Integer recLeft;

    @Column(name = "rec_bonus_time", nullable = false)
    private short recBonusTime;

    @Column(name = "hunting_bonus_time", nullable = false)
    private short huntingBonusTime;

    @Column(name = "hunting_bonus", nullable = false)
    private short huntingBonus;

    @Column(name = "rec_tick_cnt")
    private Integer recTickCnt;

    @Column(name = "last_teleport", nullable = false)
    private int lastTeleport;

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

    public String getCharName() {
        return charName;
    }

    public void setCharName(String charName) {
        this.charName = charName;
    }

    public Integer getFace() {
        return face;
    }

    public void setFace(Integer face) {
        this.face = face;
    }

    public Integer getHairStyle() {
        return hairStyle;
    }

    public void setHairStyle(Integer hairStyle) {
        this.hairStyle = hairStyle;
    }

    public Integer getHairColor() {
        return hairColor;
    }

    public void setHairColor(Integer hairColor) {
        this.hairColor = hairColor;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Integer getHeading() {
        return heading;
    }

    public void setHeading(Integer heading) {
        this.heading = heading;
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public Integer getZ() {
        return z;
    }

    public void setZ(Integer z) {
        this.z = z;
    }

    public Integer getKarma() {
        return karma;
    }

    public void setKarma(Integer karma) {
        this.karma = karma;
    }

    public Integer getPvpKills() {
        return pvpKills;
    }

    public void setPvpKills(Integer pvpKills) {
        this.pvpKills = pvpKills;
    }

    public Integer getPkKills() {
        return pkKills;
    }

    public void setPkKills(Integer pkKills) {
        this.pkKills = pkKills;
    }

    public Integer getClanId() {
        return clanId;
    }

    public void setClanId(Integer clanId) {
        this.clanId = clanId;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getDeleteTime() {
        return deleteTime;
    }

    public void setDeleteTime(long deleteTime) {
        this.deleteTime = deleteTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(Integer accessLevel) {
        this.accessLevel = accessLevel;
    }

    public Integer getOnline() {
        return online;
    }

    public void setOnline(Integer online) {
        this.online = online;
    }

    public long getOnlineTime() {
        return onlineTime;
    }

    public void setOnlineTime(long onlineTime) {
        this.onlineTime = onlineTime;
    }

    public long getLastAccess() {
        return lastAccess;
    }

    public void setLastAccess(long lastAccess) {
        this.lastAccess = lastAccess;
    }

    public long getLeaveClan() {
        return leaveClan;
    }

    public void setLeaveClan(long leaveClan) {
        this.leaveClan = leaveClan;
    }

    public long getDeleteClan() {
        return deleteClan;
    }

    public void setDeleteClan(long deleteClan) {
        this.deleteClan = deleteClan;
    }

    public int getNoChannel() {
        return noChannel;
    }

    public void setNoChannel(int noChannel) {
        this.noChannel = noChannel;
    }

    public short getPledgeType() {
        return pledgeType;
    }

    public void setPledgeType(short pledgeType) {
        this.pledgeType = pledgeType;
    }

    public Integer getPledgeRank() {
        return pledgeRank;
    }

    public void setPledgeRank(Integer pledgeRank) {
        this.pledgeRank = pledgeRank;
    }

    public Integer getLvlJoinedAcademy() {
        return lvlJoinedAcademy;
    }

    public void setLvlJoinedAcademy(Integer lvlJoinedAcademy) {
        this.lvlJoinedAcademy = lvlJoinedAcademy;
    }

    public long getApprentice() {
        return apprentice;
    }

    public void setApprentice(long apprentice) {
        this.apprentice = apprentice;
    }

    public byte[] getKeyBindings() {
        return keyBindings;
    }

    public void setKeyBindings(byte[] keyBindings) {
        this.keyBindings = keyBindings;
    }

    public Integer getPcBangPoints() {
        return pcBangPoints;
    }

    public void setPcBangPoints(Integer pcBangPoints) {
        this.pcBangPoints = pcBangPoints;
    }

    public Integer getRaidBossPoints() {
        return raidBossPoints;
    }

    public void setRaidBossPoints(Integer raidBossPoints) {
        this.raidBossPoints = raidBossPoints;
    }

    public Integer getVitality() {
        return vitality;
    }

    public void setVitality(Integer vitality) {
        this.vitality = vitality;
    }

    public Integer getFame() {
        return fame;
    }

    public void setFame(Integer fame) {
        this.fame = fame;
    }

    public Integer getBaseClassId() {
        return baseClassId;
    }

    public void setBaseClassId(Integer baseClassId) {
        this.baseClassId = baseClassId;
    }

    public Integer getBookmarks() {
        return bookmarks;
    }

    public void setBookmarks(Integer bookmarks) {
        this.bookmarks = bookmarks;
    }

    public Integer getRecHave() {
        return recHave;
    }

    public void setRecHave(Integer recHave) {
        this.recHave = recHave;
    }

    public Integer getRecLeft() {
        return recLeft;
    }

    public void setRecLeft(Integer recLeft) {
        this.recLeft = recLeft;
    }

    public short getRecBonusTime() {
        return recBonusTime;
    }

    public void setRecBonusTime(short recBonusTime) {
        this.recBonusTime = recBonusTime;
    }

    public short getHuntingBonusTime() {
        return huntingBonusTime;
    }

    public void setHuntingBonusTime(short huntingBonusTime) {
        this.huntingBonusTime = huntingBonusTime;
    }

    public short getHuntingBonus() {
        return huntingBonus;
    }

    public void setHuntingBonus(short huntingBonus) {
        this.huntingBonus = huntingBonus;
    }

    public Integer getRecTickCnt() {
        return recTickCnt;
    }

    public void setRecTickCnt(Integer recTickCnt) {
        this.recTickCnt = recTickCnt;
    }

    public int getLastTeleport() {
        return lastTeleport;
    }

    public void setLastTeleport(int lastTeleport) {
        this.lastTeleport = lastTeleport;
    }
}