package com.example.pspchatclientandroidatr.model;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.example.pspchatclientandroidatr.model.dao.MessageDao;
import com.example.pspchatclientandroidatr.model.entity.Message;
import com.example.pspchatclientandroidatr.model.room.ChatDatabase;
import com.example.pspchatclientandroidatr.util.ThreadPool;
import com.example.pspchatclientandroidatr.util.callback.ServerJoinResponseListener;
import com.example.pspchatclientandroidatr.util.callback.UserListCallback;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Repository {

    private Context context;

    private String currentUsername = "";
    private String currentReceiver = "";

    private MessageDao messageDao;



    private static final String SERVER_ADDRESS = "10.0.2.2";
    private static final int SERVER_PORT = 5000;

    private static final String RESPONSE_JOIN_FAILED = "FAILED";
    private static final String RESPONSE_JOIN_SUCCESSFUL = "JOINED";
    private static final String DISCONNECT_CODE = "DISCONNECT";
    private static final String GET_USERS_CODE = "USERS";
    private static final String GLOBAL_MESSAGE_CODE = "GLOBAL";
    private static final String PRIVATE_MESSAGE_CODE = "PRIVATE";

    private static final String MESSAGE_DELIMITER = ":";

    private Socket client;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;

    private UserListCallback userListCallback;



    public Repository(Context context){
        this.context = context;
        ChatDatabase db = ChatDatabase.getDb(context);
        messageDao = db.getMessageDao();


    }




    public String getCurrentUsername(){ return currentUsername; }


    public LiveData<List<Message>> getLiveGlobalMessages(){
        return messageDao.getLiveGlobalMessages();
    }


    public LiveData<List<Message>> getLivePrivateMessages(String from){
        return messageDao.getLivePrivateMessages(from, currentUsername);
    }


    public void insertMessage(Message message){
        ThreadPool.threadExecutorPool.execute(new Runnable() {
            @Override
            public void run() {
                messageDao.insert(message);
            }
        });
    }



    public void deleteAllPrivateMessages(){
        ThreadPool.threadExecutorPool.execute(new Runnable() {
            @Override
            public void run() {
                messageDao.deleteAllPrivateMessages();
            }
        });
    }


    public String getCurrentReceiver() {
        return currentReceiver;
    }


    public void setUserListCallback(UserListCallback callback){ this.userListCallback = callback; }

    public void setCurrentReceiver(String currentReceiver) {
        this.currentReceiver = currentReceiver;
    }

    public void startChatService(String userName, String password, ServerJoinResponseListener listener){
        ThreadPool.threadExecutorPool.execute(new Runnable() {
            @Override
            public void run() {
                try{
                    client = new Socket(SERVER_ADDRESS, SERVER_PORT);

                    inputStream = new DataInputStream(client.getInputStream());
                    outputStream = new DataOutputStream(client.getOutputStream());

                    //Se envía el primer mensaje al servidor para establecer el nombre de usuario

                    String send = userName + MESSAGE_DELIMITER + password;
                    outputStream.writeUTF(send);


                    //El servidor responde si hay éxito o fallo al intentar unirse
                    String response = inputStream.readUTF();


                    Log.v("xyzyx", "RESPUESTA INICIAL = "+response);

                    if(response.equals(RESPONSE_JOIN_FAILED)){
                        //Nombre no válido o no se pudo conectar

                        listener.onFailed();

                        return;
                    }else if(response.equals(RESPONSE_JOIN_SUCCESSFUL)){
                        //Se conectó al servidor
                        currentUsername = userName;
                        listener.onJoined();
                        startListening();

                    }


                }catch (Exception ex){
                    listener.onFailed();
                }
            }
        });

    }



    private void startListening(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                String received = "";
                String[] split;
                Message tmpMessage = new Message();
                while(true){
                    try{

                        received = inputStream.readUTF();

                        Log.v("xyzyx", "RECIBIDO " + received);

                        split = received.split(MESSAGE_DELIMITER);


                        Log.v("xyzyx", "PRIMERA POSICION DE SPLIT " + split[0]);

                        if(split[0].equals(GLOBAL_MESSAGE_CODE)){
                            //From   , message
                            tmpMessage = new Message(split[1], split[2]);

                            long id = messageDao.insert(tmpMessage);
                            Log.v("xyzyx", "MENSAJE GLOBAL TEMPORAL = "+tmpMessage.toString());

                        }else if(split[0].equals(PRIVATE_MESSAGE_CODE)){

                            tmpMessage = new Message(split[1], split[2], split[3]);

                            long id = messageDao.insert(tmpMessage);

                        }else if(split[0].equals(GET_USERS_CODE)){
                            if(userListCallback != null){

                                List<String> users = new ArrayList<>();

                                if(split.length > 1){
                                    for (int i = 1; i < split.length; i++) {
                                        users.add(split[i]);
                                    }
                                }

                                userListCallback.onLoaded(users);

                            }
                        }





                    }catch(Exception ex){
                        Log.v("xyzyx", "MESSAGE FAILED");
                        ex.fillInStackTrace().printStackTrace();

                    }
                }
            }
        }).start();


    }


    public void disconnectUser(){
        ThreadPool.threadExecutorPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    outputStream.writeUTF(DISCONNECT_CODE);
                } catch (IOException e) { }
            }
        });
    }



    private void sendMessage(String message){
        ThreadPool.threadExecutorPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    outputStream.writeUTF(message);
                } catch (IOException e) { }
            }
        });
    }



    public void sendPrivateMessage(String receiver, String message){
        if(!(receiver.contains(":") || receiver.contains(";") || message.contains(":") || message.contains(";"))){
            sendMessage(PRIVATE_MESSAGE_CODE + MESSAGE_DELIMITER + currentUsername + MESSAGE_DELIMITER + receiver + MESSAGE_DELIMITER + message);
        }
    }


    public void sendGlobalMesssage(String message){
        if(!(message.contains(":") || message.contains(";"))){
            sendMessage(GLOBAL_MESSAGE_CODE + MESSAGE_DELIMITER + currentUsername + MESSAGE_DELIMITER + message);
        }
    }


    public void getUserList(UserListCallback callback){

        ThreadPool.threadExecutorPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    setUserListCallback(callback);
                    outputStream.writeUTF(GET_USERS_CODE);

                } catch (Exception e) {
                    callback.onFailure();
                }
            }
        });


    }


}
