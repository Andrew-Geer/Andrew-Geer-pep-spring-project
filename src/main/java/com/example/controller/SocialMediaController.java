package com.example.controller;

import java.util.Optional;

import org.springframework.ui.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;



/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */

@Controller
public class SocialMediaController {

        private final AccountService accountService;
        private final MessageService messageService;

        public SocialMediaController(AccountService accountService, MessageService messageService) {
            this.accountService = accountService;
            this.messageService = messageService;
        }

        // -- Start of Account Controlers
        // -- These should be in differnet files but it may interfear with test units.
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

        @PostMapping("/login")
        public ResponseEntity<Account> userLogin(@RequestBody Account account)
        {

            Optional<Account> loginAccount = accountService.userLogin(account);
            if (loginAccount.isEmpty())
            {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }
            return ResponseEntity.status(HttpStatus.OK).body(loginAccount.get());
        }
        // -- End of account controlers

        // -- Start of Message Controlers
        // -- These should be in differnet files but it may interfear with test units.

        @PostMapping("/messages")
        public ResponseEntity<Message> postMessage(@RequestBody Message message)
        {
            if (! messageService.isValidMessageText(message.getMessageText()))
            {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }

            if (! accountService.isExistingAccount(message.getPostedBy()))
            {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }

            message = messageService.saveMessage(message);
            return ResponseEntity.status(HttpStatus.OK).body(message);
        }

        // -- End of message Controlers
}


