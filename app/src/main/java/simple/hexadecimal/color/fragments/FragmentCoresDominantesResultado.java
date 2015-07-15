package simple.hexadecimal.color.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.ClipboardManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kimo.lib.alexei.calculus.ColorPaletteCalculus;

import java.util.ArrayList;
import java.util.List;

import simple.hexadecimal.color.R;
import simple.hexadecimal.color.controller.Manipulador;

/**
 * Created by Kimo on 8/29/14.
 */
public class FragmentCoresDominantesResultado extends Fragment {

    public static final String TAG = ColorPaletteCalculus.class.getSimpleName();

    public static final String PALETTE = TAG + ".PALETTE";
    public static final String ELAPSED_TIME = TAG + ".ELAPSED_TIME";

    private List<Integer> mPalette;
    private long mElapsedTime;

    public FragmentCoresDominantesResultado() {
    }

    public static FragmentCoresDominantesResultado newInstance(ArrayList<Integer> palette, long elapsedTime) {
        FragmentCoresDominantesResultado fragment = new FragmentCoresDominantesResultado();

        Bundle args = new Bundle();
        args.putIntegerArrayList(PALETTE, palette);
        args.putLong(ELAPSED_TIME, elapsedTime);

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mPalette = getArguments().getIntegerArrayList(PALETTE);
            mElapsedTime = getArguments().getLong(ELAPSED_TIME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cores_dominantes_resultado, container, false);
        configure(view);
        return view;
    }

    private void configure(View view) {
        LinearLayout palleteContainer = (LinearLayout) view.findViewById(R.id.palette_container);
        TextView elapsedTime = (TextView) view.findViewById(R.id.elapsed_time);

        fillPalleteColors(mPalette, palleteContainer);
        elapsedTime.setText(new StringBuilder().append(mElapsedTime).append(" milliseconds"));
    }

    private void fillPalleteColors(List<Integer> colors, LinearLayout paletteContainer) {

        LayoutInflater inflater = getActivity().getLayoutInflater();
        for (final int color : colors) {

            View palleteColor = inflater.inflate(R.layout.item_pallete, paletteContainer, false);
            palleteColor.setBackgroundColor(color);
            palleteColor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClipboardManager cm = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                    String corHexadecimal = Manipulador.convertIntToHex(color);
                    cm.setText(corHexadecimal);

                    Toast.makeText(getActivity(), "A cor " + corHexadecimal + " foi copiada com sucesso!", Toast.LENGTH_SHORT).show();
                }
            });
            paletteContainer.addView(palleteColor);
        }
    }
}
