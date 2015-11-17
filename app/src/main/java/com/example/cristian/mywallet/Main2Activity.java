package com.example.cristian.mywallet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;

public class Main2Activity extends AppCompatActivity {

    TextView mUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        mUser= (TextView) findViewById(R.id.textView2);
        Bundle i = getIntent().getExtras();

        if(i != null){//TODO: Solucionar casos en los que se recibe un Intent
            mUser.setText(i.getString("USERID"));
        }

        CardView movements    = (CardView) findViewById(R.id.card_view0);
        CardView addMovements = (CardView) findViewById(R.id.card_view1);
        CardView accounts     = (CardView) findViewById(R.id.card_view2);

        // Declare and setup Explicit Activation card
        movements.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (v.getContext(),GastosActivity.class);
                startActivity(intent);
            }
        });
        addMovements.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (v.getContext(),AddGastosActivity.class);
                startActivity(intent);
            }
        });
        accounts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (v.getContext(),LocationActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onStart() {super.onStart();}

    @Override
    public void onResume() {super.onResume();}

    @Override
    public void onPause() {super.onPause();}

    @Override
    public void onStop() {super.onStop();}

    @Override
    public void onRestart() {super.onRestart();}

    @Override
    public void onDestroy() {super.onDestroy();}

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }
}
