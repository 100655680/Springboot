package com.dartsapp.repository;
import com.dartsapp.model.GameStat;
import org.springframework.data.jpa.repository.JpaRepository;
public interface GameStatRepository extends JpaRepository<GameStat, GameStat.StatId> {}