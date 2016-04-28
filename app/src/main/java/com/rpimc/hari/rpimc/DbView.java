package com.rpimc.hari.rpimc;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Amit on 20-Mar-16.
 */
public class DbView extends AppCompatActivity implements AdapterView.OnItemLongClickListener {

    public ListView listView;
    public CommandDB commandDB;
    ArrayList<String> cmds;
    ArrayList<ArrayList<Line>> paths;
    MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.db_view);
        commandDB = new CommandDB(getApplicationContext());
        commandDB.open();
        listView = (ListView) findViewById(R.id.db_listView);
        adapter = new MyAdapter(getApplicationContext());
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(this);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        commandDB.deleteEntry(cmds.get(position));
        Toast.makeText(getApplicationContext(), cmds.get(position) + " is deleted", Toast.LENGTH_SHORT).show();
        cmds.remove(position);
        paths.remove(position);
        adapter.notifyDataSetChanged();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        new MenuInflater(getApplication()).inflate(R.menu.dbview_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.dbview_build_in:
                Intent intent = new Intent(DbView.this, VoiceSettingsActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    class MyAdapter extends BaseAdapter {
        Context context;


        public MyAdapter(Context context) {
            this.context = context;
            cmds = commandDB.getAllCommand();
            paths = commandDB.getAllPath();
        }

        @Override
        public int getCount() {
            return cmds.size();
        }

        @Override
        public Object getItem(int position) {
            return cmds.get(position);
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
                row = inflater.inflate(R.layout.db_view_adapter, parent, false);
            } else {
                row = convertView;
            }
            TextView tv = (TextView) row.findViewById(R.id.db_cmd);
            DisplayView dv = (DisplayView) row.findViewById(R.id.db_view);
            tv.setText(cmds.get(position));
            dv.setLines(paths.get(position));
            dv.invalidate();
            return row;
        }
    }
}
