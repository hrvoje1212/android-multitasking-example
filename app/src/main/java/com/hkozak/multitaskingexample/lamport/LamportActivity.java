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

import butterknife.Bind;
import butterknife.ButterKnife;

public class LamportActivity extends AppCompatActivity {

    @Bind(R.id.fab)
    FloatingActionButton fab;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.output_text)
    TextView outputTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lamport);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Threads starting", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                outputTextView.setText("");

                for (int i = 0; i < LamportRunnable.tables.length; i++)
                    LamportRunnable.tables[i] = -1;

                Thread thread0 = new Thread(new LamportRunnable(0, outputTextView));
                thread0.start();

                Thread thread1 = new Thread(new LamportRunnable(1, outputTextView));
                thread1.start();

                Thread thread2 = new Thread(new LamportRunnable(2, outputTextView));
                thread2.start();
            }
        });
    }

}
