package com.example.cristian.mywallet;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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
        db.execSQL("CREATE TABLE PRESUPUESTOS( _id INTEGER PRIMARY KEY AUTOINCREMENT, presupuesto NUMERIC)");
        db.execSQL("CREATE UNIQUE INDEX gasto_concepto ON GASTOS(gasto_concepto ASC)" );
        Log.i(this.getClass().toString(), "Tabla GASTOS creada");

        // Insertamos datos iniciales
       /* db.execSQL("INSERT INTO GASTOS(_id, gasto_concepto, gasto_descripcion, gasto_cantidad) VALUES(1,'Gasto ejemplo 1', 'Descripcion de ejemplo para el gasto 1', 10)");
        db.execSQL("INSERT INTO GASTOS(_id, gasto_concepto, gasto_descripcion, gasto_cantidad) VALUES(2,'Gasto ejemplo 2', 'Descripcion de ejemplo para el gasto 2', 20)");
        db.execSQL("INSERT INTO GASTOS(_id, gasto_concepto, gasto_descripcion, gasto_cantidad) VALUES(3,'Gasto ejemplo 3', 'Descripcion de ejemplo para el gasto 3', 30)");
        db.execSQL("INSERT INTO GASTOS(_id, gasto_concepto, gasto_descripcion, gasto_cantidad) VALUES(4,'Gasto ejemplo 4', 'Descripcion de ejemplo para el gasto 4', 40)");
        db.execSQL("INSERT INTO GASTOS(_id, gasto_concepto, gasto_descripcion, gasto_cantidad) VALUES(5,'Gasto ejemplo 5', 'Descripcion de ejemplo para el gasto 5', 50)");
        */Log.i(this.getClass().toString(), "Base de datos creada");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }
}