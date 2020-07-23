package com.mutebi.mchama;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mutebi.mchama.Retrofit.ApiClient;
import com.mutebi.mchama.Retrofit.ApiService;
import com.mutebi.mchama.Retrofit.ApiUtils;
import com.mutebi.mchama.models.Data;
import com.mutebi.mchama.models.Data_;
import com.mutebi.mchama.models.User;
import com.mutebi.mchama.models.UserResponse;

public class Login extends AppCompatActivity {
    private EditText email;
    private EditText password;
    private Button signinBtn;
//    Creating an instance of ApiService
    private ApiService mAPIService;
//    Progress dialog
    ProgressDialog progressDialog;

    public static String authToken;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//      Instantiate Progress Dialog
        progressDialog = new ProgressDialog(Login.this);

//        Initializing the ApiService instance
        mAPIService = ApiUtils.getAPIService();

//        instantiate email and password text views
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

//        instantiate sign in button
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


            }
        });








    }
    public void authenticateUser(String email, String password){
        mAPIService.authenticateUser(email,password).enqueue(new Callback<Data>() {

            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                String name = response.body().getData().getUserResponse().getName();
                authToken = response.body().getData().getToken();
                //Toast.makeText(Login.this, response.body().getData().getToken()+"works!!!", Toast.LENGTH_LONG).show();
                Intent login = new Intent(Login.this, dashboard.class);

                login.putExtra("name", name);
                startActivity(login);

            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {
                Toast.makeText(Login.this, "Username or Password is invalid",Toast.LENGTH_LONG);

            }


        });

    }



    public void signin() {




//        User user = new User(editTextEmail,editTextPassword);
//
//        Call<UserResponse> call = ApiClient
//                .getApiClient()
//                .getUserClient()
//                .loginAccount(user);
//
//        call.enqueue(new Callback<UserResponse>() {
//            @Override
//            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
//                String s = response.toString();
//                Toast.makeText(Login.this, s+"works!!!", Toast.LENGTH_LONG).show();
//
////                Intent intent = new Intent(Login.this, dashboard.class);
////                startActivity(intent);
//
//            }
//
//            @Override
//            public void onFailure(Call<UserResponse> call, Throwable t) {
//
//            }
//        });


    }

    public void signup(View view) {
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);

    }


}