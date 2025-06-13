package com.example.absoluteweb.server.repository;

import com.example.absoluteweb.server.entity.CharacterSubclasses;
import com.example.absoluteweb.server.entity.GameCharacter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CharacterSubclassesRep extends JpaRepository<CharacterSubclasses, Integer> {
    List<CharacterSubclasses> findByCharObjId(int charObjId);
}