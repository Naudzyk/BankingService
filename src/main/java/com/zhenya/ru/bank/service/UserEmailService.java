package com.zhenya.ru.bank.service;

import com.zhenya.ru.bank.models.User;
import com.zhenya.ru.bank.models.UserEmail;

import java.util.List;

public interface UserEmailService {
    String save(String emails, String username);

    List<String> getEmails(User user);

    void deleteEmail(String email);
}
