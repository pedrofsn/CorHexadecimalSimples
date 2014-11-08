package simple.hexadecimal.color.fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import simple.hexadecimal.color.R;
import simple.hexadecimal.color.adapters.ListAdapterMenuLateral;
import simple.hexadecimal.color.dao.DataBaseHandler;
import simple.hexadecimal.color.interfaces.IMenuLateral;

public class MenuLateral extends Fragment implements OnItemClickListener {

    public static String TAG_FRAGMENT_MENU_LADO_ESQUERDO = "TAG_FRAGMENT_MENU_LADO_ESQUERDO";
    public ListAdapterMenuLateral adapterListaMenuLateral;
    private ListView listaFavorita;
    private AtualizadorMenuLateral atualizador = new AtualizadorMenuLateral();

    private IMenuLateral mCallback;

    private LocalBroadcastManager localBroadCast;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_menu_lateral, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listaFavorita = (ListView) view.findViewById(R.id.listaFavorita);
    }

    @Override
    public void onStart() {
        super.onStart();

        localBroadCast = LocalBroadcastManager.getInstance(getActivity());

        adapterListaMenuLateral = new ListAdapterMenuLateral(getActivity(), DataBaseHandler.getInstance(getActivity()).readAllCor());
        listaFavorita.setAdapter(adapterListaMenuLateral);
        listaFavorita.setOnItemClickListener(this);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallback = (IMenuLateral) activity;
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
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TextView textView = (TextView) view.findViewById(R.id.hexColor);
        mCallback.setBackgroundFromActivity(textView.getText().toString());
    }

    public class AtualizadorMenuLateral extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            adapterListaMenuLateral = new ListAdapterMenuLateral(getActivity(), DataBaseHandler.getInstance(getActivity()).readAllCor());
            listaFavorita.setAdapter(adapterListaMenuLateral);
        }

    }

}
