package ru.popov.bodya.idlingresource;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

import java.lang.ref.WeakReference;
import java.util.Random;

public class TextLoader extends HandlerThread {

    private static final String LOADER_NAME = TextLoader.class.getSimpleName();
    private static final String STRING_KEY = "string_key";
    private static final int LOWER_VALUE = 3000;
    private static final int UPPER_VALUE = 5000;

    private Handler loaderHandler;
    private WeakReference<OnLoadFinishedCallback> callbackWeakReference = new WeakReference<>(null);

    interface OnLoadFinishedCallback {
        void onDone(String text);
    }

    TextLoader() {
        super(LOADER_NAME);
    }

    synchronized void setListener(OnLoadFinishedCallback callback) {
        callbackWeakReference = new WeakReference<>(callback);
    }

    @Override
    protected void onLooperPrepared() {
        super.onLooperPrepared();
        prepareHandler();
    }

    void queueTask(int what, String string, TextIdlingResource idlingResource) {
        if (idlingResource != null) {
            idlingResource.setIdleState(false);
        }
        Message message = loaderHandler.obtainMessage(what, idlingResource);
        Bundle bundle = new Bundle();
        bundle.putString(STRING_KEY, string);
        message.setData(bundle);
        message.sendToTarget();
    }

    private void prepareHandler() {
        loaderHandler = new Handler(getLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                String data = msg.getData().getString(STRING_KEY);
                final String result = reverseString(data);
                final TextIdlingResource idlingResource = (TextIdlingResource) msg.obj;

                long randomValue = LOWER_VALUE + (long)(new Random().nextDouble()*(UPPER_VALUE - LOWER_VALUE));
                Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        OnLoadFinishedCallback callback = callbackWeakReference.get();
                        if (callback != null) {
                            callback.onDone(result);
                            if (idlingResource != null) {
                                idlingResource.setIdleState(true);
                            }
                        }
                    }
                }, randomValue);
                return true;
            }
        });
    }

    private String reverseString(String data) {
        StringBuilder sb = new StringBuilder(data);
        return sb.reverse().toString();
    }
}
