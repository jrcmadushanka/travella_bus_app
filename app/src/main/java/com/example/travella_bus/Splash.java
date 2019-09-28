package com.example.travella_bus;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Splash extends AppCompatActivity {

    Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        loginBtn = findViewById(R.id.login_btn);

        ((Globals) this.getApplication()).setVehicleID("142");

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Splash.this,MainActivity.class);
                intent.putExtra("user", "LoggedIn");
                startActivity(intent);
                finish();
            }
        });
    }

}
