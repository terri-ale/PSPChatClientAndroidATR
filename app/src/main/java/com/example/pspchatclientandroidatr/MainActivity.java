package com.example.pspchatclientandroidatr;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.v("xyzyx", "ON CREATE");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v("xyzyx", "ON DESTROY");
    }


    @Override
    protected void onPause() {
        super.onPause();
        Log.v("xyzyx", "ON PAUSE");
    }


    @Override
    protected void onStop() {
        super.onStop();
        Log.v("xyzyx", "ON STOP");
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.v("xyzyx", "ON RESUME");
    }
}