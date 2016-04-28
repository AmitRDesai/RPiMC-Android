package com.rpimc.hari.rpimc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PowerManager;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by Hari on 10/16/2015.l
 */
public class Logo extends Activity {
    static Connection con;
    static PowerManager.WakeLock wakeLock;
    static boolean connected = false;
    public static final String TAG = "MSG";
    public static SharedPreferences getPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferenceManager.setDefaultValues(this, R.xml.prefs, false);
        setContentView(R.layout.initial_screen);
        PowerManager powerManager = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "My Lock");
        getPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        wakeLock.acquire();
        new Thread() {
            @Override
            public void run() {
                try {
                    ConnectionManager.initialize(getApplicationContext());
                    while (!SSHConnection.connect()) ;
                    String server_name = getPrefs.getString("server_name", null);
                    SSHConnection.send("sudo java -jar " + server_name + ";");
                    con = ConnectionManager.connect();
                    connected = true;
                    String trun = getPrefs.getString("turn", null);
                    CommandSender commandSender = new CommandSender(Logo.con);
                    commandSender.send(new Command("change", trun));
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    finish();
                    startActivity(new Intent(getApplicationContext(), MainMenu.class));
                }
            }
        }.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent prefintent = new Intent(this, SettingsActivity.class);
            startActivity(prefintent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
