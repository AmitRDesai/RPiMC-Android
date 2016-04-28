package com.rpimc.hari.rpimc;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class AddVoiceCmd extends AppCompatActivity implements View.OnClickListener {

    DrawingView dv;
    Button add, clear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_voice_cmd);
        dv = (DrawingView) findViewById(R.id.view);
        add = (Button) findViewById(R.id.avc_add);
        clear = (Button) findViewById(R.id.avc_clear);
        add.setOnClickListener(this);
        clear.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.avc_add:
                if (dv.getLines().size() <= 0) {
                    new MessageDialog(AddVoiceCmd.this, "Message", "Please draw path for new voice command").alertDialog.show();
                    return;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Command Name");
                final EditText input = new EditText(this);
                builder.setView(input);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String cmd_name = input.getText().toString();
                        if (cmd_name != null) {
                            CommandDB commandDB = new CommandDB(getApplicationContext());
                            commandDB.open();
                            commandDB.createEntry(cmd_name, dv.getLines());
                            Toast.makeText(getApplicationContext(), "Successfully added", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                dialog.show();
                break;
            case R.id.avc_clear:
                dv.reset();
                dv.canvas.drawColor(Color.WHITE);
                dv.invalidate();
                break;
        }
    }
}
