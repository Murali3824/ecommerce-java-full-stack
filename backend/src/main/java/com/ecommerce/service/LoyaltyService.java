package com.ecommerce.service;

import com.ecommerce.entity.LoyaltyAccount;
import com.ecommerce.entity.LoyaltyTransaction;
import com.ecommerce.entity.User;
import com.ecommerce.repository.LoyaltyRepository;
import com.ecommerce.repository.LoyaltyTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LoyaltyService {

    @Autowired
    private LoyaltyRepository loyaltyRepository;

    @Autowired
    private LoyaltyTransactionRepository transactionRepository;

    @Transactional
    public void earnPoints(User user, Double orderAmount) {
        int earned = (int) (orderAmount / 10); // 1 point per $10 spent
        if (earned > 0) {
            LoyaltyAccount account = loyaltyRepository.findByUserId(user.getId()).orElseGet(() -> {
                LoyaltyAccount newAccount = new LoyaltyAccount();
                newAccount.setUser(user);
                return loyaltyRepository.save(newAccount);
            });
            account.setTotalPoints(account.getTotalPoints() + earned);
            loyaltyRepository.save(account);

            LoyaltyTransaction tx = new LoyaltyTransaction();
            tx.setUser(user);
            tx.setPoints(earned);
            tx.setType("EARNED");
            tx.setDescription("Earned points on order");
            transactionRepository.save(tx);
        }
    }
}
