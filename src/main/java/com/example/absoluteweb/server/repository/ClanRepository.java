package com.example.absoluteweb.server.repository;

import com.example.absoluteweb.server.entity.ClanData;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface ClanRepository extends CrudRepository<ClanData, Integer> {

    @org.springframework.data.jpa.repository.Query(value = """
            SELECT cs.name, cd.reputation_score
            FROM clan_data cd
            JOIN clan_subpledges cs ON cs.clan_id = cd.clan_id
            WHERE cs.type = 0
            ORDER BY cd.reputation_score DESC
            LIMIT 10
            """, nativeQuery = true)
    List<Object[]> findTop10ClansRaw();
}