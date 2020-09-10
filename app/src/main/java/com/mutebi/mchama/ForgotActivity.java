package com.mutebi.mchama;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mutebi.mchama.models.SharedPrefManager;
import com.mutebi.mchama.models.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ForgotActivity extends AppCompatActivity {
    private String forgot_url = "https://mchamatest.jeffreykingori.dev/api/v1/user/forgot";

    private EditText forgotEmail;
    private Button forgotBtn;
    private int currentUserId;
    private String token;

    //    Progress dialog
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);

        forgotEmail = findViewById(R.id.forgotEmail);
        forgotBtn = findViewById(R.id.newpass);

        //Instantiate Progress Dialog
        progressDialog = new ProgressDialog(ForgotActivity.this);

        User currentAuthUser = SharedPrefManager.getInstance(ForgotActivity.this).getUser();
        currentUserId = currentAuthUser.getId();
        token = currentAuthUser.getToken();

        forgotBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Sending the Reset link to your email");
                progressDialog.show();
                String editTextEmail = forgotEmail.getText().toString().trim();

                if(!TextUtils.isEmpty(editTextEmail) ) {
                    sendEmail(editTextEmail);
                }
                else {
                    //validation responses
                    progressDialog.dismiss();
                    if (TextUtils.isEmpty(editTextEmail)) {
                        forgotEmail.setError("Please enter your mChama email");
                        forgotEmail.requestFocus();
                        return;
                    }


                }


            }
        });


    }

    private void sendEmail(final String editTextEmail){
        StringRequest userRequest = new StringRequest(Request.Method.POST, forgot_url, new com.android.volley.Response.Listener<String>() {

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)

            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    //Handling Response
                    System.out.println(response);
                    JSONObject transResponse = new JSONObject(response);

                    boolean success = transResponse.getBoolean("success");
                    String message = transResponse.getString("message");

                    //Handling Successful resposnse
                    if(success == true){

                        AlertDialog.Builder respBuilder = new AlertDialog.Builder(ForgotActivity.this);
                        respBuilder.setTitle("Reset Password Email Sent!")
                                .setMessage("The reset password email has been sent successfully!\nClick the link on the email to reset your password. \n")

                                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();
                                        Intent loginActivity = new Intent(ForgotActivity.this, Login.class);
                                        startActivity(loginActivity);

                                    }
                                });
                        AlertDialog dialog = respBuilder.create();
                        dialog.show();



                    }
                    //Handling unsuccessful response
                    else if(!success){
                        if(transResponse.has("errors")){
                            JSONObject errors = transResponse.getJSONObject("errors");
                            String errorMsg = errors.toString();

                            String errMsg = "Error: "+ message.toUpperCase() +". \n\nDetails: "+errorMsg;
                            //Error Dialog
                            AlertDialog.Builder respBuilder = new AlertDialog.Builder(ForgotActivity.this);
                            respBuilder.setTitle("Email Reset Failed!")
                                    .setMessage(errMsg)
                                    .setNeutralButton("Dismiss", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            Toast.makeText(ForgotActivity.this, "Please retry...",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                            AlertDialog dialog = respBuilder.create();
                            dialog.show();
                        }
                        else{
                            AlertDialog.Builder respBuilder = new AlertDialog.Builder(ForgotActivity.this);
                            respBuilder.setTitle("Email Reset Failed!")
                                    .setMessage(message)
                                    .setNeutralButton("Dismiss", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.dismiss();
                                            Toast.makeText(ForgotActivity.this, "Please retry...",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                            AlertDialog dialog = respBuilder.create();
                            dialog.show();
                        }

                    }
                    else{
                        //Some other server response message
                        Toast.makeText(ForgotActivity.this, message,Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException je) {
                    je.printStackTrace();
                    Toast.makeText(ForgotActivity.this, je.toString(),Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ForgotActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                error.printStackTrace();
                progressDialog.dismiss();
            }
        }){
            //HTTP headers

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String>  params = new HashMap<>();
                params.put("Authorization", "Bearer "+token);
                params.put("Accept", "application/json");

                return params;
            }
            //Req params
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<>();
                params.put("email", editTextEmail);
                return params;
            }
        };
        userRequest.setRetryPolicy(new DefaultRetryPolicy(0, -1, 0));
        RequestQueue requestQueue = Volley.newRequestQueue(ForgotActivity.this);
        requestQueue.add(userRequest);
    }




}