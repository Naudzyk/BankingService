package com.zhenya.ru.bank.controller;

import com.zhenya.ru.bank.dto.AuthDTO;
import com.zhenya.ru.bank.dto.TokenDTO;
import com.zhenya.ru.bank.dto.UserDTO;
import com.zhenya.ru.bank.it.AbstractRestControllerBaseTest;
import com.zhenya.ru.bank.service.SecurityService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
@Testcontainers
@ExtendWith(MockitoExtension.class)
public class SecurityControllerTest extends AbstractRestControllerBaseTest {

    @InjectMocks
    private SecurityController securityController;

    @Mock
    private SecurityService securityService;

    @Test
    public void testSingin() {
        AuthDTO authDTO = new AuthDTO("testUser", "testPassword");
        TokenDTO tokenDTO = new TokenDTO("authorizationToken");
        when(securityService.authorize("testUser", "testPassword")).thenReturn(ResponseEntity.ok(tokenDTO));

        ResponseEntity<TokenDTO> response = securityController.singin(authDTO);
         assertEquals(HttpStatus.OK, response.getStatusCode());
         assertEquals("authorizationToken", response.getBody().jwt());
    }

    @Test
    public void testSingup() {
        UserDTO userDTO = new UserDTO("testUser", "Test User", "test@example.com", "1234567890", LocalDate.now(), BigDecimal.valueOf(100.0), BigDecimal.valueOf(100.0),"password");
        ResponseEntity<?> response = securityController.singup(userDTO);

        verify(securityService).register("testUser", "Test User", "test@example.com", "1234567890", LocalDate.now(), BigDecimal.valueOf(100.0), "password");

        assertEquals(ResponseEntity.ok("Пользователь создан : testUser"), response);
    }
}