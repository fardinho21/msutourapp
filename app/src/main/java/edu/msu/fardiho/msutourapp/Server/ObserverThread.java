package edu.msu.fardiho.msutourapp.Server;
import android.util.Log;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import edu.msu.fardiho.msutourapp.Server.ThreadCompleteListener;

public abstract class ObserverThread extends Thread {
    private final Set<ThreadCompleteListener> listeners =
            new CopyOnWriteArraySet<ThreadCompleteListener>();

    public final void addListener(final ThreadCompleteListener TH) {
        listeners.add(TH);
    }

    public final void removeListener(final ThreadCompleteListener TH) {
        listeners.remove(TH);
    }

    private final void notifyListeners() {
        for (ThreadCompleteListener TH : listeners) {
            TH.threadCompleteNotification(this);
        }
    }

    @Override
    public final void run() {
        try {
            doRun();
        } catch (Exception e) {
            Log.e("ERROR: ", e.toString());
        } finally {
            notifyListeners();
        }
    }
    public abstract void doRun();
}
