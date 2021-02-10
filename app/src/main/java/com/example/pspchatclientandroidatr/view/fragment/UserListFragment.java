package com.example.pspchatclientandroidatr.view.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pspchatclientandroidatr.R;
import com.example.pspchatclientandroidatr.util.callback.UserListCallback;
import com.example.pspchatclientandroidatr.view.adapter.OnUserClickListener;
import com.example.pspchatclientandroidatr.view.adapter.UserAdapter;
import com.example.pspchatclientandroidatr.viewmodel.ViewModelActivity;

import java.util.ArrayList;
import java.util.List;


public class UserListFragment extends Fragment implements View.OnClickListener {

    private ViewModelActivity viewModel;

    private UserAdapter adapter;

    private List<String> users = new ArrayList<>();


    public UserListFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(getActivity()).get(ViewModelActivity.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.imgBack).setOnClickListener(this);

        init();
    }

    private void init() {
        RecyclerView rvUserList = getView().findViewById(R.id.rvUserList);

        adapter = new UserAdapter(users, new OnUserClickListener() {
            @Override
            public void onUserClick(String user) {
                viewModel.setCurrentReceiver(user);
                NavHostFragment.findNavController(UserListFragment.this)
                        .navigate(R.id.action_userListFragment_to_privateChatFragment);
            }
        });

        rvUserList.setAdapter(adapter);
        rvUserList.setHasFixedSize(false);
        rvUserList.setLayoutManager(new LinearLayoutManager(getActivity()));
        loadUsers();
    }



    private void loadUsers(){


        viewModel.getUserList(new UserListCallback() {
            @Override
            public void onLoaded(List<String> userList) {
                users.clear();
                users.addAll(userList);
                adapter.notifyDataSetChanged();

                viewModel.setUserListCallback(null);
            }

            @Override
            public void onFailure() {

            }
        });


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgBack:
                NavHostFragment.findNavController(UserListFragment.this).popBackStack();
                break;
        }
    }
}