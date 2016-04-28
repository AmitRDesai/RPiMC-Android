package com.rpimc.hari.rpimc;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class VirtualLine extends AppCompatActivity implements View.OnClickListener {

    public static
    Button send, clear;
    DrawingView dv;
    ArrayList<Line> lines;
    Paint paint;
    Handler handler;
    Thread executer;
    public static int scale = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_virtual_line);
        dv = (DrawingView) findViewById(R.id.view);
        send = (Button) findViewById(R.id.send);
        clear = (Button) findViewById(R.id.clear);
        send.setOnClickListener(this);
        clear.setOnClickListener(this);
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                int i = msg.arg1;
                if (i % 2 == 0)
                    paint.setColor(Color.BLUE);
                else
                    paint.setColor(Color.RED);
                dv.canvas.drawLine(lines.get(i).p1.x, lines.get(i).p1.y, lines.get(i).p2.x, lines.get(i).p2.y, paint);
                dv.invalidate();
            }
        };
        CommandSender commandSender = new CommandSender(Logo.con);
        commandSender.send(new Command("scale", ScaleDialog.scaleValue + ""));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        new MenuInflater(getApplication()).inflate(R.menu.virtual_line_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_scale:
                ScaleDialog scaleDialog = new ScaleDialog(this);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send:
                lines = dv.getLines();
                if (lines.size() <= 0) {
                    new MessageDialog(VirtualLine.this, "Message", "There is nothing to send").alertDialog.show();
                    return;
                }
                dv.reset();
                dv.canvas.drawColor(Color.WHITE);
                paint = new Paint();
                paint.setAntiAlias(true);
                paint.setDither(true);
                paint.setColor(Color.RED);
                paint.setStrokeWidth(10);
                final CommandSender commandSender = new CommandSender(Logo.con);
                executer = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < lines.size(); i++) {
                            Message msge = Message.obtain();
                            msge.arg1 = i;
                            handler.sendMessage(msge);
                        }
                        commandSender.send(new Command("v_line", lines));
                    }
                });
                executer.start();
                break;
            case R.id.clear:
                dv.reset();
                dv.canvas.drawColor(Color.WHITE);
                dv.invalidate();
                break;
        }
    }
}
