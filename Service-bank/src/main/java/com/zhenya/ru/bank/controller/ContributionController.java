package com.zhenya.ru.bank.controller;

import com.zhenya.ru.bank.dto.ContributionDTO;
import com.zhenya.ru.bank.service.ContributionService;
import com.zhenya.ru.bank.service.impl.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/con")
@RequiredArgsConstructor
public class ContributionController {
    private final ContributionService contributionService;

    @PostMapping("/contribution")
    public ResponseEntity<?> contribution(@RequestBody ContributionDTO contributionDTO){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return contributionService.increaseBalance(userDetails.getUsername(), contributionDTO.money());


    }
    @GetMapping("/moneyToTheAccount")
    public ResponseEntity<?> moneyToAccount(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return contributionService.moneyToTheAccount(userDetails.getUsername());


    }
    @GetMapping("/showMoneyInContribution")
    public ResponseEntity<?> showMoneyInContribution() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return contributionService.showMoneyInContribution(userDetails.getUsername());
    }
}
