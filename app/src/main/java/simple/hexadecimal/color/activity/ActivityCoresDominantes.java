package simple.hexadecimal.color.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.File;

import simple.hexadecimal.color.R;
import simple.hexadecimal.color.domain.ActivityGeneric;
import simple.hexadecimal.color.fragments.FragmentCoresDominantes;
import simple.hexadecimal.color.utils.ImageIntentHandler;
import simple.hexadecimal.color.utils.ImageUtils;

/**
 * Created by pedro.sousa on 15/07/15.
 */
public class ActivityCoresDominantes extends ActivityGeneric {

    private ImageIntentHandler.ImagePair mImagePair;
    private FragmentCoresDominantes fragmentCoresDominantes = new FragmentCoresDominantes();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout, fragmentCoresDominantes, FragmentCoresDominantes.TAG)
                .commit();
    }

    @Override
    public void setLayout() {
        setContentView(R.layout.toolbar_com_framelayout);
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void capturarImagem(boolean camera) {
        if (camera) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File f = ImageUtils.createImageFile(ImageUtils.getPackageName(this));
            if ((f != null) && f.exists()) {
                mImagePair = new ImageIntentHandler.ImagePair(fragmentCoresDominantes.getImageView(), f.getAbsolutePath());
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                startActivityForResult(takePictureIntent, ImageIntentHandler.REQUEST_CAPTURE);
            } else {
                Toast.makeText(this, "Storage Error", Toast.LENGTH_LONG).show();
            }
        } else {
            mImagePair = new ImageIntentHandler.ImagePair(fragmentCoresDominantes.getImageView(), null);
            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, ImageIntentHandler.REQUEST_GALLERY);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ImageIntentHandler intentHandler =
                new ImageIntentHandler(this, mImagePair)
                        .folder("IIH Sample")
                        .sizeDp(200);
        intentHandler.handleIntent(requestCode, resultCode, data);
        fragmentCoresDominantes.computarCores();
    }
}