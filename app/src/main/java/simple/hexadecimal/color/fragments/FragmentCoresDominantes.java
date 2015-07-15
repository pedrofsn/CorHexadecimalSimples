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

import de.greenrobot.event.EventBus;
import simple.hexadecimal.color.R;
import simple.hexadecimal.color.domain.CalculateColorPaletteClicked;

/**
 * Created by Kimo on 8/19/14.
 */
public class FragmentCoresDominantes extends ProgressFragment {

    public static final String TAG = FragmentCoresDominantes.class.getSimpleName();

    private ImageView mImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cores_dominantes, container, false);
        configure(view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setContentShown(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
        EventBus.getDefault().post(new CalculateColorPaletteClicked(5));
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    public void onEventMainThread(CalculateColorPaletteClicked event) {
        performCalculus(event.getNumberOfColors());
    }

    private void configure(View view) {
        mImage = (ImageView) view.findViewById(R.id.img);
    }

    private void performCalculus(int numberOfColors) {

        Alexei.with(getActivity())
                .analyze(mImage)
                .perform(new ColorPaletteCalculus(Utils.getBitmapFromImageView(mImage), numberOfColors))
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
}
