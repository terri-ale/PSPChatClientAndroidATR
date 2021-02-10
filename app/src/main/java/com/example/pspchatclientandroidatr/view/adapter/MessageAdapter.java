package com.example.pspchatclientandroidatr.view.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pspchatclientandroidatr.R;
import com.example.pspchatclientandroidatr.model.entity.Message;
import com.example.pspchatclientandroidatr.viewmodel.ViewModelActivity;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private ViewModelActivity viewModel;
    private List<Message> messageList;

    public MessageAdapter(Activity activity, List<Message> messages){
        messageList = messages;
        viewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(ViewModelActivity.class);

    }


    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MessageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return messageList == null ? 0 : messageList.size();
    }




    public class MessageViewHolder extends RecyclerView.ViewHolder{

        private View view;


        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
        }


        public void bind(int position){

            if(viewModel.getCurrentUsername().equals(messageList.get(position).getSender())){
                view.findViewById(R.id.constraintSent).setVisibility(View.VISIBLE);
                ((TextView)view.findViewById(R.id.tvSentMessage)).setText(messageList.get(position).getText());

            }else{
                view.findViewById(R.id.constraintReceived).setVisibility(View.VISIBLE);
                ((TextView)view.findViewById(R.id.tvReceivedMessage)).setText(messageList.get(position).getText());
                ((TextView)view.findViewById(R.id.tvFrom)).setText(messageList.get(position).getSender());
            }

        }

    }
}
