package com.example.cristian.mywallet;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Clase para gestionar la base de datos SQLite de la aplicación.
 * -------------------------------------------------------------
 * Para actualizar la estructura de la base de datos se debe registrar un
 * UPGRADE dentro de la clase. De este modo "version" deberá incrementarse
 * y, dentro del método onUpgrade, se declararán las modificaciones realizadas
 * sobre esta.
 */
public class WalletDBHelper extends SQLiteOpenHelper {

    private static int version = 1;
    private static String name = "WalletDB" ;
    private static CursorFactory factory = null;

    public WalletDBHelper(Context context){
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        Log.i(this.getClass().toString(), "Creando base de datos");
        db.execSQL("CREATE TABLE GASTOS( _id INTEGER PRIMARY KEY AUTOINCREMENT, gasto_concepto TEXT NOT NULL, gasto_descripcion TEXT, gasto_cantidad NUMERIC, gasto_localizacion TEXT, categoria TEXT)");
        db.execSQL("CREATE TABLE PRESUPUESTOS( _id INTEGER PRIMARY KEY AUTOINCREMENT, presupuesto NUMERIC, disponible NUMERIC)");
        db.execSQL("CREATE UNIQUE INDEX gasto_concepto ON GASTOS(gasto_concepto ASC)");
        Log.i(this.getClass().toString(), "Tabla GASTOS creada");
        Log.i(this.getClass().toString(), "Tabla PRESUPUESTOS creada");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
    }
}