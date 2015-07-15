package simple.hexadecimal.color.activity;

import android.os.Bundle;

import simple.hexadecimal.color.R;
import simple.hexadecimal.color.domain.ActivityGeneric;
import simple.hexadecimal.color.fragments.ColorPaletteFragment;

/**
 * Created by pedro.sousa on 15/07/15.
 */
public class ActivityCoresDominantes extends ActivityGeneric {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.principal);



        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, new ColorPaletteFragment(), ColorPaletteFragment.TAG)
                .commit();
    }
}