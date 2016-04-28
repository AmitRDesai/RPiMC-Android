package com.rpimc.hari.rpimc;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.net.ConnectException;

public class MainMenu extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemClickListener {

    ListView listView;
    String classes[] = {"RemoteControl", "VoiceControl", "MotionControl", "VirtualLine"};
    DrawerLayout drawer;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.main_list);
        listView.setAdapter(new MyAdapter(getApplicationContext()));
        listView.setOnItemClickListener(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            drawer.openDrawer(GravityCompat.START);
        }
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_exit:
                try {
                    CommandSender commandSender = new CommandSender(Logo.con);
                    commandSender.send(new Command("exit", ""));
                    SSHConnection.disconnect();
                    Logo.wakeLock.release();
                    Logo.con.close();
                    if (Logo.getPrefs.getBoolean("stop_host", true))
                        ConnectionManager.disconnect(this);
                    finish();
                } catch (Exception e) {
                    finish();
                }
                break;
            case R.id.nav_shutdown:
                SSHConnection.send("sudo shutdown now;");
                break;
            case R.id.nav_add:
                Intent intent = new Intent(this, AddVoiceCmd.class);
                startActivity(intent);
                break;
            case R.id.nav_con_st:
                showConnectionDialog();
                break;
            case R.id.nav_settings:
                Intent prefintent = new Intent(this, SettingsActivity.class);
                startActivity(prefintent);
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showConnectionDialog() {
        String buttonText = "";
        final TextView textView = new TextView(this);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(25);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Connecting...");

        if (Logo.connected) {
            buttonText = "Disconnect";
            textView.setText("Connected");
        } else {
            buttonText = "Connect";
            textView.setText("Disconnected");
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Connection Status");
        builder.setView(textView);

        builder.setPositiveButton(buttonText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!Logo.connected) {
                    textView.setText("Connecting...");
                    Logo.connected = true;
                    new ConnectionStatus().execute();
                } else {
                    Logo.connected = false;
                    CommandSender commandSender = new CommandSender(Logo.con);
                    commandSender.send(new Command("exit", ""));
                    Logo.con.close();
                    SSHConnection.disconnect();
                    //dialog.cancel();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String select = classes[position];
        Class<?> name = null;
        try {
            name = Class.forName("com.rpimc.hari.rpimc." + select);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            Intent open = new Intent(this, name);
            startActivity(open);
        }

    }

    class MyAdapter extends BaseAdapter {
        Context context;
        int[] image = {R.mipmap.ic_remote_control, R.mipmap.ic_voice_control, R.mipmap.ic_motion_control, R.mipmap.ic_virtual_line};
        String[] name = {"Remote Control", "Voice Control", "Motion Control", "Virtual Line Follower"};

        public MyAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return name.length;
        }

        @Override
        public Object getItem(int position) {
            return name[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = null;
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater)
                        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.list_adapter, parent, false);
            } else {
                row = convertView;
            }
            TextView tv = (TextView) row.findViewById(R.id.tView);
            ImageView iv = (ImageView) row.findViewById(R.id.iV1);
            tv.setText(name[position]);
            iv.setImageResource(image[position]);
            return row;
        }
    }

    class ConnectionStatus extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.cancel();
        }

        @Override
        protected String doInBackground(String... params) {
            ConnectionManager.initialize(getApplicationContext());
            while (!SSHConnection.connect()) ;
            SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            String server_name = getPrefs.getString("server_name", null);
            SSHConnection.send("sudo java -jar " + server_name + ";");
            Logo.con = ConnectionManager.connect();
            return null;
        }
    }
}
