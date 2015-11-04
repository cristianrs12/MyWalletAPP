package com.example.cristian.mywallet;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class WalletCursorAdapter extends CursorAdapter
{
    private WalletDBAdapter dbAdapter = null ;

    public WalletCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
        dbAdapter = new WalletDBAdapter(context);
        dbAdapter.abrir();
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor)
    {
        //TODO: Formatear contenido List Item
        TextView tv = (TextView) view ;
        tv.setText("Concepto: "+
                   cursor.getString(cursor.getColumnIndex(WalletDBAdapter.C_CONCEPTO))+"\n"+
                   "Descripción: "+
                   cursor.getString(cursor.getColumnIndex(WalletDBAdapter.C_DESCRIPCION))+"\n"+
                   "Cantidad: "+
                   cursor.getString(cursor.getColumnIndex(WalletDBAdapter.C_CANTIDAD))+"\n"+
                   "Localización: "+
                   cursor.getString(cursor.getColumnIndex(WalletDBAdapter.C_LOCALIZACION)));
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent)
    {
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.list_item, parent, false);

        return view;
    }
}