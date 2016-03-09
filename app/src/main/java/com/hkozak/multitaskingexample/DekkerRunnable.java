package com.hkozak.multitaskingexample;

import android.os.Looper;
import android.util.Log;
import android.os.Handler;
import android.widget.TextView;

/**
 * Created by Hrvoje Kozak on 09/03/16.
 */
public class DekkerRunnable implements Runnable {

    public static volatile int turn = 0;
    public static volatile boolean[] wantsToEnter = {false, false};

    private int id;
    private int k;
    private int m;

    private TextView outputTextView;

    public DekkerRunnable(int id, TextView outputTextView) {
        this.id = id;
        this.outputTextView = outputTextView;
    }

    public void setOutputTextView(TextView outputTextView) {
        this.outputTextView = outputTextView;
    }

    @Override
    public void run() {

        for (k = 1; k <= 5; k++) {
            enterCriticalSection();
            print(5);
            exitCriticalSection();

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void print(int times) {
        for (m = 1; m <= times; m++) {
            Log.e("" + Thread.currentThread().getId(),
                    "Thread: " + id + ", C.S. NO: " + k + " (" + m + "/5)");

            // update UI thread with data
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    outputTextView.append("Thread: " + id + ", C.S. NO: " + k + " (" + m + "/5)\n");
                    if (m == 5)
                        outputTextView.append("\n");
                }
            });

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }



    private void enterCriticalSection() {

        wantsToEnter[id] = true;

        while (wantsToEnter[(id+1)%2]) {
            if (turn != id) {

                wantsToEnter[id] = false;
                while (turn != id) {
                    // busy wait
                    try {
                        Thread.sleep(230);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                wantsToEnter[id] = true;
            }
        }

    }

    private void exitCriticalSection() {
        turn = (id + 1) % 2;
        wantsToEnter[id] = false;
    }

}
