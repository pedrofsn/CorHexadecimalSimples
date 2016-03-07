package simple.hexadecimal.color.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import simple.hexadecimal.color.controller.Manipulador;
import simple.hexadecimal.color.interfaces.IBancoDeDados;
import simple.hexadecimal.color.model.Cor;

public class DataBaseHandler extends SQLiteOpenHelper implements IBancoDeDados {

    private static int DB_VERSAO = 1;
    private static String DB_NOME = "db";
    private static DataBaseHandler instance;

    private DataBaseHandler(Context context) {
        super(context, DB_NOME, null, DB_VERSAO);
    }

    public static DataBaseHandler getInstance(Context context) {
        if (instance == null) {
            instance = new DataBaseHandler(context);
        }
        return instance;
    }

    private int booleanToInt(boolean b) {
        if (b) {
            return 1;
        } else {
            return 0;
        }
    }

    private boolean intToBoolean(int i) {
        return i == 1;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // COR_FAVORITA
        db.execSQL("CREATE TABLE " + Cor.TABELA_COR_FAVORITADA + " (" + Cor._id + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL" + ", " + Cor.HEX_COR + " TEXT" + ", " + Cor.FAVORITADA + " INTENGER " + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // db.execSQL("DROP TABLE IF EXISTS " + Cor.TABELA_COR_FAVORITADA);
        onCreate(db);
    }

    @Override
    public void createCor(Cor modelo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Cor.HEX_COR, modelo.getHexColor());
        values.put(Cor.FAVORITADA, booleanToInt(modelo.isFavorito()));

        db.insert(Cor.TABELA_COR_FAVORITADA, null, values);

        db.close();
    }

    @Override
    public Cor readCor(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cor modelo = new Cor();

        Cursor cursor = db.query(Cor.TABELA_COR_FAVORITADA, new String[]{Cor.HEX_COR, Cor.FAVORITADA}, Cor._id + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();

            modelo.setHexColor(cursor.getString(cursor.getColumnIndex(Cor.HEX_COR)));
            modelo.setFavorito(intToBoolean(Integer.valueOf(cursor.getString(cursor.getColumnIndex(Cor.FAVORITADA)))));
        }
        return modelo;
    }

    @Override
    public Cor searchCor(String corPesquisada) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cor modelo = new Cor();

        Cursor cursor = db.query(Cor.TABELA_COR_FAVORITADA, new String[]{Cor._id, Cor.FAVORITADA}, Cor.HEX_COR + "=?", new String[]{corPesquisada}, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                modelo.setHexColor(corPesquisada);
                modelo.setFavorito(intToBoolean(Integer.valueOf(cursor.getString(cursor.getColumnIndex(Cor.FAVORITADA)))));
            } while (cursor.moveToNext());
        }

        return modelo;
    }

    @Override
    public boolean hasCor(String corPesquisada) {
        corPesquisada = Manipulador.putHash(corPesquisada);
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Cor.TABELA_COR_FAVORITADA, new String[]{Cor._id, Cor.FAVORITADA}, Cor.HEX_COR + "=?", new String[]{corPesquisada}, null, null, null, null);

        return cursor.moveToFirst();
    }

    @Override
    public List<Cor> readAllCor() {
        List<Cor> lista = new ArrayList<Cor>();

        String selectQuery = "SELECT * FROM " + Cor.TABELA_COR_FAVORITADA;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Cor modelo = new Cor();

                modelo.setHexColor(cursor.getString(cursor.getColumnIndex(Cor.HEX_COR)));

                modelo.setFavorito(intToBoolean(Integer.valueOf(cursor.getString(cursor.getColumnIndex(Cor.FAVORITADA)))));

                lista.add(modelo);
            } while (cursor.moveToNext());
        }

        return lista;
    }

    @Override
    public int updateCor(Cor modelo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Cor.FAVORITADA, booleanToInt(modelo.isFavorito()));

        return db.update(Cor.TABELA_COR_FAVORITADA, values, Cor.HEX_COR + " = ?", new String[]{String.valueOf(modelo.getHexColor())});
    }

    @Override
    public void deleteCor(String hex) {
        hex = Manipulador.putHash(hex);

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Cor.TABELA_COR_FAVORITADA, Cor.HEX_COR + " = ?", new String[]{String.valueOf(hex)});
        db.close();
    }

    @Override
    public int getCountCor() {
        SQLiteDatabase db = this.getWritableDatabase();
        return (int) db.compileStatement("SELECT count(*) FROM " + Cor.TABELA_COR_FAVORITADA).simpleQueryForLong();
    }

}