package com.example.cristian.mywallet;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class WalletDBAdapter {

    //Nombre y columnas de la tabla
    public static final String N_TABLA  = "GASTOS" ;
    public static final String C_ID           = "_id";
    public static final String C_CONCEPTO     = "gasto_concepto";
    public static final String C_DESCRIPCION  = "gasto_descripcion";
    public static final String C_CANTIDAD     = "gasto_cantidad";
    public static final String C_LOCALIZACION = "gasto_localizacion";
    public static final String C_CATEGORIA    = "categoria";

    public static final String N_TABLA2 = "PRESUPUESTOS";
    public static final String C_PRESUPUESTO  = "presupuesto";
    public static final String C_DISPONIBLE  = "disponible";

    //Lista de columnas de las tablas
    private String[] columnas = new String[]{C_ID,
                                             C_CONCEPTO,
                                             C_DESCRIPCION,
                                             C_CANTIDAD,
                                             C_LOCALIZACION,
                                             C_CATEGORIA};

    private String[] columnas2 = new String[]{C_ID,
                                              C_PRESUPUESTO,
                                              C_DISPONIBLE};

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
        Cursor c = db.query(true, N_TABLA, columnas, null, null, null, null, null, null);
        return c;
    }

    public Cursor getCursorPrep() throws  SQLException {
        Cursor c = db.query(true, N_TABLA2, columnas2, null, null, null, null, null, null);
        if (c != null) c.moveToFirst();
        return c;
    }
    public long updatePrep(ContentValues reg){
        long result = 0;
        if (db == null) abrir();
        if (reg.containsKey(C_ID)) {
            // Obtenemos el id y lo borramos de los valores
            long id = reg.getAsLong(C_ID);
            reg.remove(C_ID);
            // Actualizamos el registro con el identificador que hemos extraido
            result = db.update(N_TABLA2, reg, "_id=" + id, null);
        }
        return result;
    }

   /* // Devuelve cursor con todos las columnas de la tabla  }
    public Cursor getPresupuesto() throws SQLException {
        Cursor c = db.query( true, N_TABLA2, columnas2, null, null, null, null, null, null);
        return c;
    }*/

  /*  // Inserta los valores en un registro de la tabla
    public long insertPresupuesto(ContentValues reg) {
        if (db == null)
            abrir();
        return db.insert(N_TABLA2, null, reg);
    }*/

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

   /* public double getPresupuesto2(){
        Cursor c;
        double presupuesto=0;
        c=db.query(true, N_TABLA2,columnas2,null,null,null,null,null,"1");
        if(c.getCount()==0)
            presupuesto=0;
        else
        presupuesto=c.getDouble(c.getColumnIndex(C_PRESUPUESTO));

        return presupuesto;
    }*/

    // Modificar el registro de presupuesto
 /*   public long updatePres(ContentValues reg) {
        long result = 0;
        if (db == null) abrir();
        if (reg.containsKey(C_ID)) {
            // Obtenemos el id y lo borramos de los valores
            long id = reg.getAsLong(C_ID);
            reg.remove(C_ID);
            // Actualizamos el registro con el identificador que hemos extraido
            result = db.update(N_TABLA2, reg, "_id=" + id, null);
        }
        return result;
    }*/

    // Eliminar el registro con el identificador
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
/*
    // Devuelve cursor con todos las columnas del registro
    public Cursor getRegistroPres(long id) throws SQLException {
        Cursor c = db.query( true, N_TABLA2, columnas2, C_ID + "=" + id, null, null, null, null, null);
        //Nos movemos al primer registro de la consulta
        if (c != null) c.moveToFirst();
        return c;
    }*/
}