package simple.hexadecimal.color.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.ClipboardManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import simple.hexadecimal.color.AdUnitId;
import simple.hexadecimal.color.R;
import simple.hexadecimal.color.activity.ActivityPrincipal;
import simple.hexadecimal.color.controller.Manipulador;
import simple.hexadecimal.color.dao.DataBaseHandler;
import simple.hexadecimal.color.model.Cor;

public class SelecaoCorHEX extends Fragment implements OnClickListener {

    private RelativeLayout conteudo;
    private TextView textViewHashtag;
    private EditText editText;
    private LinearLayout historico;
    private LinearLayout barraRodape;
    private HorizontalScrollView horizontalScrollView;
    private ImageView favoritar;
    private ImageView compartilhar;
    private ImageView copiar;

    private DataBaseHandler db;

    private int lastColorInt;

    private AdView adView;

    private static int getContrastColor(String hex) {
        double y = (299 * Manipulador.getRedFromHex(hex) + 587 * Manipulador.getGreenFromHex(hex) + 114 * Manipulador.getBlueFromHex(hex)) / 1000;
        return y >= 128 ? Color.BLACK : Color.WHITE;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_selecao_cor, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        conteudo = (RelativeLayout) view.findViewById(R.id.conteudo);
        editText = (EditText) view.findViewById(R.id.editText);
        textViewHashtag = (TextView) view.findViewById(R.id.textViewHashtag);
        historico = (LinearLayout) view.findViewById(R.id.historico);
        barraRodape = (LinearLayout) view.findViewById(R.id.barraRodape);
        horizontalScrollView = (HorizontalScrollView) view.findViewById(R.id.horizontalScrollView);
        favoritar = (ImageView) view.findViewById(R.id.favoritar);
        compartilhar = (ImageView) view.findViewById(R.id.compartilhar);
        copiar = (ImageView) view.findViewById(R.id.copiar);
    }

    @Override
    public void onStart() {
        super.onStart();
        db = DataBaseHandler.getInstance(getActivity());

        favoritar.setOnClickListener(this);
        copiar.setOnClickListener(this);
        compartilhar.setOnClickListener(this);

        manipulaBanner();

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence text, int start, int before, int count) {
                String hexSemEspacos = text.toString().replaceAll(" ", "");
                hexSemEspacos = hexSemEspacos.toUpperCase();
                setBackgroundColor(hexSemEspacos);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editText.setText("ABCDEF");
    }

    public void manipulaBanner() {
        if (adView != null) {
            adView.setVisibility(View.GONE);
            adView.destroy();
            barraRodape.removeView(adView);
            adView = null;
        }

        adView = new AdView(getActivity());
        adView.setAdUnitId(AdUnitId.ID);

        if (ActivityPrincipal.isTelaEmPe) {
            adView.setAdSize(AdSize.BANNER);
        } else {
            adView.setAdSize(AdSize.SMART_BANNER);
        }

        AdRequest adRequest = new AdRequest.Builder().build();

        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                adView.setVisibility(View.VISIBLE);
            }
        });

        barraRodape.addView(adView);
        adView.setVisibility(View.GONE);
        adView.loadAd(adRequest);
    }

    @Override
    public void onPause() {
        adView.pause();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        adView.resume();
    }

    @Override
    public void onDestroy() {
        adView.destroy();
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.favoritar:
                if (db.hasCor(getTextFromEditText())) {
                    favoritador(false);
                    db.deleteCor(getTextFromEditText());
                } else {
                    favoritador(true);
                    db.createCor(new Cor(getTextFromEditText(), true));
                }
                LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(new Intent(ActivityPrincipal.TAG_FRAGMENT_MENU_LADO_ESQUERDO));
                break;

            case R.id.compartilhar:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, getTextFromEditText());
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.compartilhar)));
                break;

            case R.id.copiar:
                ClipboardManager cm = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText(getTextFromEditText());

                Toast.makeText(getActivity(), "A cor " + getTextFromEditText() + " foi copiada com sucesso!", Toast.LENGTH_SHORT).show();
                break;
        }

        if (v instanceof ImageView) {
            String corClicada = Manipulador.convertIntToHex(Manipulador.convertViewColorToInt(v));
            editText.setText(corClicada.replace("#", ""));
            setBackgroundColor(corClicada);
        }
    }

    public void autoSmoothScroll() {
        horizontalScrollView.postDelayed(new Runnable() {
            @Override
            public void run() {
                horizontalScrollView.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
            }
        }, 100);
    }

    public void setBackgroundColor(String color) {
        favoritador(db.hasCor(color));

        int colorInt = Manipulador.convertHexToInt(color);

        if (lastColorInt != colorInt) {
            lastColorInt = colorInt;
            historico.addView(Manipulador.criaCorTemporaria(getActivity(), colorInt, this));

            conteudo.setBackgroundColor(colorInt);
            if (adView != null)
                adView.setBackgroundColor(colorInt);

            autoSmoothScroll();

            editText.setText(Manipulador.removeHash(color));

            int corInvertida = getContrastColor(color);
            editText.setTextColor(corInvertida);
            textViewHashtag.setTextColor(corInvertida);
            //((ActivityPrincipal) getActivity()).toolbar.setTitleTextColor(colorInt);

            ((ActivityPrincipal) getActivity()).getSupportActionBar().setBackgroundDrawable(new ColorDrawable(colorInt));
        }
    }

    private void favoritador(boolean valor) {
        if (valor)
            favoritar.setImageDrawable(getResources().getDrawable(R.drawable.favorito));
        else
            favoritar.setImageDrawable(getResources().getDrawable(R.drawable.nao_favorito));
    }

    private String getTextFromEditText() {
        return Manipulador.putHash(editText.getText().toString().trim());
    }
}
