package com.example.pspchatclientandroidatr.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.pspchatclientandroidatr.model.entity.Message;

import java.util.List;

@Dao
public interface MessageDao {


    @Insert
    long insert(Message message);


    //@Query("SELECT * FROM message WHERE sender = :sender AND receiver = :receiver")
    //LiveData<List<Message>> getLivePrivateMessages(String sender, String receiver);


    @Query("SELECT * FROM message WHERE receiver IS NULL")
    LiveData<List<Message>> getLiveGlobalMessages();


    @Query("SELECT * FROM message WHERE ((sender = :sender AND receiver = :receiver) OR (sender = :receiver AND receiver = :sender))")
    LiveData<List<Message>> getLivePrivateMessages(String sender, String receiver);


    @Query("DELETE FROM message WHERE receiver NOT NULL")
    int deleteAllPrivateMessages();





}
