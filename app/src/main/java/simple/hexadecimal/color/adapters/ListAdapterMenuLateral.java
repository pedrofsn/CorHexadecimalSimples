package simple.hexadecimal.color.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import simple.hexadecimal.color.R;
import simple.hexadecimal.color.controller.Manipulador;
import simple.hexadecimal.color.model.Cor;

public class ListAdapterMenuLateral extends BaseAdapter {

    private List<Cor> data;
    private Context context;

    public ListAdapterMenuLateral(Context context, List<Cor> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Cor getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Cor item = getItem(position);
        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = li.inflate(R.layout.adapter_cor_favorita, parent, false);

            holder = new ViewHolder();

            holder.hexColor = (TextView) convertView.findViewById(R.id.hexColor);
            holder.amostraCor = (LinearLayout) convertView.findViewById(R.id.amostraCor);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        setDataOnAdapter(holder, item);

        return convertView;
    }

    private void setDataOnAdapter(final ViewHolder holder, final Cor item) {
        holder.hexColor.setText(item.getHexColor());
        holder.amostraCor.setBackgroundColor(Manipulador.convertHexToInt(item.getHexColor()));
    }

    static class ViewHolder {
        TextView hexColor;
        LinearLayout amostraCor;
    }
}
