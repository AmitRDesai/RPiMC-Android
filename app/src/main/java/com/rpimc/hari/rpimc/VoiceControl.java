package com.rpimc.hari.rpimc;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class VoiceControl extends AppCompatActivity implements View.OnClickListener {

    ImageButton voice;
    Button add, show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_control);
        voice = (ImageButton) findViewById(R.id.voice_cmd);
        add = (Button) findViewById(R.id.voice_add);
        show = (Button) findViewById(R.id.voice_show);
        add.setOnClickListener(this);
        show.setOnClickListener(this);
        voice.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        new MenuInflater(getApplication()).inflate(R.menu.menu_voice, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.voice_stop_menu:
                CommandSender commandSender = new CommandSender(Logo.con);
                commandSender.send(new Command(0, 0, 0, 0, 1));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (requestCode == 8080) {
                String result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0);
                CommandSender commandSender = new CommandSender(Logo.con);
                result = result.toLowerCase();
                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                String forward=Logo.getPrefs.getString("forward","forward");
                String backward=Logo.getPrefs.getString("backward","backward");
                String left=Logo.getPrefs.getString("left","left");
                String right=Logo.getPrefs.getString("right","right");
                String stop=Logo.getPrefs.getString("stop","stop");
                if (result.equals(forward)) {
                    commandSender.send(new Command(1, 0, 0, 0, 0));
                } else if (result.equals(backward)) {
                    commandSender.send(new Command(0, 1, 0, 0, 0));
                } else if (result.equals(left)) {
                    commandSender.send(new Command(0, 0, 1, 0, 0));
                } else if (result.equals(right)) {
                    commandSender.send(new Command(0, 0, 0, 1, 0));
                } else if (result.equals(stop)) {
                    commandSender.send(new Command(0, 0, 0, 0, 1));
                } else {
                    CommandDB db = new CommandDB(this);
                    db.open();
                    ArrayList<Line> lines = db.getPath(result);
                    if (lines != null)
                        commandSender.send(new Command("v_line", lines));
                    else
                        Toast.makeText(getApplicationContext(), "Command not found", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.voice_cmd:
                Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Give Command");
                startActivityForResult(i, 8080);
                break;
            case R.id.voice_add:
                Intent intent = new Intent(this, AddVoiceCmd.class);
                startActivity(intent);
                break;
            case R.id.voice_show:
                Intent intent1 = new Intent(this, DbView.class);
                startActivity(intent1);
                break;
        }
    }
}
