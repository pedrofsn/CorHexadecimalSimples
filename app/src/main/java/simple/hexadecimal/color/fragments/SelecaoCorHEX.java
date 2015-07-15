package simple.hexadecimal.color.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import simple.hexadecimal.color.AdUnitId;
import simple.hexadecimal.color.R;
import simple.hexadecimal.color.activity.Principal;
import simple.hexadecimal.color.controller.Manipulador;
import simple.hexadecimal.color.dao.DataBaseHandler;
import simple.hexadecimal.color.interfaces.IControleDeCorSelecionada;
import simple.hexadecimal.color.model.Cor;

public class SelecaoCorHEX extends Fragment implements OnClickListener {

    public static String TAG_SELECAO_COR_HEX = "TAG_SELECAO_COR_HEX";

    private RelativeLayout conteudo;
    private EditText editText;
    private LinearLayout historico;
    private LinearLayout barraRodape;
    private HorizontalScrollView horizontalScrollView;
    private ImageView favoritar;
    private ImageView compartilhar;
    private ImageView copiar;

    private IControleDeCorSelecionada mCallback;

    private DataBaseHandler db;
    private InputMethodManager teclado;

    private int lastColorInt;

    private boolean apiMaiorIgual11;

    private AdView adView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        apiMaiorIgual11 = Build.VERSION.SDK_INT >= 11;
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
        teclado = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

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
                // Remove espaços
                String hexSemEspacos = text.toString().replaceAll(" ", "");
                hexSemEspacos = hexSemEspacos.toUpperCase();

                if (!text.toString().equals(hexSemEspacos)) {
                    editText.setText(hexSemEspacos);
                    // editText.setSelection(hexSemEspacos.length());
                }

                if (editText.getText().toString().trim() != null && editText.getText().toString().trim().length() == 6) {
                    teclado.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                    mCallback.getCorSelecionada(getTextFromEditText());
                }
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

        if (Principal.isTelaEmPe) {
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
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallback = (IControleDeCorSelecionada) activity;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.favoritar:
                // Não existe no banco
                if (db.isCor(getTextFromEditText())) {
                    favoritador(false);
                    db.deleteCor(getTextFromEditText());
                    // Existe no banco
                } else {
                    favoritador(true);
                    db.createCor(new Cor(getTextFromEditText(), true));
                }
                LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(new Intent(MenuLateral.TAG_FRAGMENT_MENU_LADO_ESQUERDO));
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
            setBackgroundFromActivity(corClicada);
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

    public void setBackgroundFromActivity(String color) {
        favoritador(db.isCor(color));

        int colorInt = Manipulador.convertHexToInt(color);

        if (lastColorInt != colorInt) {
            lastColorInt = colorInt;
            historico.addView(Manipulador.criaCorTemporaria(getActivity(), colorInt, this));

            conteudo.setBackgroundColor(colorInt);
            if (adView != null)
                adView.setBackgroundColor(colorInt);

            autoSmoothScroll();

            editText.setText(Manipulador.removeHash(color));

            //if (apiMaiorIgual11)
            //getActivity().getActionBar().setBackgroundDrawable(new ColorDrawable(colorInt));
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
