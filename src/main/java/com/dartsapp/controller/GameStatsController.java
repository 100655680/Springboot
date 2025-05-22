// src/main/java/com/dartsapp/controller/GameStatsController.java
package com.dartsapp.controller;

import com.dartsapp.model.Game;
import com.dartsapp.model.GameStat;
import com.dartsapp.model.GameTurn;
import com.dartsapp.model.User;
import com.dartsapp.repository.GameRepository;
import com.dartsapp.repository.GameStatRepository;
import com.dartsapp.repository.GameTurnRepository;
import com.dartsapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.*;

@RestController
@RequestMapping("/api/games")
public class GameStatsController {

  @Autowired private UserRepository       userRepo;
  @Autowired private GameRepository       gameRepo;
  @Autowired private GameStatRepository   statRepo;
  @Autowired private GameTurnRepository   turnRepo;

  @GetMapping("/{gameId}/stats")
  public ResponseEntity<List<Map<String, Object>>> getStats(
      @PathVariable Long gameId,
      @RequestParam("requester") String requester
  ) {
    User user = userRepo.findByUsername(requester)
      .orElseThrow(() -> new RuntimeException("Unknown user: " + requester));
    Game game = gameRepo.findById(gameId)
      .orElseThrow(() -> new RuntimeException("Unknown game: " + gameId));

    // Row #1: this game
    GameStat.StatId sid = new GameStat.StatId(gameId, user.getId().longValue());
    GameStat gs = statRepo.findById(sid)
      .orElseThrow(() -> new RuntimeException("No stats for user/game"));

    List<GameTurn> turnsThis = turnRepo.findByGameAndUserOrderByTurnNumberAsc(game, user);
    double avgThis = turnsThis.isEmpty() ? 0
      : turnsThis.stream().mapToInt(GameTurn::getScore).average().orElse(0);

    Map<String,Object> row1 = new LinkedHashMap<>();
    row1.put("count_100",      gs.getCount100());
    row1.put("count_100_plus", gs.getCount100Plus());
    row1.put("count_120s",     gs.getCount120s());
    row1.put("count_140s",     gs.getCount140s());
    row1.put("count_180s",     gs.getCount180s());
    row1.put("average_score",  avgThis);

    // Row #2: aggregate across all games
    List<GameStat> allStats = statRepo.findByIdUserId(user.getId().longValue());
    int sum100      = allStats.stream().mapToInt(GameStat::getCount100).sum();
    int sum100Plus  = allStats.stream().mapToInt(GameStat::getCount100Plus).sum();
    int sum120s     = allStats.stream().mapToInt(GameStat::getCount120s).sum();
    int sum140s     = allStats.stream().mapToInt(GameStat::getCount140s).sum();
    int sum180s     = allStats.stream().mapToInt(GameStat::getCount180s).sum();

    // average of per-game averages
    List<Double> perGameAvgs = allStats.stream()
      .map(s -> {
        Game g = gameRepo.findById(s.getId().getGameId()).get();
        List<GameTurn> ts = turnRepo.findByGameAndUser(g, user);
        return ts.isEmpty() ? 0 : ts.stream().mapToInt(GameTurn::getScore).average().orElse(0);
      }).collect(Collectors.toList());
    double avgOfAvgs = perGameAvgs.stream().mapToDouble(d->d).average().orElse(0);

    Map<String,Object> row2 = new LinkedHashMap<>();
    row2.put("count_100",      sum100);
    row2.put("count_100_plus", sum100Plus);
    row2.put("count_120s",     sum120s);
    row2.put("count_140s",     sum140s);
    row2.put("count_180s",     sum180s);
    row2.put("average_score",  avgOfAvgs);

    return ResponseEntity.ok(Arrays.asList(row1, row2));
  }
}
