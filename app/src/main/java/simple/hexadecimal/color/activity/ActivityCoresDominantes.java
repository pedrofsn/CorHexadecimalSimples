package simple.hexadecimal.color.activity;

import android.os.Bundle;
import android.view.MenuItem;

import simple.hexadecimal.color.R;
import simple.hexadecimal.color.domain.ActivityGeneric;
import simple.hexadecimal.color.fragments.FragmentCoresDominantes;

/**
 * Created by pedro.sousa on 15/07/15.
 */
public class ActivityCoresDominantes extends ActivityGeneric {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frame_layout);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, new FragmentCoresDominantes(), FragmentCoresDominantes.TAG)
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}