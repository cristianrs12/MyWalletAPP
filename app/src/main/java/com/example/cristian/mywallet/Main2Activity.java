package com.example.cristian.mywallet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
        if(!i.isEmpty()){
            mUser.setText(i.getString("USERID"));
        }
        // Declare and setup Explicit Activation button
        Button movements = (Button) findViewById(R.id.move);
        movements.setOnClickListener(new View.OnClickListener() {

            // Call startExplicitActivation() when pressed
            @Override
            public void onClick(View v) {

                Intent intent = new Intent (v.getContext(),GastosActivity.class);
                // TODO - Start the chooser Activity, using the intent
                startActivity(intent);

            }
        });
        Button addMovements = (Button) findViewById(R.id.add);
        addMovements.setOnClickListener(new View.OnClickListener() {

            // Call startExplicitActivation() when pressed
            @Override
            public void onClick(View v) {

                Intent intent = new Intent (v.getContext(),GastosActivity.class);
                // TODO - Start the chooser Activity, using the intent
                startActivity(intent);

            }
        });
        Button acounts = (Button) findViewById(R.id.move);
        acounts.setOnClickListener(new View.OnClickListener() {

            // Call startExplicitActivation() when pressed
            @Override
            public void onClick(View v) {

                Intent intent = new Intent (v.getContext(),GastosActivity.class);
                // TODO - Start the chooser Activity, using the intent
                startActivity(intent);

            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();

        // TODO: Emit LogCat message


        // TODO:
        // Update the appropriate count variable
        // Update the user interface


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
}
