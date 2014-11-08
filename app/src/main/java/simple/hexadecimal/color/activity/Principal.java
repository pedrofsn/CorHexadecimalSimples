package simple.hexadecimal.color.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TypefaceSpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import simple.hexadecimal.color.R;
import simple.hexadecimal.color.controller.Manipulador;
import simple.hexadecimal.color.fragments.MenuLateral;
import simple.hexadecimal.color.fragments.SelecaoCorHEX;
import simple.hexadecimal.color.interfaces.IControleDeCorSelecionada;
import simple.hexadecimal.color.interfaces.IMenuLateral;
import simple.hexadecimal.color.utils.Navegacao;
import yuku.ambilwarna.AmbilWarnaDialog;

public class Principal extends ActionBarActivity implements AmbilWarnaDialog.OnAmbilWarnaListener, IMenuLateral, IControleDeCorSelecionada {

    public static String ultimaCor = "vazio";
    public static boolean isTelaEmPe;
    public boolean primeiraAbertura = true;
    public SelecaoCorHEX fragmentSelecaoCor;
    private DrawerLayout mDrawerLayout;
    private RelativeLayout mRelativeLayoutMenuEsquerdo;
    private ActionBarDrawerToggle mDrawerToggle;
    private InterstitialAd interstitial;
    private MenuLateral fragmentoMenuLateral;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.principal);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.mDrawerLayout);
        mRelativeLayoutMenuEsquerdo = (RelativeLayout) findViewById(R.id.mRelativeLayoutMenuEsquerdo);

        if (Build.VERSION.SDK_INT >= 11) {
            SpannableString tituloEstilizadoActionBar = new SpannableString(getString(R.string.app_name));
            tituloEstilizadoActionBar.setSpan(new TypefaceSpan("sans-serif-light"), 0, tituloEstilizadoActionBar.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            getSupportActionBar().setTitle(tituloEstilizadoActionBar);
        }

        isTelaNaVertical(getResources().getConfiguration());
    }

    @Override
    protected void onStart() {
        super.onStart();

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_navigation_drawer, android.R.string.ok, android.R.string.cancel) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                supportInvalidateOptionsMenu();
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                supportInvalidateOptionsMenu();
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        if (primeiraAbertura) {

            mDrawerLayout.closeDrawer(mRelativeLayoutMenuEsquerdo);

            fragmentSelecaoCor = new SelecaoCorHEX();
            fragmentoMenuLateral = new MenuLateral();

            Navegacao.showFragment(fragmentSelecaoCor, getSupportFragmentManager(), SelecaoCorHEX.TAG_SELECAO_COR_HEX, R.id.content_frame);

            Navegacao.showFragment(fragmentoMenuLateral, getSupportFragmentManager(), MenuLateral.TAG_FRAGMENT_MENU_LADO_ESQUERDO, R.id.fragmentMenuLateral);

            primeiraAbertura = false;
        }

        manipulaBanner();
    }

    private void manipulaBanner() {
        // Criar o an�nncio intersticial
        interstitial = new InterstitialAd(this);
        interstitial.setAdUnitId("ca-app-pub-3875707990385821/7368830597");

        // Criar a solicita��o de an�ncio
        AdRequest adRequest = new AdRequest.Builder().build();

        // Iiniciar o carregamento do an�ncio intersticia.
        interstitial.loadAd(adRequest);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);

        isTelaNaVertical(newConfig);
    }

    private void isTelaNaVertical(Configuration orientacao) {
        if (orientacao.orientation == Configuration.ORIENTATION_PORTRAIT) {
            // em p�
            isTelaEmPe = true;
        } else {
            // deitado
            isTelaEmPe = false;
        }

        if (fragmentSelecaoCor != null)
            fragmentSelecaoCor.manipulaBanner();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        displayInterstitial();
        finish();
    }

    public void displayInterstitial() {
        if (interstitial.isLoaded())
            interstitial.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case R.id.colorPicker:
                new AmbilWarnaDialog(this, Manipulador.convertHexToInt(ultimaCor), this).show();
                break;
            case R.id.rate:
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
                }
                break;

            default:
                System.exit(0);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setBackgroundFromActivity(String cor) {
        if (fragmentSelecaoCor != null) {
            fragmentSelecaoCor.setBackgroundFromActivity(Manipulador.putHash(cor));
        }
        mDrawerLayout.closeDrawer(mRelativeLayoutMenuEsquerdo);
    }

    @Override
    public void getCorSelecionada(String hexColor) {
        if (ultimaCor.equals("vazio")) {
            ultimaCor = hexColor;
            setBackgroundFromActivity(hexColor);

        } else if (!hexColor.equals(ultimaCor)) {
            ultimaCor = hexColor;
            setBackgroundFromActivity(hexColor);

        }
    }

    @Override
    public void onCancel(AmbilWarnaDialog dialog) {
        dialog.getDialog().dismiss();
    }

    @Override
    public void onOk(AmbilWarnaDialog dialog, int color) {
        setBackgroundFromActivity(Manipulador.convertIntToHex(dialog.getColor()));
    }
}
