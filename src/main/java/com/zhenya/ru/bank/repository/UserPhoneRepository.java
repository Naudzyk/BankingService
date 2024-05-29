package com.zhenya.ru.bank.repository;

import com.zhenya.ru.bank.models.User;
import com.zhenya.ru.bank.models.UserPhones;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserPhoneRepository extends JpaRepository<UserPhones,Integer> {

    void deleteById(Integer id);

    List<UserPhones> findUserPhonesByUser(User user);
    Boolean existsUserPhonesByPhone (String phone);


    @Transactional
    @Modifying
    @Query("DELETE FROM UserPhones e WHERE e.phone = :phone")
    void deleteByPhone(String phone);




}
