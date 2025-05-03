package com.example.controller;

import java.util.Optional;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

        //Service objects for the controlers
        private final AccountService accountService;
        private final MessageService messageService;

        //Constrctor for spring dependecy injection
        public SocialMediaController(AccountService accountService, MessageService messageService) {
            this.accountService = accountService;
            this.messageService = messageService;
        }

        // -- Start of Account Controlers
        // -- These should be in differnet files but it may interfear with test units.

        /**
         * @info An post endpoint to create a new account in the database
         * @param account An account canidate that is to be added into the databas
         * @return A responce entity contating the status and account object that was registered
         */
        @PostMapping("/register")
        public ResponseEntity<Account> registerUser(@RequestBody Account account)
        {
            //Checks if the account username and password are valid
            if (!accountService.validateNewAccountDetails(account))
            {
                //Returns a null body and an cleint error 400 status
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }

            //Checks if the username is unique
            if (accountService.isExistingUsername(account.getUsername()))
            {
                //Returns a null body and an conflict 409 status
                return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
            }

            //Saves the account to the database
            account = accountService.createAccount(account);

            //Returns the account with its generated id and an ok 200 status
            return ResponseEntity.status(HttpStatus.OK).body(account);
        }

        /**
         * @info An post endpoint to login to an account
         * @param account An account canidate that is to be checked if the details are correct
         * @return A responce entity contating the status and account object that was loged into.
         */
        @PostMapping("/login")
        public ResponseEntity<Account> userLogin(@RequestBody Account account)
        {
            //Attempts to login to an account
            Optional<Account> loginAccount = accountService.userLogin(account);

            //Checks if the account was successfully loged into
            if (loginAccount.isEmpty())
            {
                //Returns a null body and an Unautorized 401 status
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }
            //Returns the account that was loged into and an ok 200 status
            return ResponseEntity.status(HttpStatus.OK).body(loginAccount.get());
        }
        // -- End of account controlers

        // -- Start of Message Controlers
        // -- These should be in differnet files but it may interfear with test units.

        /**
         * @info An post endpoint to create a new message
         * @param message An message canidate that is to be created
         * @return A responce entity contating the status and message object that was created
         */
        @PostMapping("/messages")
        public ResponseEntity<Message> postMessage(@RequestBody Message message)
        {
            //Validates the message text
            if (! messageService.isValidMessageText(message.getMessageText()))
            {
                //Returns a null body and an Client error 400 status
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }

            //Validates an existing user
            if (! accountService.isExistingAccount(message.getPostedBy()))
            {
                //Returns a null body and an Client error 400 status
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }

            //Saves the message
            message = messageService.saveMessage(message);

            //Returns a message with its generated id and an ok 200 status
            return ResponseEntity.status(HttpStatus.OK).body(message);
        }

        /**
         * 
         * @return A responce entity contating all messages in the database
         */
        @GetMapping("/messages")
        public ResponseEntity<List<Message>> getAllMessages()
        {
            return ResponseEntity.status(HttpStatus.OK).body(messageService.getAllMessages());
        }

        /**
         * @pathparam accountId the Id of an account to retrive messages from
         * @return A responce entity contating all messages in the database that were posted by a user
         */
        @GetMapping("/accounts/{accountId}/messages")
        public ResponseEntity<List<Message>> getAllUserMessages(@PathVariable(name = "accountId", required = true) int accountId)
        {
            return ResponseEntity.status(HttpStatus.OK).body(messageService.getAllMessagesByUserId(accountId));
        }

        /**
         * 
         * @param messageId - the id of a message that is to be returned
         * @return -- A responce entity contating A message with a matching id
         */
        @GetMapping("/messages/{messageId}")
        public ResponseEntity<Message> getMessageById(@PathVariable(name = "messageId", required = true) int messageId)
        {
            return ResponseEntity.status(HttpStatus.OK).body(messageService.getMessageById(messageId));
        }

        /**
         * 
         * @param messageId - the id of a message that is to be deleted
         * @return - A responce entity contating the status and ammount of updated records
         */
        @DeleteMapping("/messages/{messageId}")
        public ResponseEntity<Integer> deleteMessageById(@PathVariable(name = "messageId", required = true) int messageId)
        {
            //Deleats the records and stores the number of deleated records
            int rowsDeleated = messageService.deleteMessageById(messageId);

            //Checks if the number of deleated records was above 0
            if (rowsDeleated > 0)
            {
                //Returns a null body and an ok 200 status
                return ResponseEntity.status(HttpStatus.OK).body(rowsDeleated);
            }

            //Returns an integer of the number of deleated records and an ok 200 status
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }

        /**
         * 
         * @param messageId the id of the message that is to be changed
         * @param message the text of a message that is to be updated
         * @return A responce entity contating the status and ammount of updated records
         */
        @PatchMapping("/messages/{messageId}")
        public ResponseEntity<Integer> updateMessageTextById(@PathVariable(name = "messageId", required = true) int messageId, @RequestBody Message message)
        {
            //Validates the new text that is to be inserted
            if (! messageService.isValidMessageText(message.getMessageText()))
            {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }

            //Validates that the message id already exists in the database
            if (! messageService.isExistingMessage(messageId))
            {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }

            //Updates the records and stores the number of updated records
            int recordsUpdated = messageService.updateMessageTextById(messageId, message);

            //Checks if the number of updated records was over 0
            if (recordsUpdated > 0)
            {
                //Returns a null body and an ok 200 status
                return ResponseEntity.status(HttpStatus.OK).body(recordsUpdated);
            }
            //Returns an integer of the number of updated records and an ok 200 status
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }

        // -- End of message Controlers
}


