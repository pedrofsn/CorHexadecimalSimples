package simple.hexadecimal.color.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.devspark.progressfragment.ProgressFragment;
import com.kimo.lib.alexei.Alexei;
import com.kimo.lib.alexei.Answer;
import com.kimo.lib.alexei.Utils;
import com.kimo.lib.alexei.calculus.ColorPaletteCalculus;

import java.util.List;

import simple.hexadecimal.color.R;
import simple.hexadecimal.color.activity.ActivityCoresDominantes;

/**
 * Created by Kimo on 8/19/14.
 */
public class FragmentCoresDominantes extends ProgressFragment {

    public static final String TAG = FragmentCoresDominantes.class.getSimpleName();

    private ImageView imageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cores_dominantes, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imageView = (ImageView) view.findViewById(R.id.imageView);
    }

    @Override
    public void onStart() {
        super.onStart();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ActivityCoresDominantes) getActivity()).capturarImagem(false);
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setContentShown(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        computarCores();

    }

    public void computarCores() {
        performCalculus(5);
    }

    private void performCalculus(int numberOfColors) {

        Alexei.with(getActivity())
                .analyze(imageView)
                .perform(new ColorPaletteCalculus(Utils.getBitmapFromImageView(imageView), numberOfColors))
                .showMe(new Answer<List<Integer>>() {
                    @Override
                    public void beforeExecution() {
                        setContentShown(false);
                    }

                    @Override
                    public void afterExecution(List<Integer> answer, long elapsedTime) {

                        try {
                            getFragmentManager().beginTransaction().replace(R.id.info_area, FragmentCoresDominantesResultado.newInstance((java.util.ArrayList<Integer>) answer, elapsedTime)).commit();
                            setContentShown(true);
                        } catch (NullPointerException e) {
                        }
                    }

                    @Override
                    public void ifFails(Exception error) {
                    }
                });
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }
}
