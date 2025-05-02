package com.example.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import com.example.entity.Account;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.service.AccountService;

import org.springframework.ui.*;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */

@Controller
public class SocialMediaController {

        private final AccountService accountService;
        //private final AccountService accountService;

        public SocialMediaController(AccountService accountService) {
            this.accountService = accountService;
        }

        @PostMapping("/register")
        public ResponseEntity<Account> registerUser(@RequestBody Account account)
        {
            if (!accountService.validateNewAccountDetails(account))
            {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }

            if (accountService.isExistingUsername(account.getUsername()))
            {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
            }

            account = accountService.createAccount(account);

            return ResponseEntity.status(HttpStatus.OK).body(account);
        }
}


