// src/main/java/com/dartsapp/repository/GameStatRepository.java
package com.dartsapp.repository;

import com.dartsapp.model.GameStat;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface GameStatRepository
    extends JpaRepository<GameStat, GameStat.StatId> {
  // find all game‐stat rows for a given user
  List<GameStat> findByIdUserId(Long userId);
}
