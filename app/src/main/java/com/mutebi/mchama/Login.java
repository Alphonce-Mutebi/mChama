package com.mutebi.mchama;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.mutebi.mchama.Retrofit.ApiService;
import com.mutebi.mchama.Retrofit.ApiUtils;
import com.mutebi.mchama.models.SharedPrefManager;
import com.mutebi.mchama.models.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class Login extends AppCompatActivity {
    private EditText email;
    private EditText password;
    private Button signinBtn;
//    Creating an instance of ApiService
    private ApiService mAPIService;
//    Progress dialog
    ProgressDialog progressDialog;

    public String login_url = "https://mchamatest.jeffreykingori.dev/api/v1/user/oauth";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Instantiate Progress Dialog
        progressDialog = new ProgressDialog(Login.this);

        //checking sign in status
        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            Toast.makeText(Login.this, "Welcome back...",Toast.LENGTH_SHORT).show();
            finish();
            startActivity(new Intent(this, Dashboard.class));
            return;
        }

        //Initializing the ApiService instance
        mAPIService = ApiUtils.getAPIService();

        //instantiate email and password text views
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        //instantiate sign in button
        signinBtn = findViewById(R.id.signin);
        signinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Signing in...");
                progressDialog.show();
                String editTextEmail = email.getText().toString().trim();
                String editTextPassword =password.getText().toString().trim();

                if(!TextUtils.isEmpty(editTextEmail) && !TextUtils.isEmpty(editTextPassword)) {
                    authenticateUser(editTextEmail, editTextPassword);
                }
                else {
                    //validation responses
                    progressDialog.dismiss();
                    if (TextUtils.isEmpty(editTextEmail)) {
                        email.setError("Please enter your mChama email");
                        email.requestFocus();
                        return;
                    }

                    if (!android.util.Patterns.EMAIL_ADDRESS.matcher(editTextEmail).matches()) {
                        email.setError("Enter a valid email address");
                        email.requestFocus();
                        return;
                    }

                    if (TextUtils.isEmpty(editTextPassword)) {
                        password.setError("Enter a password");
                        password.requestFocus();
                        return;
                    }
                }

            }
        });

    }

    public void authenticateUser(final String email, final String password){
        //volley request
        StringRequest userRequest = new StringRequest(Request.Method.POST, login_url, new com.android.volley.Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    //Handling Response
                    System.out.println(response);
                    JSONObject regResponse = new JSONObject(response);

                    int success = regResponse.getInt("success");
                    String message = regResponse.getString("message");

                    //Handling Successful login
                    if(success == 1){
                        if(regResponse.has("user")){
                            JSONObject mUser = regResponse.getJSONObject("user");
                            String uName = mUser.getString("name");
                            String[] split = uName.split("\\s+");
                            String fName = split[0];
                            Toast.makeText(Login.this, "Welcome back "+fName, Toast.LENGTH_LONG).show();

                            User user = new User(
                                    mUser.getInt("id"),
                                    mUser.getString("name"),
                                    mUser.getString("email"),
                                    mUser.getString("phone"),
                                    mUser.getInt("wallet"),
                                    mUser.getInt("rotation"),
                                    regResponse.getString("token")
                            );

                            //storing the user in shared preferences
                            SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);

                            //starting the Dashboard activity
                            finish();
                            startActivity(new Intent(getApplicationContext(), Dashboard.class));

                        }
                    }
                    //Handling unsuccessful login
                    else if(success == 0){
                        if(regResponse.has("errors")){
                            JSONObject errors = regResponse.getJSONObject("errors");
                            String errorMsg = errors.toString();

                            String errMsg = "Error: "+ message.toUpperCase() +". \n\nDetails: "+errorMsg;
                            //Error Dialog
                            AlertDialog.Builder respBuilder = new AlertDialog.Builder(Login.this);
                            respBuilder.setTitle("Login Failed!")
                                    .setMessage(errMsg)
                                    .setNeutralButton("Dismiss", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            Toast.makeText(Login.this, "Please retry...",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                            AlertDialog dialog = respBuilder.create();
                            dialog.show();
                        }
                        else{
                            AlertDialog.Builder respBuilder = new AlertDialog.Builder(Login.this);
                            respBuilder.setTitle("Login Failed!")
                                    .setMessage(message)
                                    .setNeutralButton("Dismiss", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.dismiss();
                                            Toast.makeText(Login.this, "Please retry...",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                            AlertDialog dialog = respBuilder.create();
                            dialog.show();
                        }

                    }
                    else{
                        //Some other server response message
                        Toast.makeText(Login.this, message,Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException je) {
                    je.printStackTrace();
                    Toast.makeText(Login.this, je.toString(),Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Login.this,error.toString(),Toast.LENGTH_LONG).show();
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
                params.put("email", email);
                params.put("password", password);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(userRequest);
    }

    public void signup(View view) {
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);

    }

    public void goToForgotPassword(View view) {
        Intent forgot = new Intent(this, ForgotActivity.class);
        startActivity(forgot);
    }
}