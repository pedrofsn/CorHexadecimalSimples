package simple.hexadecimal.color.domain;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TypefaceSpan;

import simple.hexadecimal.color.R;

/**
 * Created by pedro.sousa on 15/07/15.
 */
public abstract class ActivityGeneric extends AppCompatActivity {

    private Toolbar toolbar;

    public abstract void setLayout();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayout();
        setUpToolbar();
    }

    private void setUpToolbar() {
        try {
            toolbar = (Toolbar) findViewById(R.id.toolbar);

            SpannableString tituloEstilizadoActionBar = new SpannableString(getString(R.string.app_name));
            tituloEstilizadoActionBar.setSpan(new TypefaceSpan("sans-serif-light"), 0, tituloEstilizadoActionBar.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            toolbar.setTitle(tituloEstilizadoActionBar);

            setSupportActionBar(toolbar);
        } catch (Exception e) {
            throw new RuntimeException("A toolbar não foi encontrada no seu XML!");
        }
    }

    public Toolbar getToolbar() {
        return toolbar;
    }
}
