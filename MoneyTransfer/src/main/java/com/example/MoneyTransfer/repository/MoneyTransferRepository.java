package com.example.MoneyTransfer.repository;

import com.example.MoneyTransfer.model.MoneyTransfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MoneyTransferRepository extends JpaRepository<MoneyTransfer,Integer> {
    MoneyTransfer getMoneyTransferByClientusername (String username);

}
