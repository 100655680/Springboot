package com.dartsapp.controller;

import com.dartsapp.model.*;
import com.dartsapp.repository.*;

import java.util.stream.Collectors;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

@RestController
@RequestMapping("/api/games")
public class GameController {
    @Autowired private GameRepository     gameRepo;
    @Autowired private UserRepository     userRepo;
    @Autowired private GameTurnRepository turnRepo;
    @Autowired private GameStatRepository statRepo;


    @PostMapping
    public Long startGame(
        @RequestParam Long opponentId,
        @RequestParam String requester
    ) {
        User me = userRepo.findByUsername(requester)
                         .orElseThrow(() -> new RuntimeException("Unknown user: " + requester));
        User opponent = userRepo.findById(opponentId)
                         .orElseThrow(() -> new RuntimeException("Unknown opponent ID: " + opponentId));

        // set both IDs before saving
        Game game = new Game();
        game.setPlayer1Id(me.getId().longValue());
        game.setPlayer2Id(opponent.getId().longValue());
        Game saved = gameRepo.save(game);
        Long gid = saved.getGameId();

        // seed stats
        GameStat.StatId sid1 = new GameStat.StatId(gid, me.getId().longValue());
        GameStat.StatId sid2 = new GameStat.StatId(gid, opponent.getId().longValue());
        statRepo.save(new GameStat(sid1));
        statRepo.save(new GameStat(sid2));

        return gid;
    }


    @PostMapping("/{gameId}/turns")
    @Transactional
    public int addTurn(
        @PathVariable Long gameId,
        @RequestParam("score") int score,
        @RequestParam(value = "dartsThrown", defaultValue = "3") int dartsThrown,
        @RequestParam("requester") String requester
    ) {
        User me   = userRepo.findByUsername(requester)
                         .orElseThrow(() -> new RuntimeException("Unknown user: " + requester));
        Game game = gameRepo.findById(gameId)
                         .orElseThrow(() -> new RuntimeException("Unknown game: " + gameId));

        int turnNumber = (int)(turnRepo.countByGameAndUser(game, me) + 1);

        GameTurn turn = new GameTurn(game, me, turnNumber, score, dartsThrown);
        turnRepo.save(turn);

        GameStat.StatId sid = new GameStat.StatId(gameId, me.getId().longValue());
        GameStat stats = statRepo.findById(sid)
                          .orElseThrow(() -> new RuntimeException("No stats for user/game"));
        stats.setTotalDarts(stats.getTotalDarts() + dartsThrown);
        if (score == 100) {
            stats.setCount100(stats.getCount100() + 1);
        } else if (score > 100 && score != 120 && score != 140 && score != 180) {
            stats.setCount100Plus(stats.getCount100Plus() + 1);
        } else if (score == 120) {
            stats.setCount120s(stats.getCount120s() + 1);
        } else if (score == 140) {
            stats.setCount140s(stats.getCount140s() + 1);
        } else if (score == 180) {
            stats.setCount180s(stats.getCount180s() + 1);
        }
        statRepo.save(stats);

        return turnNumber;
    }

    @GetMapping("/{gameId}/turns")
    public List<TurnResponseDto> getTurns(
        @PathVariable Long gameId,
        @RequestParam("requester") String requester
    ) {
        User user = userRepo.findByUsername(requester)
                      .orElseThrow(() -> new RuntimeException("Unknown user: " + requester));
        Game game = gameRepo.findById(gameId)
                      .orElseThrow(() -> new RuntimeException("Unknown game: " + gameId));

        return turnRepo.findByGameAndUserOrderByTurnNumberAsc(game, user).stream()
            .map(t -> new TurnResponseDto(t.getTurnNumber(), t.getScore(), t.getDartsThrown()))
            .collect(Collectors.toList());
    }

    // DTO for turns
    public static class TurnResponseDto {
        private int turnNumber;
        private int score;
        private int dartsThrown;

        public TurnResponseDto(int turnNumber, int score, int dartsThrown) {
            this.turnNumber  = turnNumber;
            this.score       = score;
            this.dartsThrown = dartsThrown;
        }
        public int getTurnNumber()  { return turnNumber; }
        public int getScore()       { return score; }
        public int getDartsThrown() { return dartsThrown; }
    }

      @PutMapping("/{gameId}/end")
    public void endGame(
        @PathVariable Long gameId,
        @RequestParam String winnerUsername
    ) {
        Game game = gameRepo.findById(gameId)
            .orElseThrow(() -> new RuntimeException("No such game: " + gameId));
        User winner = userRepo.findByUsername(winnerUsername)
            .orElseThrow(() -> new RuntimeException("Unknown user: " + winnerUsername));

        game.setWinnerId(winner.getId().longValue());
        game.setEndedAt(LocalDateTime.now());
        gameRepo.save(game);
    }
}
