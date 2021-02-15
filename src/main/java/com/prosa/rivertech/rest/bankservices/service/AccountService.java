package com.prosa.rivertech.rest.bankservices.service;

import com.prosa.rivertech.rest.bankservices.entity.Account;

import java.util.List;

public interface AccountService {

    public List<Account> findAll();

    public Account findById(Long id);

    public Account save (Account account);

    public void delete(Long id);
}
