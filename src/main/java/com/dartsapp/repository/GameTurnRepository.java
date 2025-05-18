package com.dartsapp.repository;
import com.dartsapp.model.GameTurn;
import com.dartsapp.model.Game;        // ← add this
import com.dartsapp.model.User;        // ← and this
import org.springframework.data.jpa.repository.JpaRepository;
public interface GameTurnRepository extends JpaRepository<GameTurn, Long> {
    int countByGameAndUser(Game game, User user);
  }