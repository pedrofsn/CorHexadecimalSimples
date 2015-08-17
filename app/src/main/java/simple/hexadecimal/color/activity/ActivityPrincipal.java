package simple.hexadecimal.color.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import simple.hexadecimal.color.AdUnitId;
import simple.hexadecimal.color.R;
import simple.hexadecimal.color.adapters.ListAdapterMenuLateral;
import simple.hexadecimal.color.controller.Manipulador;
import simple.hexadecimal.color.dao.DataBaseHandler;
import simple.hexadecimal.color.domain.ActivityGeneric;
import simple.hexadecimal.color.fragments.SelecaoCorHEX;
import yuku.ambilwarna.AmbilWarnaDialog;

public class ActivityPrincipal extends ActivityGeneric {

    public static String TAG_FRAGMENT_MENU_LADO_ESQUERDO = ActivityPrincipal.class.getCanonicalName();

    public static String ultimaCor = "";
    public static boolean isTelaEmPe;
    public ListAdapterMenuLateral adapterListaMenuLateral;
    public SelecaoCorHEX fragmentSelecaoCor;

    private ListView listaFavorita;
    //// daqui pra cima é do menu lateral
    private AtualizadorMenuLateral atualizador = new AtualizadorMenuLateral();
    private LocalBroadcastManager localBroadCast;
    private InterstitialAd interstitial;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        manipulaBanner();

        configurarViews();

        isTelaNaVertical(getResources().getConfiguration());

        fragmentSelecaoCor = new SelecaoCorHEX();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragmentSelecaoCor);
        fragmentTransaction.commit();
    }

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_principal);
    }

    @Override
    protected void onStart() {
        super.onStart();
        localBroadCast = LocalBroadcastManager.getInstance(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        localBroadCast.unregisterReceiver(atualizador);
    }

    @Override
    public void onResume() {
        super.onResume();
        localBroadCast.registerReceiver(atualizador, new IntentFilter(TAG_FRAGMENT_MENU_LADO_ESQUERDO));

        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(ActivityPrincipal.TAG_FRAGMENT_MENU_LADO_ESQUERDO));
    }

    private void configurarViews() {
        listaFavorita = (ListView) findViewById(R.id.listaFavorita);
        listaFavorita.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String corSelecionada = ((TextView) view.findViewById(R.id.hexColor)).getText().toString();
                setBackgroundColorFromActivity(corSelecionada, Manipulador.convertHexToInt(corSelecionada));
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });
        // menu lateral ^

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, getToolbar(), R.string.app_name, R.string.app_name) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    private void manipulaBanner() {
        interstitial = new InterstitialAd(this);
        interstitial.setAdUnitId(AdUnitId.ID);

        AdRequest adRequest = new AdRequest.Builder().build();

        interstitial.loadAd(adRequest);
    }

    private void isTelaNaVertical(Configuration orientacao) {
        if (orientacao.orientation == Configuration.ORIENTATION_PORTRAIT) {
            isTelaEmPe = true;
        } else {
            isTelaEmPe = false;
        }

        if (fragmentSelecaoCor != null)
            fragmentSelecaoCor.manipulaBanner();
    }

    @Override
    public void onBackPressed() {
        displayInterstitial();
        finish();
        super.onBackPressed();
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
        switch (item.getItemId()) {
            case R.id.image:
                startActivity(new Intent(this, ActivityCoresDominantes.class));
                break;
            case R.id.colorPicker:
                new AmbilWarnaDialog(this, Manipulador.convertHexToInt(ultimaCor), new AmbilWarnaDialog.OnAmbilWarnaListener() {
                    @Override
                    public void onCancel(AmbilWarnaDialog dialog) {
                        dialog.getDialog().dismiss();
                    }

                    @Override
                    public void onOk(AmbilWarnaDialog dialog, int color) {
                        setBackgroundColorFromActivity(Manipulador.convertIntToHex(color), color);
                    }
                }).show();

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

    public void setBackgroundColorFromActivity(String hex, int cor) {
        if (fragmentSelecaoCor != null && hex != null) {
            fragmentSelecaoCor.setBackgroundColor(Manipulador.putHash(hex), cor);
        }
    }

    public class AtualizadorMenuLateral extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            adapterListaMenuLateral = new ListAdapterMenuLateral(ActivityPrincipal.this, DataBaseHandler.getInstance(ActivityPrincipal.this).readAllCor());
            listaFavorita.setAdapter(adapterListaMenuLateral);
        }

    }
}
