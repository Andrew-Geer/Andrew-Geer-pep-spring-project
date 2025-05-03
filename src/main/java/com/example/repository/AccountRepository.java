package com.example.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.Account;

public interface AccountRepository extends JpaRepository<Account, Integer> {

    /**
     * @info Returns a list of all accounts with that username
     * @param username - The username of account(s)
     * @return A list of all users that have the username
     */
    List<Account> findAllByUsername(String username);

    /**
     * @info Returns an optional of Account based on the account ids in the database
     * @param id - The id of an account
     * @return Returns an optional of Account
     */
    Optional <Account> findByAccountId(int id);
}
