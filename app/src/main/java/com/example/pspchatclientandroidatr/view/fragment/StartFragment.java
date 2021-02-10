package com.example.pspchatclientandroidatr.view.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.pspchatclientandroidatr.R;
import com.example.pspchatclientandroidatr.util.callback.ServerJoinResponseListener;
import com.example.pspchatclientandroidatr.viewmodel.ViewModelActivity;
import com.google.android.material.textfield.TextInputLayout;


public class StartFragment extends Fragment implements View.OnClickListener {

    private ViewModelActivity viewModel;

    private ProgressDialog progressDialog;
    private Button btJoin;
    private TextInputLayout tiUsername;
    private TextInputLayout tiPassword;


    private String userName = "";
    private String password = "";


    public StartFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(getActivity()).get(ViewModelActivity.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_start, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btJoin = view.findViewById(R.id.btJoin);
        tiUsername = view.findViewById(R.id.tiUsername);
        tiPassword = view.findViewById(R.id.tiPassword);

        btJoin.setOnClickListener(this);
    }


    private void attemptStartChat(){
        //viewModel.startChatService();
        if(checkFields()){
            btJoin.setOnClickListener(null);
            progressDialog = ProgressDialog.show(getContext(), getContext().getString(R.string.progress_loading), "", true, false);
            viewModel.startChatService(userName, password, new ServerJoinResponseListener() {
                @Override
                public void onJoined() {
                    progressDialog.dismiss();
                    NavHostFragment.findNavController(StartFragment.this)
                            .navigate(R.id.action_startFragment_to_globalChatFragment);
                }

                @Override
                public void onFailed() {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), R.string.error_join_failed, Toast.LENGTH_SHORT).show();
                            btJoin.setOnClickListener(StartFragment.this);
                        }
                    });

                }
            });
        }
    }


    private boolean checkFields(){

        userName = tiUsername.getEditText().getText().toString();
        password = tiPassword.getEditText().getText().toString();

        tiUsername.setErrorEnabled(false);
        tiPassword.setErrorEnabled(false);

        boolean check = true;

        if(userName.isEmpty()){
            check = false;
            tiUsername.setError(getContext().getString(R.string.error_empty_field));
        }

        if(userName.contains(":") || userName.contains(";")){
            check = false;
            tiUsername.setError(getContext().getString(R.string.error_invalid_characters));
        }

        if(password.isEmpty()){
            check = false;
            tiPassword.setError(getContext().getString(R.string.error_empty_field));
        }

        if(password.contains(":") || password.contains(";")){
            check = false;
            tiPassword.setError(getContext().getString(R.string.error_invalid_characters));
        }


        return check;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btJoin:
                attemptStartChat();
                break;
        }
    }
}