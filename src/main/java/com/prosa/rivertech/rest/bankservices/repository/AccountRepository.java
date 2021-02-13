package com.prosa.rivertech.rest.bankservices.repository;

import com.prosa.rivertech.rest.bankservices.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
