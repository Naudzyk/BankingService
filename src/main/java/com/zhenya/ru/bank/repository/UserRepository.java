package com.zhenya.ru.bank.repository;

import com.zhenya.ru.bank.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    User findByUsername(String username);

    Optional<User> findUserByUsername(String username);

    Boolean existsUserByUsername(String username);

    User getUserByUsername (String username);


    Object findAllById(long id);

    @Transactional
    @Modifying
    @Query("SELECT u FROM User u WHERE u.fullname LIKE CONCAT(:fullName, '%')")
    List<User> findUserByFullname( String fullName);


    Optional<User> findById(Long id);

}
