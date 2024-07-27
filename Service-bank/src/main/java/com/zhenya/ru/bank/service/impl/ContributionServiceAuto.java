package com.zhenya.ru.bank.service.impl;

import com.zhenya.ru.bank.models.Contribution;
import com.zhenya.ru.bank.repository.ContributionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ContributionServiceAuto {
    private static final BigDecimal MAX_LIMIT = BigDecimal.valueOf(2.07);
    private static final BigDecimal PERCENT_BONUS = BigDecimal.valueOf(1.1);

    private static final Logger logger = LoggerFactory.getLogger(ContributionServiceAuto.class);


    private final ContributionRepository contributionRepository;




    @Scheduled(fixedRate = 30000) // 30 секунд
    public void increaseBalanceAutomatically() {

        try {
                List<Contribution> contributions = contributionRepository.findAll();
                for (Contribution contribution : contributions) {
                        BigDecimal maxBalance = contribution.getInitialBalance().multiply(MAX_LIMIT);
                        BigDecimal balance = contribution.getBalance().multiply(PERCENT_BONUS);
                        if (balance.compareTo(maxBalance) <= 0) {
                            contribution.setBalance(balance);
                            contributionRepository.save(contribution);

                    }
                }
        } catch (Exception e) {
           logger.error("Ошибка при автоматическом увеличении баланса: {}", e.getMessage(), e);
        }
    }
}

