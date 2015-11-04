package com.example.cristian.mywallet;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by David on 03/11/2015.
 */
public class WalletDBAdapter {

    //Nombre y columnas de la tabla
    public static final String N_TABLA = "GASTOS" ;
    public static final String C_ID           = "_id";
    public static final String C_CONCEPTO     = "gasto_concepto";
    public static final String C_DESCRIPCION  = "gasto_descripcion";
    public static final String C_CANTIDAD     = "gasto_cantidad";
    public static final String C_LOCALIZACION = "gasto_localizacion";

    //Lista de columnas de la tabla
    private String[] columnas = new String[]{C_ID,
                                             C_CONCEPTO,
                                             C_DESCRIPCION,
                                             C_CANTIDAD,
                                             C_LOCALIZACION};
    private Context context;
    private WalletDBHelper dbHelper;
    private SQLiteDatabase db;

    public WalletDBAdapter(Context c){
        this.context = c;
    }

    // Abre un conexión con la base de datos
    public WalletDBAdapter abrir() throws SQLException {
        dbHelper = new WalletDBHelper(context);
        db = dbHelper.getWritableDatabase();
        return this;
    }

    // Cierra la conexión con la base de datos
    public void cerrar(){
        dbHelper.close();
    }

    // Devuelve cursor con todos las columnas de la tabla
    public Cursor getCursor() throws SQLException {
        Cursor c = db.query( true, N_TABLA, columnas, null, null, null, null, null, null);
        return c;
    }

    // Inserta los valores en un registro de la tabla
    public long insert(ContentValues reg) {
        if (db == null)
            abrir();
        return db.insert(N_TABLA, null, reg);
    }

    // Modificar el registro
    public long update(ContentValues reg) {
        long result = 0;
        if (db == null) abrir();
        if (reg.containsKey(C_ID)) {
            // Obtenemos el id y lo borramos de los valores
            long id = reg.getAsLong(C_ID);
            reg.remove(C_ID);
            // Actualizamos el registro con el identificador que hemos extraido
            result = db.update(N_TABLA, reg, "_id=" + id, null);
        }
        return result;
    }

    /**
     * Eliminar el registro con el identificador indicado
     */
    public long delete(long id){
        if (db == null) abrir();
        return db.delete(N_TABLA, "_id=" + id, null);
    }

    // Devuelve cursor con todos las columnas del registro
    public Cursor getRegistro(long id) throws SQLException {
        Cursor c = db.query( true, N_TABLA, columnas, C_ID + "=" + id, null, null, null, null, null);
        //Nos movemos al primer registro de la consulta
        if (c != null) c.moveToFirst();
        return c;
    }
}