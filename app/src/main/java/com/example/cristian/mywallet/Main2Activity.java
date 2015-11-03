package com.example.cristian.mywallet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
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

        // Retrieve the LinearLayout
        View linear = findViewById(R.id.main2linear);

        // Set the padding to match the Status Bar height
        linear.setPadding(0, -getStatusBarHeight(), 0, 0);

        CardView movements = (CardView) findViewById(R.id.card_view0);
        CardView addMovements = (CardView) findViewById(R.id.card_view1);
        CardView accounts = (CardView) findViewById(R.id.card_view2);

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
                Intent intent = new Intent (v.getContext(),GastosActivity.class);
                startActivity(intent);
            }
        });
    }

    //Find height of the status bar
    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Override
    public void onStart() {
        super.onStart();
        //TODO: Emit logcat...
    }

    @Override
    public void onResume() {
        super.onResume();

        // TODO: Emit LogCat message


        // TODO:
        // Update the appropriate count variable
        // Update the user interface


    }

    @Override
    public void onPause() {
        super.onPause();

        // TODO: Emit LogCat message

    }

    @Override
    public void onStop() {
        super.onStop();

        // TODO: Emit LogCat message

    }

    @Override
    public void onRestart() {
        super.onRestart();

        // TODO: Emit LogCat message


        // TODO:
        // Update the appropriate count variable
        // Update the user interface


    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // TODO: Emit LogCat message


    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // TODO:
        // Save state information with a collection of key-value pairs
        // 4 lines of code, one for every count variable


    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

    }
}
