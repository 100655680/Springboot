package com.dartsapp.model;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "game_stats",
       uniqueConstraints = @UniqueConstraint(columnNames = {"game_id","user_id"}))
public class GameStat {

    @Embeddable
    public static class StatId implements Serializable {
        @Column(name="game_id") private Long gameId;
        @Column(name="user_id") private Long userId;

        public StatId() {}

        /** existing constructor call in your controller **/
        public StatId(Long gameId, Long userId) {
            this.gameId = gameId;
            this.userId = userId;
        }

        public Long getGameId()  { return gameId; }
        public void setGameId(Long gameId) { this.gameId = gameId; }

        public Long getUserId()  { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }

        // equals() / hashCode() omitted for brevity—just be sure you include them!
    }

    @EmbeddedId
    private StatId id;

    @Column(name="count_100", nullable=false)
    private int count100 = 0;
    @Column(name="count_100_plus", nullable=false)
    private int count100Plus = 0;
    @Column(name="count_120s",      nullable=false)
    private int count120s     = 0;
    @Column(name="count_140s",      nullable=false)
    private int count140s     = 0;
    @Column(name="count_180s",      nullable=false)
    private int count180s     = 0;
    @Column(name="total_darts",     nullable=false)
    private int totalDarts    = 0;

    public GameStat() {}

    /** so you can do new GameStat(id) in your controller **/
    public GameStat(StatId id) {
        this.id = id;
    }

    public StatId getId()  { return id; }
    public void   setId(StatId id)  { this.id = id; }

    public int getCount100() { return count100; }
    public void setCount100(int count100) { this.count100 = count100; }

    public int getCount100Plus() { return count100Plus; }
    public void setCount100Plus(int c) { this.count100Plus = c; }

    public int getCount120s() { return count120s; }
    public void setCount120s(int c)  { this.count120s = c; }

    public int getCount140s() { return count140s; }
    public void setCount140s(int c)  { this.count140s = c; }

    public int getCount180s() { return count180s; }
    public void setCount180s(int c)  { this.count180s = c; }

    public int getTotalDarts() { return totalDarts; }
    public void setTotalDarts(int t) { this.totalDarts = t; }

    @Transient
public double getAverageScore() {
    // totalDarts is number of darts thrown; to compute avg per turn
    // you’d need totalTurns or totalScore in the table
    // here’s an example if you had totalScore column:
    // return totalDarts>0 ? ((double) totalScore / totalDarts) * 3 : 0;
    return 0;  // stub: replace with your actual calculation or remove if not needed
}

}
