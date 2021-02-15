package com.prosa.rivertech.rest.bankservices.controller;

import com.prosa.rivertech.rest.bankservices.entity.Account;
import com.prosa.rivertech.rest.bankservices.entity.User;
import com.prosa.rivertech.rest.bankservices.service.AccountService;
import com.prosa.rivertech.rest.bankservices.service.UserService;
import com.prosa.rivertech.rest.bankservices.utils.FilterResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class AccountController {
    private final AccountService accountService;
    private final UserService userService;
    private final FilterResponse filterResponse;

    @Autowired
    public AccountController(AccountService accountService, UserService userService, FilterResponse filterResponse) {
        this.accountService = accountService;
        this.userService = userService;
        this.filterResponse = filterResponse;
    }

    @GetMapping("/accounts")
    public MappingJacksonValue retrieveAllAccounts() {
        List<Account> accounts = accountService.findAll();
        return filterResponse.getMappingJacksonValue(accounts, filterResponse.AccountFilter, filterResponse.AccountFilterMapping);
    }



    @GetMapping("/accounts/{id}")
    public MappingJacksonValue retrieveAccountById(@PathVariable long id) {
        Account account = accountService.findById(id);
        return filterResponse.getMappingJacksonValue(account, filterResponse.AccountFilter, filterResponse.AccountFilterMapping);
    }


    @GetMapping("/users/{userId}/accounts")
    public MappingJacksonValue retrieveAccountsByUser(@PathVariable Long userId) {
        User user = userService.findById(userId);
        if(user == null){
            return null;
        }
        return filterResponse.getMappingJacksonValue(user.getAccounts(), filterResponse.AccountFilter, filterResponse.AccountFilterMapping);
    }

    @PostMapping("/users/{userId}/accounts")
    public MappingJacksonValue addAccount(@PathVariable Long userId, @RequestBody Account account) {

        Optional<User> userOptional = Optional.ofNullable(userService.findById(userId));
        if (!userOptional.isPresent()) {
            throw new RuntimeException("Account not found id: " + userId);
        }
        User user = userOptional.get();
        account.setOwner(user);
        Account newAccount = accountService.save(account);
        return filterResponse.getMappingJacksonValue(newAccount, filterResponse.AccountFilter, filterResponse.AccountFilterMapping);
    }

    @PutMapping("/users/{userId}/accounts")
    public MappingJacksonValue updateAccount(@PathVariable Long userId, @RequestBody Account account) {
        Optional<User> userOptional = Optional.ofNullable(userService.findById(userId));
        if (!userOptional.isPresent()) {
            throw new RuntimeException("User not found id: " + userId);
        }
        User user = userOptional.get();
        account.setOwner(user);
        Account newAccount = accountService.save(account);
        return filterResponse.getMappingJacksonValue(newAccount, filterResponse.AccountFilter, filterResponse.AccountFilterMapping);
    }

    @DeleteMapping("/accounts/{id}")
    public String deleteAccount(@PathVariable Long id) {
        Account account = accountService.findById(id);
        if (account == null) {
            throw new RuntimeException("Account is not found: " + id);
        }
        accountService.delete(id);
        return "Deleted account id: " + id;
    }
}
