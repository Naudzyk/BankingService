package com.zhenya.ru.bank.repository;

import com.zhenya.ru.bank.models.Contribution;
import com.zhenya.ru.bank.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;


public interface ContributionRepository extends JpaRepository<Contribution,Long> {
    Contribution findByUser(User user);

    @Query("SELECT c.balance FROM Contribution  c WHERE c.user = :user")
    BigDecimal findBalanceByUser(User user);
}
