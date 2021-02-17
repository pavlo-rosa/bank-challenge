package com.prosa.rivertech.rest.bankservices.controller;

import com.prosa.rivertech.rest.bankservices.dto.AccountCreateRequest;
import com.prosa.rivertech.rest.bankservices.dto.AccountDto;
import com.prosa.rivertech.rest.bankservices.entity.Account;
import com.prosa.rivertech.rest.bankservices.entity.User;
import com.prosa.rivertech.rest.bankservices.mapper.AccountMapper;
import com.prosa.rivertech.rest.bankservices.service.AccountService;
import com.prosa.rivertech.rest.bankservices.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class AccountController {
    private final AccountService accountService;
    private final UserService userService;
    private final AccountMapper accountMapper;

    @Autowired
    public AccountController(AccountService accountService, UserService userService, AccountMapper accountMapper) {
        this.accountService = accountService;
        this.userService = userService;
        this.accountMapper = accountMapper;
    }

    @GetMapping("/accounts")
    public ResponseEntity<List<AccountDto>> retrieveAllAccounts() {
        List<Account> accounts = accountService.findAll();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(accounts.stream().map(accountMapper::mapToDto).collect(Collectors.toList()));
    }


    @GetMapping("/accounts/{id}")
    public ResponseEntity<AccountDto> retrieveAccountById(@PathVariable long id) {
        Account account = accountService.findById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(accountMapper.mapToDto(account));
    }


    @GetMapping("/users/{userId}/accounts")
    public ResponseEntity<List<AccountDto>> retrieveAccountsByUser(@PathVariable Long userId) {
        User user = userService.findById(userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(user.getAccounts().stream().map(accountMapper::mapToDto).collect(Collectors.toList()));
    }

    @PostMapping("/users/{userId}/accounts")
    public ResponseEntity<Object> addAccount(@PathVariable Long userId, @Valid @RequestBody AccountCreateRequest accountCreateRequest) {
        User user = userService.findById(userId);
        Account newAccount = accountService.createAccount(user, accountCreateRequest.getPassword());
        URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/accounts/{accountId}").buildAndExpand(newAccount.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

//    Disabled --> Only make sense for change password and this is not covered for the test
//    @PutMapping("/users/{userId}/accounts")
//    public ResponseEntity<AccountDto> updateAccount(@PathVariable Long userId, @RequestBody Account account) {
//        User user = userService.findById(userId);
//        account.setOwner(user);
//        Account newAccount = accountService.update(account);
//        return ResponseEntity
//                .status(HttpStatus.CREATED)
//                .body(accountMapper.mapToDto(newAccount));
//    }

    @DeleteMapping("/accounts/{id}")
    public ResponseEntity<String> deleteAccount(@PathVariable Long id) {
        accountService.delete(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Deleted account id: " + id);
    }
}
