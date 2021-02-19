package com.prosa.rivertech.rest.bankservices.controller;

import com.prosa.rivertech.rest.bankservices.config.SwaggerConfig;
import com.prosa.rivertech.rest.bankservices.dto.AccountCreateRequest;
import com.prosa.rivertech.rest.bankservices.dto.AccountDto;
import com.prosa.rivertech.rest.bankservices.entity.Account;
import com.prosa.rivertech.rest.bankservices.entity.User;
import com.prosa.rivertech.rest.bankservices.mapper.AccountMapper;
import com.prosa.rivertech.rest.bankservices.service.AccountService;
import com.prosa.rivertech.rest.bankservices.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Api(tags = { SwaggerConfig.TAG_ACCOUNT })
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
    @ApiOperation(value = "Retrieve all bank accounts")
    public ResponseEntity<List<AccountDto>> retrieveAllAccounts() {
        List<Account> accounts = accountService.findAll();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(accounts.stream().map(accountMapper::mapToDto).collect(Collectors.toList()));
    }


    @GetMapping("/accounts/{accountId}")
    @ApiOperation(value = "Retrieve an account information by id")
    public ResponseEntity<AccountDto> retrieveAccountById(@PathVariable long accountId) {
        Account account = accountService.findById(accountId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(accountMapper.mapToDto(account));
    }


    @GetMapping("/users/{userId}/accounts")
    @ApiOperation(value = "Retrieve all accounts from a client")
    public ResponseEntity<List<AccountDto>> retrieveAccountsByUser(@PathVariable Long userId) {
        User user = userService.findById(userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(user.getAccounts().stream().map(accountMapper::mapToDto).collect(Collectors.toList()));
    }

    @PostMapping("/users/{userId}/accounts")
    @ApiOperation(value = "Create a new account for an existing client")
    public ResponseEntity<Object> addAccount(@PathVariable Long userId, @Valid @RequestBody AccountCreateRequest accountCreateRequest) {
        Account newAccount = accountService.createAccount(userId, accountCreateRequest.getPassword());
        URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/accounts/{accountId}").buildAndExpand(newAccount.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

//    Disabled --> Only make sense for change password and this is not covered for the test --> no continue
//    @PutMapping("/users/{userId}/accounts")
//    public ResponseEntity<AccountDto> updateAccount(@PathVariable Long userId, @RequestBody Account account) {
//        User user = userService.findById(userId);
//        account.setOwner(user);
//        Account newAccount = accountService.update(account);
//        return ResponseEntity
//                .status(HttpStatus.CREATED)
//                .body(accountMapper.mapToDto(newAccount));
//    }

    @DeleteMapping("/accounts/{accountId}")
    @ApiOperation(value = "Create an existing account")
    public ResponseEntity<String> deleteAccount(@PathVariable Long accountId) {
        accountService.delete(accountId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.TEXT_PLAIN)
                .body("Deleted account id: " + accountId);
    }
}
