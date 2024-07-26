package com.zhenya.ru.bank.service.impl;

import com.zhenya.ru.bank.models.User;
import com.zhenya.ru.bank.models.UserEmail;
import com.zhenya.ru.bank.repository.UserEmailRepository;
import com.zhenya.ru.bank.repository.UserRepository;
import com.zhenya.ru.bank.service.UserEmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserEmailServiceImpl implements UserEmailService {
    private final UserEmailRepository userEmailRepository;
    private final UserRepository userRepository;
   @Override
    public ResponseEntity<?> save(String email, String username) {
        if (!userEmailRepository.existsUserEmailByEmail(email)) {
            User user = userRepository.getUserByUsername(username);

            UserEmail userEmail = new UserEmail();
            userEmail.setEmail(email);
            userEmail.setUser(user);

            userEmailRepository.save(userEmail);
            return ResponseEntity.ok("Добавлен email " + email);
        }


        return ResponseEntity.badRequest().body("Ошибка такой email уже зарегестрирован");
    }



    @Override
    @Transactional(readOnly = true)
    @Cacheable(
            value = "UserEmailService::getEmail",
            key= "#user"
    )
    public List<String> getEmails(User user) {
     List<String> emails = new ArrayList<>();
    List<UserEmail> userEmails = userEmailRepository.findUserEmailByUser(user);

    for (UserEmail userEmail : userEmails) {
        emails.add(userEmail.getEmail());
    }

    return emails;
}


    @Override
    public ResponseEntity<?> deleteEmail(String email,Long id) {
        if(userEmailRepository.findUserIdByEmail(email).equals(id)){
        if(userEmailRepository.deleteByEmail(email)==1){
            return ResponseEntity.ok("Email удален: " + email);
        }else {
            return ResponseEntity.badRequest().body("Некоректный email");
        }
        }else {
            return ResponseEntity.badRequest().body("Некоректный email");
        }
    }


}


