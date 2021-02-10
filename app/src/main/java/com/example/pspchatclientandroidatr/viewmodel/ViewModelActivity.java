package com.example.pspchatclientandroidatr.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.pspchatclientandroidatr.model.Repository;
import com.example.pspchatclientandroidatr.model.entity.Message;
import com.example.pspchatclientandroidatr.util.callback.ServerJoinResponseListener;
import com.example.pspchatclientandroidatr.util.callback.UserListCallback;

import java.util.List;

public class ViewModelActivity extends AndroidViewModel {

    private Repository repository;


    public ViewModelActivity(@NonNull Application application) {
        super(application);
        repository = new Repository(application);

    }

    public String getCurrentUsername() {
        return repository.getCurrentUsername();
    }

    public LiveData<List<Message>> getLiveGlobalMessages() {
        return repository.getLiveGlobalMessages();
    }

    public LiveData<List<Message>> getLivePrivateMessages(String from) {
        return repository.getLivePrivateMessages(from);
    }

    public void insertMessage(Message message) {
        repository.insertMessage(message);
    }

    public void deleteAllPrivateMessages() {
        repository.deleteAllPrivateMessages();
    }

    public void startChatService(String userName, String password, ServerJoinResponseListener listener) {
        repository.startChatService(userName, password, listener);
    }


    public void sendPrivateMessage(String receiver, String message) {
        repository.sendPrivateMessage(receiver, message);
    }

    public void sendGlobalMesssage(String message) {
        repository.sendGlobalMesssage(message);
    }

    public String getCurrentReceiver() {
        return repository.getCurrentReceiver();
    }

    public void setCurrentReceiver(String currentReceiver) {
        repository.setCurrentReceiver(currentReceiver);
    }

    public void getUserList(UserListCallback callback) {
        repository.getUserList(callback);
    }


    public void setUserListCallback(UserListCallback callback) {
        repository.setUserListCallback(callback);
    }

    public void disconnectUser() {
        repository.disconnectUser();
    }
}
