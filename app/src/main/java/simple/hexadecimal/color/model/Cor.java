package simple.hexadecimal.color.model;

import simple.hexadecimal.color.controller.Manipulador;

public class Cor {
    public static final String TABELA_COR_FAVORITADA = "TABELA_COR_FAVORITADA";
    public static final String _id = "_id";
    public static final String HEX_COR = "HEX_COR";
    public static final String FAVORITADA = "FAVORITADA";

    private String hexColor;
    private boolean favorito;

    public Cor() {
    }

    public Cor(String hexColor, boolean favorito) {
        this.hexColor = Manipulador.putHash(hexColor);
        this.favorito = favorito;
    }

    public String getHexColor() {
        return hexColor;
    }

    public void setHexColor(String hexColor) {
        this.hexColor = hexColor;
    }

    public boolean isFavorito() {
        return favorito;
    }

    public void setFavorito(boolean favorito) {
        this.favorito = favorito;
    }
}
