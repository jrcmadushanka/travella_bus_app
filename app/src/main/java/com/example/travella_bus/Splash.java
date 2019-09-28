package com.example.travella_bus;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Splash extends AppCompatActivity {

    Button loginBtn;
    EditText vID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        loginBtn = findViewById(R.id.login_btn);
        vID = findViewById(R.id.vehicleIdEdit);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Globals) Splash.this.getApplication()).setVehicleID( vID.getText().toString());
                String vehicle_id = ((Globals) Splash.this.getApplication()).getVehicleID();

                if (!vehicle_id.equals("")) {
                    Intent intent = new Intent(Splash.this, MainActivity.class);
                    intent.putExtra("user", "LoggedIn");
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(Splash.this, "Invalid credentials", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}
