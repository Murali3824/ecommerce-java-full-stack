package com.ecommerce.repository;

import com.ecommerce.entity.LoyaltyAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoyaltyRepository extends JpaRepository<LoyaltyAccount, Long> {
}
