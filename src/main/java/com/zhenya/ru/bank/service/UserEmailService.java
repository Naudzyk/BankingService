package com.zhenya.ru.bank.service;

import com.zhenya.ru.bank.models.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserEmailService {
    ResponseEntity<?> save(String emails, String username);

    List<String> getEmails(User user);

    ResponseEntity<?> deleteEmail(String email,Long id);
}
