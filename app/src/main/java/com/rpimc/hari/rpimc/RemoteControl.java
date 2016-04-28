package com.rpimc.hari.rpimc;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

public class RemoteControl extends AppCompatActivity implements View.OnTouchListener {

    Button forward, backward, left, right;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote__control);
        forward = (Button) findViewById(R.id.bforwad);
        forward.setOnTouchListener(this);
        backward = (Button) findViewById(R.id.bbackword);
        backward.setOnTouchListener(this);
        left = (Button) findViewById(R.id.bleft);
        left.setOnTouchListener(this);
        right = (Button) findViewById(R.id.bright);
        right.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        CommandSender commandSender = new CommandSender(Logo.con);
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            switch (v.getId()) {
                case R.id.bforwad:
                    commandSender.send(new Command(1, 0, 0, 0, 0));
                    break;
                case R.id.bbackword:
                    commandSender.send(new Command(0, 1, 0, 0, 0));
                    break;
                case R.id.bleft:
                    commandSender.send(new Command(0, 0, 1, 0, 0));
                    break;
                case R.id.bright:
                    commandSender.send(new Command(0, 0, 0, 1, 0));
                    break;
            }
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            commandSender.send(new Command(0, 0, 0, 0, 1));
        }
        return false;
    }
}
