package com.example.travella_bus;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.DownloadManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TokenView extends AppCompatActivity {

    ImageView tokenView;
    TextView idView, userView, statusVeiw, journeyView;
    Button procced, delete;
    ConstraintLayout viewLayout;
    String vehicleID ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_token_view);
         vehicleID = ((Globals) this.getApplication()).getVehicleID();
        Intent intent = getIntent();
        final String token = intent.getStringExtra("token");

        tokenView = findViewById(R.id.tokenView);
        idView = findViewById(R.id.idView);
        userView = findViewById(R.id.userView);
        statusVeiw = findViewById(R.id.statusView);
        journeyView = findViewById(R.id.journeyView);
        procced = findViewById(R.id.proceedButton);
        viewLayout = findViewById(R.id.tokenViewConstraint);
        delete = findViewById(R.id.deletButton);

        procced.setEnabled(false);
        procced.setVisibility(View.GONE);

        procced.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TokenView.this, Validator.class);
                intent.putExtra("token", token);
                startActivity(intent);
                finish();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteToken(token);
            }
        });

        getTokenDetails(token);
        generateQr(token);

    }

    private void generateQr(String text){
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE,400,400);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            tokenView.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    private void getTokenDetails(String tokenString){

        String json_url = "https://powerful-plateau-81192.herokuapp.com/api/token/get/"+tokenString;
        JSONObject jsonObject = new JSONObject();

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, json_url, jsonObject, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject array) {

                String[] id = new String[array.length()];
                String[] user = new String[array.length()];
                String[] journey = new String[array.length()];
                String[] status = new String[array.length()];
                String[] vehicle = new String[array.length()];


                try {
                    id[0] = array.get("id").toString();
                    user[0] = array.get("user_id").toString();
                    journey[0] = array.get("journey_id").toString();
                    status[0] = array.get("status").toString();
                    vehicle[0] = array.get("vehicle_id").toString();

                    idView.setText("Token ID : " + id[0]);
                    userView.setText("User : " + user[0]);
                    journeyView.setText("Journey : " + journey[0]);

                    if (!status[0].equals("2") && !status[0].equals("0")) {
                        statusVeiw.setText("Status : Valid" );
                        procced.setEnabled(true);
                        viewLayout.setBackgroundColor(getResources().getColor(R.color.validTokenBack));
                    }
                    if (status[0].equals("2") && !vehicle[0].equals(vehicleID))
                    {
                        statusVeiw.setText("Status : Used by an another Vehicle" );
                        viewLayout.setBackgroundColor(getResources().getColor(R.color.usedTokenBack));
                        delete.setVisibility(View.VISIBLE);
                    }
                    if (status[0].equals("2") && vehicle[0].equals(vehicleID))
                    {
                        statusVeiw.setText("Status : Used by this vehicle" );
                        viewLayout.setBackgroundColor(getResources().getColor(R.color.invalidTokenBack));
                        delete.setVisibility(View.VISIBLE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("error");
                Toast.makeText(TokenView.this, "Invalid Token", Toast.LENGTH_LONG).show();
                System.out.println(error);
                viewLayout.setBackgroundColor(getResources().getColor(R.color.invalidTokenBack));
                delete.setVisibility(View.GONE);
            }
        });
        Volley.newRequestQueue(TokenView.this).add(jsonObjectRequest);
    }

    private void deleteToken(String tokenString){

        String json_url = "https://powerful-plateau-81192.herokuapp.com/api/token/"+tokenString;
        JSONObject jsonObject = new JSONObject();

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, json_url, jsonObject, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject array) {
                String[] id = new String[array.length()];
                String[] user = new String[array.length()];

                try {
                    id[0] = array.get("id").toString();
                    user[0] = array.get("user_id").toString();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("error");
                Toast.makeText(TokenView.this, "Invalid Token", Toast.LENGTH_LONG).show();
                System.out.println(error);
                viewLayout.setBackgroundColor(getResources().getColor(R.color.invalidTokenBack));
                delete.setVisibility(View.GONE);
            }
        });
        Volley.newRequestQueue(TokenView.this).add(jsonObjectRequest);

        Toast.makeText(TokenView.this, "Token deleted", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(TokenView.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
