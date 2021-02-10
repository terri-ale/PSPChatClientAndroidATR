package com.example.pspchatclientandroidatr.model.entity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "message")
public class Message {

    @PrimaryKey(autoGenerate = true)
    private long id;


    @NonNull
    @ColumnInfo(name = "sender")
    private String sender;


    @Nullable
    @ColumnInfo(name = "receiver")
    private String receiver;


    @NonNull
    @ColumnInfo(name = "text")
    private String text;


    public Message(){}


    //MENSAJES PRIVADOS
    public Message(@NonNull String sender, @Nullable String receiver, @NonNull String text) {
        this.sender = sender;
        this.receiver = receiver;
        this.text = text;
    }


    //MENSAJES PUBLICOS
    public Message(@NonNull String sender, @NonNull String text) {
        this.sender = sender;
        this.receiver = null;
        this.text = text;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @NonNull
    public String getSender() {
        return sender;
    }

    public void setSender(@NonNull String sender) {
        this.sender = sender;
    }

    @Nullable
    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(@Nullable String receiver) {
        this.receiver = receiver;
    }

    @NonNull
    public String getText() {
        return text;
    }

    public void setText(@NonNull String text) {
        this.text = text;
    }


    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", sender='" + sender + '\'' +
                ", receiver='" + receiver + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
