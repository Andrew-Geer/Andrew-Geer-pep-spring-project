package com.example.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;
import com.example.entity.Message;
import com.example.repository.MessageRepository;

@Service
public class MessageService {

    //Message repoistory variable
    private final MessageRepository messageRepository;

    //Constrctor for spring dependecy injection
    public MessageService (MessageRepository messageRepository)
    {
        this.messageRepository = messageRepository;
    }

    /**
     * @info Saves a message to the database
     * @param message - The message to be saved to the database (Id will be generarted)
     * @return A message object with the generated id
     */
    public Message saveMessage (Message message)
    {
        messageRepository.save(message);
        return message;
    }

    /**
     * @info Validates the text feild of a message object
     * @param textCanidate - The text string of a message that is to be validated
     * @return True if the message is not null and under 255 characters]
     * @return False if the message is null or over 255 characters
     */
    public boolean isValidMessageText(String textCanidate)
    {
        //Maximum length of text that can be accepted
        final int textLenghtLimit = 255;
        if (textCanidate.length() > textLenghtLimit)
        {
            return false;
        }
        //Verifies text is not null
        if (textCanidate == "" || textCanidate == null)
        {
            return false;
        }
        return true;
    }

    /**
     * @info Validates if the message id exists in the database
     * @param id - The id of a message that is to be validated
     * @return True if the message is in the database
     * @return False if the message is not in the database
     */
    public boolean isExistingMessage(int id)
    {
        return messageRepository.findByMessageId(id) != null;
    }

    /**
     * @info Retrives all messages from the database
     * @return A list of all messages in the database
     */
    public List<Message> getAllMessages()
    {
        return messageRepository.findAll();
    }

    /**
     * @info Retrives all messages from the database posted by a specific user
     * @return A list of all messages posted by a specific user
     */
    public List<Message> getAllMessagesByUserId(int id)
    {
        return messageRepository.findAllByPostedBy(id);
    }

    /**
     * @info Retrives a message from the databse by its id
     * @return A message with matching id
     */
    public Message getMessageById(int id)
    {
        return messageRepository.findByMessageId(id);
    }

    /**
     * @info Updates an existing message in the database
     * @param id - The id of a message that is to be changed
     * @param message - a message object that is to be updated
     * @return an integer value of the ammount of records updated
     */
    @Transactional
    public int updateMessageTextById(int id, Message message)
    {
        return messageRepository.deleteByMessageId(id);
    }

    /**
     * @info Deletes an existing message from the database
     * @param id - The id of a message that is to be deleted
     * @return an integer value of the ammount of records deleted
     */
    @Transactional
    public int deleteMessageById(int id)
    {
        return messageRepository.deleteByMessageId(id);
    }
}