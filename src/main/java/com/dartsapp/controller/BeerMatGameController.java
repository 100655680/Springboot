package com.dartsapp.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.dartsapp.repository.BeerMatGameRepository;
import com.dartsapp.repository.UserRepository;
import com.dartsapp.model.BeerMatGame;
import com.dartsapp.model.User;

import java.util.Map;
import java.util.HashMap;


@RestController
@RequestMapping("/api/beermatgames")
public class BeerMatGameController {
    @Autowired private BeerMatGameRepository beerMatRepo;
    @Autowired private UserRepository       userRepo;

    @PostMapping
    public Map<String, Long> startGame(@RequestParam String requester) {
        User u = userRepo.findByUsername(requester)
                 .orElseThrow(() -> new RuntimeException("Unknown user: " + requester));
        BeerMatGame g = new BeerMatGame();
        g.setUserId(u.getId().longValue());
        g = beerMatRepo.save(g);
        return Map.of("game_id", g.getGameId());
    }

    @PostMapping("/{gameId}/hit")
    public Map<String, Long> hit(
        @PathVariable Long gameId,
        @RequestParam String category
    ) {
        BeerMatGame g = beerMatRepo.findById(gameId)
            .orElseThrow(() -> new RuntimeException("Game not found: " + gameId));

        switch (category) {
            case "bad":  g.setBad(g.getBad() + 1);  break;
            case "poor": g.setPoor(g.getPoor() + 1); break;
            case "ok":   g.setOk(g.getOk() + 1);     break;
            case "good": g.setGood(g.getGood() + 1); break;
            default: throw new RuntimeException("Unknown category: " + category);
        }

        g = beerMatRepo.save(g);
        Map<String, Long> result = new HashMap<>();
        result.put("bad",  g.getBad());
        result.put("poor", g.getPoor());
        result.put("ok",   g.getOk());
        result.put("good", g.getGood());
        return result;
    }

    @GetMapping("/{gameId}/stats")
    public Map<String, Long> getStats(@PathVariable Long gameId) {
        BeerMatGame g = beerMatRepo.findById(gameId)
          .orElseThrow(() -> new RuntimeException("Unknown BeerMatGame: " + gameId));

        Map<String, Long> result = new HashMap<>();
        result.put("bad",  g.getBad());
        result.put("poor", g.getPoor());
        result.put("ok",   g.getOk());
        result.put("good", g.getGood());
        return result;
    }
}
