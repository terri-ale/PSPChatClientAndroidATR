package com.example.pspchatclientandroidatr.view.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.pspchatclientandroidatr.R;
import com.example.pspchatclientandroidatr.model.entity.Message;
import com.example.pspchatclientandroidatr.view.adapter.MessageAdapter;
import com.example.pspchatclientandroidatr.viewmodel.ViewModelActivity;

import java.util.ArrayList;
import java.util.List;


public class GlobalChatFragment extends Fragment implements View.OnClickListener {

    private List<Message> messageList = new ArrayList<>();

    private ViewModelActivity viewModel;

    private ImageButton btSend;
    private EditText etMessage;


    public GlobalChatFragment() { }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(getActivity()).get(ViewModelActivity.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_global_chat, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etMessage = view.findViewById(R.id.etMessage);
        btSend = view.findViewById(R.id.btSend);

        btSend.setOnClickListener(this);
        view.findViewById(R.id.imgListUsers).setOnClickListener(this);
        view.findViewById(R.id.imgBack).setOnClickListener(this);

        init();
    }

    private void init() {
        RecyclerView rvMessages = getView().findViewById(R.id.rvMessages);

        MessageAdapter adapter = new MessageAdapter(getActivity(), messageList);

        rvMessages.setAdapter(adapter);
        //rvMessages.setHasFixedSize(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rvMessages.setLayoutManager(linearLayoutManager);

        viewModel.getLiveGlobalMessages().observe(getActivity(), new Observer<List<Message>>() {
            @Override
            public void onChanged(List<Message> messages) {
                Log.v("xyzyx", "LISTA DE MENSAJES: " + messages.toString());
                messageList.clear();
                messageList.addAll(messages);
                adapter.notifyDataSetChanged();
                if(messages.size() > 1) rvMessages.smoothScrollToPosition(messages.size()-1);
            }
        });

    }



    private void sendMessage(){
        String message = etMessage.getText().toString();

        if(message.isEmpty() || message.contains(":") || message.contains(";")){
            Toast.makeText(getContext(), getContext().getString(R.string.error_invalid_characters), Toast.LENGTH_SHORT);
        }else{
            viewModel.sendGlobalMesssage(message);
            etMessage.setText("");
        }
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgBack:
                viewModel.disconnectUser();
                NavHostFragment.findNavController(GlobalChatFragment.this).popBackStack();
                break;

            case R.id.btSend:
                sendMessage();
                break;

            case R.id.imgListUsers:
                NavHostFragment.findNavController(GlobalChatFragment.this)
                        .navigate(R.id.action_globalChatFragment_to_userListFragment);
                break;
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        init();
    }
}