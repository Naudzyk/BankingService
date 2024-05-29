package com.zhenya.ru.bank.repository;

import com.zhenya.ru.bank.models.User;
import com.zhenya.ru.bank.models.UserEmail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface UserEmailRepository extends JpaRepository<UserEmail,Integer> {

    List<UserEmail> findUserEmailByUser(User user);

    Boolean existsUserEmailByEmail(String email);
    @Transactional
    @Modifying
    @Query("DELETE FROM UserEmail e WHERE e.email = :email")
    void deleteByEmail(String email);
}
