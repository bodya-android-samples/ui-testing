package ru.popov.bodya.idlingresource;


import android.app.Application;

public class IdlingResourceSampleApplication extends Application {

    private TextLoader textLoader;

    @Override
    public void onCreate() {
        super.onCreate();
        textLoader = new TextLoader();
        textLoader.start();
    }

    public TextLoader getTextLoader() {
        return textLoader;
    }
}
