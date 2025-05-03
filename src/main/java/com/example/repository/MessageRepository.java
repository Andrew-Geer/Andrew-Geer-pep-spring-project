package com.example.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.entity.Message;


public interface MessageRepository extends JpaRepository<Message, Integer>{

    /**
     * @info Returns a list of all messages in the database
     * @return A List of all messages in the database
     */
    List<Message> findAll();

    /**
     * @info Returns a list of all messages in the database posted by one user
     * @param postedBy - The user id of the account that will be looked up
     * @return Returns a list of messages
     */
    List<Message> findAllByPostedBy(int postedBy);

    /**
     * @info Returns a specific message using its message id
     * @param id - The message id of the message to be retrived
     * @return One message.
     */
    Message findByMessageId(int id);

    /**
     * @info Deletes a specific message using its message id
     * @param id - The message id of the message to be deleated
     * @return int = an integer value representing the nuber of records deleted
     */
    int deleteByMessageId(int id);
    
    /**
     * @info Updates a specific message using its message id
     * @param text - The new text that is to be inserted
     * @param id - The message id of the message to be deleted
     * @return int = an integer value representing the nuber of records updated
     */
    @Modifying
    @Query("UPDATE Message m SET m.messageText = ?1 WHERE m.messageId = ?2" )
    int updateMessageTextByMessageId(String text, int id);
}
