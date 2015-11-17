package com.example.cristian.mywallet;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class PresupuestoCursorAdapter extends CursorAdapter
{
    private WalletDBAdapter dbAdapter = null ;

    public PresupuestoCursorAdapter(Context context, Cursor c){
        super(context, c, 0);
        dbAdapter = new WalletDBAdapter(context);
        dbAdapter.abrir();
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tv = (TextView) view ;
        tv.setText(cursor.getDouble(cursor.getColumnIndex(WalletDBAdapter.C_DISPONIBLE))+" €"+" de "+
                   cursor.getDouble(cursor.getColumnIndex(WalletDBAdapter.C_PRESUPUESTO))+" €");
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent){
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.pres_item, parent, false);
        return view;
    }
}