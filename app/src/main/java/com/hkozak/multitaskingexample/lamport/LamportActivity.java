package com.hkozak.multitaskingexample.lamport;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.hkozak.multitaskingexample.R;
import com.hkozak.multitaskingexample.dekker.DekkerRunnable;

public class LamportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lamport);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final TextView textView = (TextView) findViewById(R.id.output_text);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Threads starting", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                textView.setText("");

                for (int i = 0; i < LamportRunnable.tables.length; i++)
                    LamportRunnable.tables[i] = -1;

                Thread thread0 = new Thread(new LamportRunnable(0, textView));
                thread0.start();

                Thread thread1 = new Thread(new LamportRunnable(1, textView));
                thread1.start();

                Thread thread2 = new Thread(new LamportRunnable(2, textView));
                thread2.start();
            }
        });
    }

}
