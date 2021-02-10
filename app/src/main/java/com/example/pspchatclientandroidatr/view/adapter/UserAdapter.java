package com.example.pspchatclientandroidatr.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pspchatclientandroidatr.R;
import com.example.pspchatclientandroidatr.model.entity.User;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<String> userList;
    private OnUserClickListener listener;

    public UserAdapter(List<String> users, OnUserClickListener listener){
        this.userList = users;
        this.listener = listener;
    }


    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserAdapter.UserViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return userList == null ? 0 : userList.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder{

        private View view;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
        }

        public void bind(int position){
            ((TextView) view.findViewById(R.id.tvName)).setText(userList.get(position));
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onUserClick(userList.get(position));
                }
            });
        }
    }
}
