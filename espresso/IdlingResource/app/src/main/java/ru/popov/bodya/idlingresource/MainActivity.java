package ru.popov.bodya.idlingresource;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements TextLoader.OnLoadFinishedCallback {

    private static final int WHAT = 1;

    private ProgressBar progressBar;
    private TextView textView;
    private EditText editText;

    private TextLoader textLoader;

    @Nullable
    private TextIdlingResource idlingResource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textLoader = ((IdlingResourceSampleApplication) getApplication()).getTextLoader();
        textLoader.setListener(this);

        findViewById(R.id.changeTextButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String text = editText.getText().toString();
                if (v.getId() == R.id.changeTextButton) {
                    textView.setText(R.string.waiting_msg);
                    showProgress(true);
                    textLoader.queueTask(WHAT, text, idlingResource);
                }
            }
        });
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        textView = (TextView) findViewById(R.id.textToBeChanged);
        editText = (EditText) findViewById(R.id.editTextUserInput);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        textLoader.setListener(null);
    }

    @Override
    public void onDone(String text) {
        showProgress(false);
        textView.setText(text);
    }

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (idlingResource == null) {
            idlingResource = new TextIdlingResource();
        }
        return idlingResource;
    }

    private void showProgress(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }
}
