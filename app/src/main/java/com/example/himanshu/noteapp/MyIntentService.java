package com.example.himanshu.noteapp;

import android.app.IntentService;
import android.content.Intent;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class MyIntentService extends IntentService {

    public static final String EXTRA_COURSE_ID = "com.example.himanshu.noteapp.extra.COURSE_ID";

    public MyIntentService() {
        super("MyIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {



        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */

}
