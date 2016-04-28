package com.rpimc.hari.rpimc;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * Created by Amit on 11-Mar-16.
 */
public class ScaleDialog {
    Context context;
    SeekBar bar;
    TextView textView;
    static int scaleValue = 3;

    public ScaleDialog(final Context context) {
        this.context = context;
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);

        bar = new SeekBar(context);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(550, ViewGroup.LayoutParams.WRAP_CONTENT);
        bar.setLayoutParams(params);
        bar.setMax(10);
        bar.setProgress(scaleValue);

        textView = new TextView(context);
        textView.setTextSize(20);
        textView.setText(bar.getProgress() + "");

        bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textView.setText(progress + "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        linearLayout.addView(bar);
        linearLayout.addView(textView);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Scale");
        builder.setView(linearLayout);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                scaleValue = bar.getProgress();
                CommandSender commandSender = new CommandSender(Logo.con);
                commandSender.send(new Command("scale", scaleValue + ""));
                Log.i(Logo.TAG, scaleValue + "");
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
