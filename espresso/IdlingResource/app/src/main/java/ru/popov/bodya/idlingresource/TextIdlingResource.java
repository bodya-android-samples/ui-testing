package ru.popov.bodya.idlingresource;


import android.support.annotation.Nullable;
import android.support.test.espresso.IdlingResource;
import android.util.Log;

import java.util.concurrent.atomic.AtomicBoolean;

public class TextIdlingResource implements IdlingResource {

    @Nullable
    private volatile ResourceCallback callback;

    private AtomicBoolean isIdleNow = new AtomicBoolean(true);

    @Override
    public String getName() {
        return this.getClass().getName();
    }

    @Override
    public boolean isIdleNow() {
        Log.e("TextIdlingResource", "usIdleNow = " + isIdleNow.get());
        return isIdleNow.get();
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        this.callback = callback;
    }

    void setIdleState(boolean isIdleNow) {
        this.isIdleNow.set(isIdleNow);
        Log.e("TextIdlingResource", "setIdleState = " + callback);
        if (isIdleNow && callback != null) {
            callback.onTransitionToIdle();
        }
    }
}