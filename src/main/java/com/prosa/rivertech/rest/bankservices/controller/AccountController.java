package com.prosa.rivertech.rest.bankservices.controller;

import com.prosa.rivertech.rest.bankservices.entity.Account;
import com.prosa.rivertech.rest.bankservices.entity.Beneficiary;
import com.prosa.rivertech.rest.bankservices.service.AccountService;
import com.prosa.rivertech.rest.bankservices.service.BeneficiaryService;
import com.prosa.rivertech.rest.bankservices.utils.FilterResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class AccountController {
    private final AccountService accountService;
    private final BeneficiaryService beneficiaryService;
    private final FilterResponse filterResponse;

    @Autowired
    public AccountController(AccountService accountService, BeneficiaryService beneficiaryService,  FilterResponse filterResponse) {
        this.accountService = accountService;
        this.beneficiaryService = beneficiaryService;
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

    @PostMapping("/beneficiaries/{beneficiary_id}/accounts")
    public MappingJacksonValue addAccount(@PathVariable Long beneficiary_id, @RequestBody Account account) {

        Optional<Beneficiary> beneficiaryOptional = Optional.ofNullable(beneficiaryService.findById(beneficiary_id));
        if (!beneficiaryOptional.isPresent()) {
            throw new RuntimeException("Account not found id: " + beneficiary_id);
        }
        Beneficiary beneficiary = beneficiaryOptional.get();
        account.setOwner(beneficiary);
        Account newAccount = accountService.save(account);
        return filterResponse.getMappingJacksonValue(newAccount, filterResponse.AccountFilter, filterResponse.AccountFilterMapping);
    }

    @PutMapping("/beneficiaries/{beneficiary_id}/accounts")
    public MappingJacksonValue updateAccount(@PathVariable Long beneficiary_id, @RequestBody Account account) {
        Optional<Beneficiary> beneficiaryOptional = Optional.ofNullable(beneficiaryService.findById(beneficiary_id));
        if (!beneficiaryOptional.isPresent()) {
            throw new RuntimeException("Beneficiary not found id: " + beneficiary_id);
        }
        Beneficiary beneficiary = beneficiaryOptional.get();
        account.setOwner(beneficiary);
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
