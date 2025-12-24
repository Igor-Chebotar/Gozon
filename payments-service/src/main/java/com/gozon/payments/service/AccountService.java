package com.gozon.payments.service;

import com.gozon.payments.entity.Account;
import com.gozon.payments.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class AccountService {

    private final AccountRepository accountRepo;

    public AccountService(AccountRepository accountRepo) {
        this.accountRepo = accountRepo;
    }

    @Transactional
    public Account createAccount(String userId) {
        if (accountRepo.existsById(userId)) {
            throw new RuntimeException("Account already exists");
        }
        Account account = new Account();
        account.setUserId(userId);
        return accountRepo.save(account);
    }

    @Transactional
    public Account topUp(String userId, BigDecimal amount) {
        Account account = accountRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        account.setBalance(account.getBalance().add(amount));
        return accountRepo.save(account);
    }

    public Optional<Account> getAccount(String userId) {
        return accountRepo.findById(userId);
    }

    @Transactional
    public boolean withdraw(String userId, BigDecimal amount) {
        Account account = accountRepo.findById(userId).orElse(null);
        if (account == null) {
            return false;
        }
        if (account.getBalance().compareTo(amount) < 0) {
            return false;
        }
        account.setBalance(account.getBalance().subtract(amount));
        accountRepo.save(account);
        return true;
    }
}
