package com.example.pspchatclientandroidatr.util.callback;

import com.example.pspchatclientandroidatr.model.entity.User;

import java.util.List;

public interface UserListCallback {
    public void onLoaded(List<String> userList);
    public void onFailure();
}
