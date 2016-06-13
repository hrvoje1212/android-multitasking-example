package com.hkozak.multitaskingexample.dekker;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.hkozak.multitaskingexample.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DekkerActivity extends AppCompatActivity {

    @Bind(R.id.fab)
    FloatingActionButton fab;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.output_text)
    TextView outputTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dekker);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Threads starting", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                outputTextView.setText("");

                Thread thread1 = new Thread(new DekkerRunnable(0, outputTextView));
                thread1.start();

                Thread thread2 = new Thread(new DekkerRunnable(1, outputTextView));
                thread2.start();
            }
        });
    }

}
