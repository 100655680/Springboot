// src/main/java/com/dartsapp/repository/GameTurnRepository.java
package com.dartsapp.repository;

import com.dartsapp.model.Game;
import com.dartsapp.model.GameTurn;
import com.dartsapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface GameTurnRepository extends JpaRepository<GameTurn, Long> {
  List<GameTurn> findByGameAndUserOrderByTurnNumberAsc(Game game, User user);
  long countByGameAndUser(Game game, User user);
  List<GameTurn> findByGameAndUser(Game game, User user);
}
