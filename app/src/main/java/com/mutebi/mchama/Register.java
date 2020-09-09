package com.mutebi.mchama;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    private Button signUpBtn;
    private EditText editTextName;
    private EditText editTextEmail;
    private EditText editTextPhone;
    private EditText editTextPassword;
    private EditText editTextConfirmPassword;

    ProgressDialog progressDialog;

    //API Register URL
    public String register_url = "https://mchamatest.jeffreykingori.dev/api/v1/user/new";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        progressDialog = new ProgressDialog(Register.this);

        //initializing edit texts
        editTextName = findViewById(R.id.name);
        editTextEmail = findViewById(R.id.email);
        editTextPhone = findViewById(R.id.phone);
        editTextPassword = findViewById(R.id.password);
        editTextConfirmPassword = findViewById(R.id.confirm_password);

        //initializing sign up Button
        signUpBtn = findViewById(R.id.signup);

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Please wait...");
                progressDialog.show();

                userSignUp();

            }
        });

    }

    private void userSignUp() {
        final String name = editTextName.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();
        final String phone = editTextPhone.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();
        final String confirmPassword = editTextConfirmPassword.getText().toString().trim();

        //volley request
        StringRequest userRequest = new StringRequest(Request.Method.POST, register_url, new com.android.volley.Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    System.out.println(response);
                    JSONObject regResponse = new JSONObject(response);
                    String message = regResponse.getString("message");


                    if(regResponse.has("errors")){
                        JSONObject errors = regResponse.getJSONObject("errors");
                        String errorMsg = errors.toString();

                        String errMsg = "Error: "+ message.toUpperCase() +". \n\nDetails: "+errorMsg;
                        //Error Dialog
                        AlertDialog.Builder respBuilder = new AlertDialog.Builder(Register.this);
                        respBuilder.setTitle("An Error has occurred!")
                                .setMessage(errMsg)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Toast.makeText(Register.this, "Please retry...",Toast.LENGTH_SHORT).show();
                                    }
                                });
                        AlertDialog dialog = respBuilder.create();
                        dialog.show();
                    }
                    else if(regResponse.has("user")){
                        JSONObject newUser = regResponse.getJSONObject("user");
                        String uEmail = newUser.getString("email");
                        String uName = newUser.getString("name");

                        String success = "Welcome, "+ uName +" you have successfully created a mChama account with Email: '"+ uEmail +"'. \nUse this email to Login to your account.";

                        //Show Success Dialog
                        AlertDialog.Builder successBuilder = new AlertDialog.Builder(Register.this);
                        successBuilder.setTitle("Registration Successful")
                                .setMessage(success)
                                .setPositiveButton("LOGIN", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Toast.makeText(Register.this, "Redirecting to Login...",Toast.LENGTH_SHORT).show();
                                        Intent toLogin = new Intent(Register.this, Login.class);
                                        startActivity(toLogin);
                                    }
                                });
                        AlertDialog successDialog = successBuilder.create();
                        successDialog.show();
                    }
                    else{
                        //Some other server response message
                        Toast.makeText(Register.this, message,Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException je) {
                    je.printStackTrace();
                    Toast.makeText(Register.this, je.toString(),Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Register.this,error.toString(),Toast.LENGTH_LONG).show();
                error.printStackTrace();
                progressDialog.dismiss();
            }
        }){
            //HTTP headers
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String>  params = new HashMap<>();
                params.put("Accept", "application/json");

                return params;
            }
            //Req params
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<>();
                params.put("name", name);
                params.put("email", email);
                params.put("phone", phone);
                params.put("password", password);
                params.put("password_confirmation", confirmPassword);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(userRequest);
    }

}