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
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/games")
public class GameController {
    @Autowired private GameRepository     gameRepo;
    @Autowired private UserRepository     userRepo;
    @Autowired private GameTurnRepository turnRepo;
    @Autowired private GameStatRepository statRepo;

    /**
     * Start a new game. Pass both opponentId and your username.
     * Returns the new gameId.
     */
    @PostMapping
    public Long startGame(
        @RequestParam Long opponentId,
        @RequestParam String requester
    ) {
        // lookup both players by username and ID
        User me       = userRepo.findByUsername(requester)
                         .orElseThrow(() -> new RuntimeException("Unknown user: " + requester));
        User opponent = userRepo.findById(opponentId)
                         .orElseThrow(() -> new RuntimeException("Unknown opponent ID: " + opponentId));

        // create the Game entry
        Game game = gameRepo.save(new Game());

        // seed stats for both players
        Long gid = game.getGameId();
        GameStat.StatId sid1 = new GameStat.StatId(gid, me.getId().longValue());
        GameStat.StatId sid2 = new GameStat.StatId(gid, opponent.getId().longValue());
        statRepo.save(new GameStat(sid1));
        statRepo.save(new GameStat(sid2));

        return gid;
    }

    /**
     * Record a turn. Pass your username as 'requester' param.
     * Returns the turn number.
     */
    @PostMapping("/{gameId}/turns")
    public int addTurn(
        @PathVariable Long gameId,
        @RequestParam int score,
        @RequestParam(defaultValue = "3") int dartsThrown,
        @RequestParam String requester
    ) {
        User me   = userRepo.findByUsername(requester)
                         .orElseThrow(() -> new RuntimeException("Unknown user: " + requester));
        Game game = gameRepo.findById(gameId)
                         .orElseThrow(() -> new RuntimeException("Unknown game: " + gameId));

        int turnNumber = turnRepo.countByGameAndUser(game, me) + 1;

        // save the turn
        GameTurn turn = new GameTurn();
        turn.setGame(game);
        turn.setUser(me);
        turn.setTurnNumber(turnNumber);
        turn.setScore(score);
        turn.setDartsThrown(dartsThrown);
        turnRepo.save(turn);

        // update stats
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
