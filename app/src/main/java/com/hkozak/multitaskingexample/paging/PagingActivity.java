package com.hkozak.multitaskingexample.paging;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.hkozak.multitaskingexample.R;
import com.hkozak.multitaskingexample.lamport.LamportRunnable;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PagingActivity extends AppCompatActivity {

    public static final int PAGES_NUMBER = 8;
    @Bind(R.id.frames_input) EditText framesET;
    @Bind(R.id.requests_input) EditText requestsET;
    @Bind(R.id.fab) FloatingActionButton fab;
    @Bind(R.id.output_text) TextView outputTextView;
    @Bind(R.id.toolbar) Toolbar toolbar;

    private List<Integer> requests;
    private List<Integer> frameOrder;

    private int framesNumber;
    private int requestsNumber;

    private List<Integer> frames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paging);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
    }

    @OnClick(R.id.fab)
    public void startPagingExample(View fab) {
        Snackbar.make(fab, "Starting Paging Example", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();

        getParameters();

        requests = getRandomRequests();
        frames = new ArrayList<>(Collections.nCopies(framesNumber, 0));
        frameOrder = new ArrayList<>(Collections.nCopies(framesNumber, 0));

        outputTextView.setText("requests: ");

        for (int request : requests) {
            outputTextView.append(String.format("%3d", request));
        }

        outputTextView.append("\nframes: ");

        for (int frame : frames) {
            outputTextView.append(String.format("%3d", frame));
        }

        outputTextView.append("\n");

        int orderCounter = 1;

        for (int page : requests) {

            outputTextView.append(page + " | ");

            int minOrderIndex = 0;
            for (int i = 0; i < frames.size(); i++) {

                minOrderIndex =
                        frameOrder.get(i) < frameOrder.get(minOrderIndex) ? i : minOrderIndex;

                if (frames.get(i) == page) {
                    minOrderIndex = i;
                    break;
                }
            }

            if (frames.get(minOrderIndex) == page) {
                printFramesStatus(page);
                outputTextView.append(" #bingo!\n");
                continue;
            }


            frames.set(minOrderIndex, page);
            frameOrder.set(minOrderIndex, orderCounter++);

            printFramesStatus(page);

            outputTextView.append("\n");
        }
    }

    private void printFramesStatus(int page) {

        for (int frame : frames) {
            if (frame == page) {
                String t = String.format("[%d]", frame);
                outputTextView.append(String.format("%5s", t));
            }
            else
                outputTextView.append(String.format("%5d", frame));
        }
    }

    private void getParameters() {
        try {
            requestsNumber = Integer.parseInt(requestsET.getText().toString());
            framesNumber = Integer.parseInt(framesET.getText().toString());
        } catch(NumberFormatException e) {
            Snackbar.make(fab, "Error parsing integers", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();

        }

        Log.e("TAG", requestsNumber + " " + framesNumber);


    }

    private List<Integer> getRandomRequests() {
        List<Integer> requests = new ArrayList<>();

        for (int i = 0; i < requestsNumber; i++) {
            Random random = new Random();
            requests.add(random.nextInt(PAGES_NUMBER) + 1);
        }
        return requests;
    }
}
