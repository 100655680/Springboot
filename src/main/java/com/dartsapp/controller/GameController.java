package com.dartsapp.controller;

import com.dartsapp.model.*;
import com.dartsapp.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

@RestController
@RequestMapping("/api/games")
public class GameController {
    @Autowired private GameRepository      gameRepo;
    @Autowired private UserRepository      userRepo;
    @Autowired private GameTurnRepository  turnRepo;
    @Autowired private GameStatRepository  statRepo;

    @PostMapping
    public Long startGame(
        @RequestParam Long opponentId,
        @RequestParam String requester
    ) {
        User me       = userRepo.findByUsername(requester)
                         .orElseThrow(() -> new RuntimeException("Unknown user: " + requester));
        User opponent = userRepo.findById(opponentId)
                         .orElseThrow(() -> new RuntimeException("Unknown opponent ID: " + opponentId));

        Game game = gameRepo.save(new Game());
        Long gid = game.getGameId();

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

        // use our new constructor instead of setters
        GameTurn turn = new GameTurn(game, me, turnNumber, score, dartsThrown);
        turnRepo.save(turn);

        // update stats as before
        GameStat.StatId sid = new GameStat.StatId(gameId, me.getId().longValue());
        GameStat stats = statRepo.findById(sid)
                          .orElseThrow(() -> new RuntimeException("No stats for user/game"));
        stats.setTotalDarts(stats.getTotalDarts() + dartsThrown);
        if (score == 120) stats.setCount120s(stats.getCount120s() + 1);
        else if (score == 140) stats.setCount140s(stats.getCount140s() + 1);
        else if (score == 180) stats.setCount180s(stats.getCount180s() + 1);
        else if (score > 100) stats.setCount100Plus(stats.getCount100Plus() + 1);
        statRepo.save(stats);

        return turnNumber;
    }
}
