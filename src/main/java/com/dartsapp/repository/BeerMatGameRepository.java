package com.dartsapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.dartsapp.model.BeerMatGame;

@Repository
public interface BeerMatGameRepository extends JpaRepository<BeerMatGame, Long> {
}
