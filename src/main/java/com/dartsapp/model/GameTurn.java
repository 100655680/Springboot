package com.dartsapp.model;

import jakarta.persistence.*;

@Entity
@Table(name = "game_turns")
public class GameTurn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne @JoinColumn(name="game_id",    nullable=false)
    private Game  game;

    @ManyToOne @JoinColumn(name="user_id",    nullable=false)
    private User  user;

    @Column(name="turn_number", nullable=false)
    private Integer turnNumber;

    @Column(name="darts_thrown", nullable=false)
    private Integer dartsThrown;

    @Column(nullable=false)
    private Integer score;

    public GameTurn() {}

    public Long   getId()          { return id; }
    public void   setId(Long id)   { this.id = id; }

    public Game   getGame()        { return game; }
    public void   setGame(Game game) { this.game = game; }

    public User   getUser()        { return user; }
    public void   setUser(User user) { this.user = user; }

    public Integer getTurnNumber() { return turnNumber; }
    public void    setTurnNumber(Integer turnNumber) { this.turnNumber = turnNumber; }

    public Integer getDartsThrown() { return dartsThrown; }
    public void    setDartsThrown(Integer dartsThrown) { this.dartsThrown = dartsThrown; }

    public Integer getScore()      { return score; }
    public void    setScore(Integer score) { this.score = score; }
}
