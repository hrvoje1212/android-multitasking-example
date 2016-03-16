package com.hkozak.multitaskingexample.lamport;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;

import java.util.Random;

/**
 * Created by Hrvoje Kozak on 15/03/16.
 */
public class LamportRunnable implements Runnable {

    public static volatile int THREADS_NUMBER = 3;
    public static volatile int TABLES_NUMBER = 5;
    public static volatile boolean[] entering = new boolean[THREADS_NUMBER];
    public static volatile int[] number = new int[THREADS_NUMBER];
    public static volatile int[] tables = new int[TABLES_NUMBER];

    private int id;

    private TextView outputTextView;

    public LamportRunnable(int id, TextView outputTextView) {
        this.id = id;
        this.outputTextView = outputTextView;
    }

    public void setOutputTextView(TextView outputTextView) {
        this.outputTextView = outputTextView;
    }

    @Override
    public void run() {

        try {
            while (true) {
                // sleep for a second
                Thread.sleep(1000);

                // check if there are free tables
                if (allTablesBooked()) {
                    Log.e("ASDF", "Thread: " + id + " leaving, all tables booked! " + getTablesStatus());
                    break;
                }

                // pick a random table
                final int wantedTable = (int)(Math.random() * TABLES_NUMBER);

                Log.e("ASDF", "Thread: " + id + ", table picked: " + wantedTable);

                // update UI thread with data
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        outputTextView.append("Thread: " + id + ", table picked: " + wantedTable + "\n");
                    }
                });

                enterCriticalSection();

                bookTable(wantedTable);

                exitCriticalSection();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void bookTable(final int wantedTable) {

        // if table is free, reserve it
        if (tables[wantedTable] == -1) {
            tables[wantedTable] = id;

            // update UI thread with data
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    outputTextView.append("Thread: " + id + ", table booked: " + wantedTable
                            + ", status: " + getTablesStatus() + "\n");
                }
            });
        }
        else {
            // update UI thread with data
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    outputTextView.append("Thread: " + id + ", failed booking of table " + wantedTable
                            + ", status: " + getTablesStatus() + "\n");
                }
            });
        }
    }

    private String getTablesStatus() {
        String status = "";

        for (int ownerID : tables) {
            if (ownerID == -1) status += '-';
            else status += Integer.toString(ownerID);
        }

        return status;
    }

    private boolean allTablesBooked() {

        for (int threadID : tables) {
            if (threadID == -1) return false;
        }

        return true;
    }

    private int getMaxNumber() {
        int maxNumber = -1;

        for (int n : number) {
            maxNumber = Math.max(maxNumber, n);
        }

        return maxNumber;
    }

    private void enterCriticalSection() throws InterruptedException {
        entering[id] = true;
        number[id] = getMaxNumber() + 1;
        entering[id] = false;

        for (int j = 0; j < THREADS_NUMBER; j++) {
            // wait until thread receives its number
            while (entering[j]) {
                Thread.sleep(1000);
            }

            // Wait until all threads with smaller numbers or with the same
            // number, but with higher priority, finish their work:
            while ((number[j] != 0) && (number[j] < number[id] || (number[j] == number[id] && j < id))) {
                Thread.sleep(1000);
            }
        }
    }

    private void exitCriticalSection() {
        number[id] = 0;
    }

}
