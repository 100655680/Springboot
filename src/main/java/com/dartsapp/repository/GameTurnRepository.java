package com.dartsapp.repository;

import com.dartsapp.model.Game;
import com.dartsapp.model.User;
import com.dartsapp.model.GameTurn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GameTurnRepository extends JpaRepository<GameTurn, Long> {

  /**
   * Instead of relying on Spring Data to infer COUNT(id),
   * we explicitly do COUNT(t) so no nonexistent "id" column is referenced.
   */
  @Query("SELECT COUNT(t) FROM GameTurn t WHERE t.game = :game AND t.user = :user")
  long countByGameAndUser(
    @Param("game") Game game,
    @Param("user") User user
  );
}
