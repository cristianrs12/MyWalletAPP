package com.example.cristian.mywallet;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;

public class GraphsActivity extends AppCompatActivity {

    private WalletDBAdapter dbAdapter;
    private double presupuesto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphs);

        dbAdapter = new WalletDBAdapter(this);
        dbAdapter.abrir();

        GraphView graph2 = (GraphView) findViewById(R.id.graph4);

        double[] valores = formatearArray(getValores());

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, Constants.presupuesto),
                new DataPoint(1, Constants.presupuesto),
                new DataPoint(2, Constants.presupuesto),
                new DataPoint(3, Constants.presupuesto),
                new DataPoint(4, Constants.presupuesto),
                new DataPoint(5, Constants.presupuesto),
                new DataPoint(6, Constants.presupuesto),
                new DataPoint(7, Constants.presupuesto),
                new DataPoint(8, Constants.presupuesto)
        });
        BarGraphSeries<DataPoint> series2 = new BarGraphSeries<>(new DataPoint[] {
                new DataPoint(1, valores[0]),
                new DataPoint(2, valores[1]),
                new DataPoint(3, valores[2]),
                new DataPoint(4, valores[3]),
                new DataPoint(5, valores[4]),
                new DataPoint(6, valores[5]),
                new DataPoint(7, valores[6])
        });

        graph2.addSeries(series);

        series.setTitle("Presupuesto");

        int R = (int) presupuesto * 255/4,
            G = (int) Math.abs(presupuesto * 255/6),
            B = 100;

        series.setColor(Color.rgb(R, G, B));
        series.setDrawDataPoints(false);
        series.setThickness(5);

        graph2.addSeries(series2);

        series2.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                switch (Double.toString(data.getX())){
                    case "1.0":
                        return Color.rgb(255,230,0);
                    case "2.0":
                        return Color.rgb(255,0,0);
                    case "3.0":
                        return Color.rgb(0,255,13);
                    case "4.0":
                        return Color.rgb(0,247,255);
                    case "5.0":
                        return Color.rgb(0,34,255);
                    case "6.0":
                        return Color.rgb(148,2,211);
                    case "7.0":
                        return Color.rgb(255,136,0);
                }
                return Color.rgb(0,0,0);
            }
        });
        series2.setSpacing(50);
        series2.setDrawValuesOnTop(true);
        series2.setValuesOnTopColor(Color.BLUE);
    }

    public double[] formatearArray(ArrayList<DataPoint> a){
        double[] valores = new double[7];
        double cant =0;
        for (int i=1; i<=7; i++){
            cant=0;
            for(int j=0; j<a.size(); j++){
                if(a.get(j).getY()==i){
                cant=cant+a.get(j).getX();
                }
            }
            valores[i-1] = cant;
        }
        return valores;
    }

    public ArrayList<DataPoint> getValores() {
        ArrayList<DataPoint> auxV = new ArrayList<>();

        Cursor prep;
        prep=dbAdapter.getCursorPrep();
        Constants.presupuesto=prep.getDouble(prep.getColumnIndex(WalletDBAdapter.C_PRESUPUESTO));
        Constants.disponible=prep.getDouble(prep.getColumnIndex(WalletDBAdapter.C_DISPONIBLE));
        presupuesto=Constants.presupuesto;

        double auxC = 0;
        int cat = 0;
        Cursor cCantidad;
        cCantidad = dbAdapter.getCursor();
        if(cCantidad.getCount()>0){
            cCantidad.moveToFirst();
            while (!cCantidad.isAfterLast()) {
                auxC = cCantidad.getDouble(cCantidad.getColumnIndex(WalletDBAdapter.C_CANTIDAD));
                cat = getCatValue(cCantidad.getString(cCantidad.getColumnIndex(WalletDBAdapter.C_CATEGORIA)));
                auxV.add(new DataPoint(auxC,cat));
                cCantidad.moveToNext();
            }
        }
        return auxV;
    }
    public int getCatValue(String categoria){
        switch(categoria){
            case "Comida":      return 1;
            case "Lujos":       return 2;
            case "Básicos":     return 3;
            case "Caprichos":   return 4;
            case "Mensual":     return 5;
            case "Transporte":  return 6;
            case "Otros":       return 7;
            default:            return 0;
        }
    }
}