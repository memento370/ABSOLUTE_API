package com.example.absoluteweb.server.entity;

import java.io.Serializable;
import java.util.Objects;

public class CharacterSubclassesId implements Serializable {

    private int charObjId;
    private int classId;

    // Конструктор за замовчуванням
    public CharacterSubclassesId() {
    }

    public CharacterSubclassesId(int charObjId, int classId) {
        this.charObjId = charObjId;
        this.classId = classId;
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

//    // Перевизначаємо equals() та hashCode()
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (!(o instanceof CharacterSubclassesId)) return false;
//        CharacterSubclassesId that = (CharacterSubclassesId) o;
//        return charObjId == that.charObjId &&
//                classId == that.classId;
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(charObjId, classId);
//    }
}
