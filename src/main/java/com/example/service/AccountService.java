package com.example.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import java.util.List;
import com.example.entity.Account;
import com.example.repository.AccountRepository;

@Service
public class AccountService 
{

    //Account repoistory variable
    private final AccountRepository accountRepository;

    //Constrctor for spring dependecy injection
    public AccountService (AccountRepository accountRepository)
    {
        this.accountRepository = accountRepository;
    }

    /**
     * @info Saves an Account to the database
     * @param message - The Account to be saved to the database (Id will be generarted)
     * @return An Account object with the generated id
     */
    public Account createAccount(Account account)
    {
        accountRepository.save(account);
        return account;
    }
    
    /**
     * @info Checks if a username exists in the databse
     * @param username - The username that is to be checked
     * @return True if the username is not in the database
     * @return False if the username is in the database
     */
    public boolean isExistingUsername(String username)
    {
        List<Account> userList = accountRepository.findAllByUsername(username);
        if (userList.isEmpty())
        {
            return false;
        }
        return true;
    }

    /**
     * @info Validates the username and passworld of new account canidates
     * @param account - The account object that is to be checked if it contains valid information
     * @return True if the account has a password over 4 characters and a non empty username.
     * @return False if the account has a password under 4 characters or a null username.
     */
    public boolean validateNewAccountDetails (Account account)
    {
        //Password minimuum length
        int passwordMinimum = 4;

        //Account information to be validated
        String accountUsername = account.getUsername();
        String accountPassword = account.getPassword();
        
        //Checks for empty username
        if (accountUsername == "" || accountUsername == null)
        {
            return false;
        }

        //Checks password validity
        if (accountPassword.length() < passwordMinimum)
        {
            return false;
        }

        return true;
    }

    /**
     * @info Validates if the login information matches the database login information
     * @param accountCanidate - The account login object that is to be checked against the account in the database
     * @return An optiona account that will contain the account details if the login matches
     * @return An optional account that will be null if the login detals dont match
     */
    public Optional <Account> userLogin(Account accountCanidate)
    {
        //Finds all accounts with the user name in the databse
        List<Account> userList = accountRepository.findAllByUsername(accountCanidate.getUsername());
        
        //There SHOULD only be one username but username does not have the @unique tag in the repository so I did this instad.
        for (Account user : userList)
        {
            //Checks login details against the database records
            if (user.getPassword().equals(accountCanidate.getPassword()))
            {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    /**
     * @info Validates if the account id exists in the database
     * @param id - The id of a account that is to be validated
     * @return True if the account is in the database
     * @return False if the account is not in the database
     */
    public boolean isExistingAccount(int id)
    {
        return accountRepository.findByAccountId(id).isPresent();
    }
}
