package com.example.pspchatclientandroidatr.model.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.pspchatclientandroidatr.model.dao.MessageDao;
import com.example.pspchatclientandroidatr.model.entity.Message;

@Database(entities = {Message.class}, version = 1, exportSchema = false)
public abstract class ChatDatabase extends RoomDatabase {

    public abstract MessageDao getMessageDao();

    private volatile static ChatDatabase INSTANCE;

    public static synchronized ChatDatabase getDb(final Context context) {
        if(INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    ChatDatabase.class, "dbchat.sqlite")
                    .build();
        }
        return INSTANCE;
    }
}
