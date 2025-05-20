package com.dartsapp.model;

import jakarta.persistence.*;

@Entity
@Table(name = "game_turns")
public class GameTurn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "turn_id")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "turn_number", nullable = false)
    private int turnNumber;

    @Column(nullable = false)
    private int score;

    @Column(name = "darts_thrown", nullable = false)
    private int dartsThrown;

    public GameTurn() {}

    public GameTurn(Game game, User user, int turnNumber, int score, int dartsThrown) {
        this.game = game;
        this.user = user;
        this.turnNumber = turnNumber;
        this.score = score;
        this.dartsThrown = dartsThrown;
    }

    // --- getters & setters ---

    public Long getId() {
        return id;
    }

    public Game getGame() {
        return game;
    }
    public void setGame(Game game) {
        this.game = game;
    }

    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }

    public int getTurnNumber() {
        return turnNumber;
    }
    public void setTurnNumber(int turnNumber) {
        this.turnNumber = turnNumber;
    }

    public int getScore() {
        return score;
    }
    public void setScore(int score) {
        this.score = score;
    }

    public int getDartsThrown() {
        return dartsThrown;
    }
    public void setDartsThrown(int dartsThrown) {
        this.dartsThrown = dartsThrown;
    }
}
