package com.prosa.rivertech.rest.bankservices.service;

import com.prosa.rivertech.rest.bankservices.entity.Account;
import com.prosa.rivertech.rest.bankservices.entity.User;

import java.util.List;

public interface AccountService {

    public List<Account> findAll();

    public Account findById(Long id);

    public Account createAccount(Long userId, String password);

//    Disabled
//    public Account update (Account account);

    public void delete(Long id);
}
