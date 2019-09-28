package com.example.travella_bus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Validator extends AppCompatActivity {

    String vehicleID = "";
    ConstraintLayout goodToGo;
    Button scanAnother;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validator);

        String vehicle = ((Globals) this.getApplication()).getVehicleID();
        goodToGo = findViewById(R.id.goodToGoConstarint);
        goodToGo.setVisibility(View.GONE);
        scanAnother = findViewById(R.id.goodButton);
        Intent intent = getIntent();
        String token = intent.getStringExtra("token");

        consumeToken(token, vehicle);

        scanAnother.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(Validator.this, Scanner.class);
                startActivity(intent1);
                finish();
            }
        });
    }

    private void consumeToken(String tokenString, final String vehicleID){

        String json_url = "https://powerful-plateau-81192.herokuapp.com/api/token/validate/"+tokenString+"/"+vehicleID;
        JSONObject jsonObject = new JSONObject();

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, json_url, jsonObject, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject array) {

                String[] vehicleId = new String[array.length()];

                try {

                    vehicleId[0] = array.get("vehicle_id").toString();

                    if (vehicleId[0].equals(vehicleID)){
                        goodToGo.setVisibility(View.VISIBLE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("error");
                Toast.makeText(Validator.this, "Token error", Toast.LENGTH_LONG).show();
                System.out.println(error);
            }
        });
        Volley.newRequestQueue(Validator.this).add(jsonObjectRequest);
    }
}
