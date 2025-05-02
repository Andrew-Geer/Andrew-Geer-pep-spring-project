package com.example.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import java.util.List;
import com.example.entity.Account;
import com.example.repository.AccountRepository;

@Service
public class AccountService 
{
    private final AccountRepository accountRepository;

    public AccountService (AccountRepository accountRepository)
    {
        this.accountRepository = accountRepository;
    }

    public Account createAccount(Account account)
    {
        accountRepository.save(account);
        return account;
    }
    
    public boolean isExistingUsername(String username)
    {
        List<Account> userList = accountRepository.findAllByUsername(username);
        if (userList.isEmpty())
        {
            return false;
        }
        return true;
    }

    public boolean validateNewAccountDetails (Account account)
    {
        String accountUsername = account.getUsername();
        String accountPassword = account.getPassword();
        int passwordMinimum = 4;

        if (accountUsername == "" || accountUsername == null)
        {
            return false;
        }
        if (accountPassword.length() < passwordMinimum)
        {
            return false;
        }

        return true;
    }
}
