package simple.hexadecimal.color.controller;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Date;

public class Manipulador {

    public static String putHash(String color) {
        if (color.contains("#"))
            return color;
        else
            return "#".concat(color);
    }

    public static String removeHash(String color) {
        if (color.contains("#"))
            return color.replace("#", "");
        else
            return color;
    }

    public static String convertIntToHex(int valor) {
        return String.format("#%06X", (0xFFFFFF & valor));
    }

    public static int convertHexToInt(String hex) {
        return Color.parseColor(putHash(hex));
    }

    public static ImageView criaCorTemporaria(Context context, int hex, OnClickListener onClick) {

        int idGerado = generateID();

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(75, LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(4, 4, 4, 4);

        ImageView corTemp = new ImageView(context);
        corTemp.setLayoutParams(layoutParams);
        corTemp.setBackgroundColor(hex);
        corTemp.setOnClickListener(onClick);
        corTemp.setId(idGerado);
        corTemp.setTag("linearHistorico");

        return corTemp;
    }

    public static int convertViewColorToInt(View view) {
        ColorDrawable drawable = (ColorDrawable) view.getBackground();
        /*
        if (Build.VERSION.SDK_INT >= 11 && Build.VERSION.SDK_INT < 20) {
			return drawable.getColor();
		}
		* */
        try {
            Field field = drawable.getClass().getDeclaredField("mState");
            field.setAccessible(true);
            Object object = field.get(drawable);
            field = object.getClass().getDeclaredField("mUseColor");
            field.setAccessible(true);
            return field.getInt(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private static int generateID() {
        Date time = Calendar.getInstance().getTime();
        String id = time.getMinutes() + time.getSeconds() + "";
        return Integer.valueOf(id);
    }

    public static String convertHexToRGB(String hexColor) {
        int hex = convertHexToInt(hexColor);

        int r = (hex & 0xFF0000) >> 16;
        int g = (hex & 0xFF00) >> 8;
        int b = (hex & 0xFF);

        return "RGB(" + r + "," + g + "," + b + ")";

    }
}
