package com.example.travella_bus;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button scannerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scannerBtn = findViewById(R.id.scannerBtn);

        final Intent intent = getIntent();
        String vehicle_id = intent.getStringExtra("user");

        if (vehicle_id != null){
            TextView user = findViewById(R.id.main_id);
            user.setText(vehicle_id);
        }

        scannerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent scannerIntent = new Intent(MainActivity.this, Scanner.class);
                startActivity(scannerIntent);
            }
        });
    }
}
