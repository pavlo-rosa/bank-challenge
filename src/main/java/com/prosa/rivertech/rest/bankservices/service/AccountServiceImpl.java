package com.prosa.rivertech.rest.bankservices.service;

import com.prosa.rivertech.rest.bankservices.entity.Account;
import com.prosa.rivertech.rest.bankservices.entity.Account;
import com.prosa.rivertech.rest.bankservices.exception.NotFoundException;
import com.prosa.rivertech.rest.bankservices.repository.AccountRepository;
import com.prosa.rivertech.rest.bankservices.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import sun.security.util.Password;

import java.security.SecureRandom;
import java.sql.Timestamp;
import java.util.*;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    @Override
    public Account findById(Long id) {
        Optional<Account> result = accountRepository.findById(id);
        Account account = null;
        if (result.isPresent()) {
            account = result.get();
        } else {
            throw new NotFoundException("Account id not found - " + id);
        }
        return account;
    }

    @Override
    public Account save(Account account) {
        String encodedPassword = new BCryptPasswordEncoder().encode(account.getPassword());
        account.setPassword(encodedPassword);
        return accountRepository.save(account);
    }

    @Override
    public void delete(Long id) {
        Account account = this.findById(id);
        if (account != null) {
            accountRepository.deleteById(id);
        }

    }
}
