package com.example.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import com.example.entity.Message;
import com.example.repository.MessageRepository;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    public MessageService (MessageRepository messageRepository)
    {
        this.messageRepository = messageRepository;
    }

    public Message saveMessage (Message message)
    {
        messageRepository.save(message);
        return message;
    }

    public boolean isValidMessageText (String textCanidate)
    {
        final int textLenghtLimit = 255;
        if (textCanidate.length() > textLenghtLimit)
        {
            return false;
        }
        if (textCanidate == "" || textCanidate == null)
        {
            return false;
        }
        return true;
    }

    public List<Message> getAllMessages()
    {
        return messageRepository.findAll();
    }
}
