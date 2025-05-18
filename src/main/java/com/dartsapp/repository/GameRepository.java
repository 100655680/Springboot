package com.dartsapp.repository;
import com.dartsapp.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;
public interface GameRepository extends JpaRepository<Game, Long> {}


