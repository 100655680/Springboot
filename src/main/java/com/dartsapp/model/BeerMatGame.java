package com.dartsapp.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "beer_mat_game")
public class BeerMatGame {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "game_id")
    private Long gameId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "start_datetime", nullable = false)
    private LocalDateTime startDatetime = LocalDateTime.now();

    @Column(nullable = false)
    private Long bad = 0L;

    @Column(nullable = false)
    private Long poor = 0L;

    @Column(name = "ok", nullable = false)
    private Long ok = 0L;

    @Column(nullable = false)
    private Long good = 0L;

    public BeerMatGame() {}

    public Long getGameId() {
        return gameId;
    }
    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public LocalDateTime getStartDatetime() {
        return startDatetime;
    }
    public void setStartDatetime(LocalDateTime startDatetime) {
        this.startDatetime = startDatetime;
    }
    public Long getBad() {
        return bad;
    }
    public void setBad(Long bad) {
        this.bad = bad;
    }
    public Long getPoor() {
        return poor;
    }
    public void setPoor(Long poor) {
        this.poor = poor;
    }
    public Long getOk() {
        return ok;
    }
    public void setOk(Long ok) {
        this.ok = ok;
    }
    public Long getGood() {
        return good;
    }
    public void setGood(Long good) {
        this.good = good;
    }
}
