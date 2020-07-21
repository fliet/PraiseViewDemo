package com.example.praiseviewdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.button);
        final EditText editText = findViewById(R.id.edit_text);
        final PraiseView praiseView = findViewById(R.id.praise_view);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String beginNum = editText.getText().toString();

                if (beginNum.isEmpty()) {
                    beginNum = "999";
                }

                praiseView.reset(beginNum);
            }
        });
    }
}
