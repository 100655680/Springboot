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
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/games")
public class GameController {
    @Autowired private GameRepository      gameRepo;
    @Autowired private UserRepository      userRepo;
    @Autowired private GameTurnRepository  turnRepo;
    @Autowired private GameStatRepository  statRepo;

    /**
     * Start a new game. Pass both opponentId and your username.
     * Returns the new gameId.
     */
    @PostMapping
    public Long startGame(
        @RequestParam Long opponentId,
        @RequestParam String requester
    ) {
        User me = userRepo.findByUsername(requester)
            .orElseThrow(() ->
                new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Unknown user: " + requester
                )
            );
        User opp = userRepo.findById(opponentId)
            .orElseThrow(() ->
                new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Unknown opponent ID: " + opponentId
                )
            );

        // create the Game entry
        Game game = gameRepo.save(new Game());
        Long gid = game.getGameId();

        // seed stats for both players
        GameStat.StatId sidMe  = new GameStat.StatId(gid, me.getId().longValue());
        GameStat.StatId sidOpp = new GameStat.StatId(gid, opp.getId().longValue());
        statRepo.save(new GameStat(sidMe));
        statRepo.save(new GameStat(sidOpp));

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
        User me = userRepo.findByUsername(requester)
            .orElseThrow(() ->
                new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Unknown user: " + requester
                )
            );
        Game game = gameRepo.findById(gameId)
            .orElseThrow(() ->
                new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Unknown game ID: " + gameId
                )
            );

        // compute next turn number
        int turnNumber = turnRepo.countByGameAndUser(game, me) + 1;

        // save the turn
        GameTurn turn = new GameTurn();
        turn.setGame(game);
        turn.setUser(me);
        turn.setTurnNumber(turnNumber);
        turn.setScore(score);
        turn.setDartsThrown(dartsThrown);
        turnRepo.save(turn);

        // update stats (or create if missing)
        GameStat.StatId sid = new GameStat.StatId(gameId, me.getId().longValue());
        GameStat stats = statRepo.findById(sid)
            .orElseGet(() -> new GameStat(sid));
        stats.setTotalDarts(stats.getTotalDarts() + dartsThrown);
        if (score == 120) stats.setCount120s(stats.getCount120s() + 1);
        else if (score == 140) stats.setCount140s(stats.getCount140s() + 1);
        else if (score == 180) stats.setCount180s(stats.getCount180s() + 1);
        else if (score > 100) stats.setCount100Plus(stats.getCount100Plus() + 1);
        statRepo.save(stats);

        return turnNumber;
    }
}
