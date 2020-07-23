package com.mutebi.mchama;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mutebi.mchama.Retrofit.ApiClient;
import com.mutebi.mchama.Retrofit.ApiService;

import java.util.HashMap;

public class Register extends AppCompatActivity {

    private Button signUpBtn;
    private EditText editTextName;
    private EditText editTextEmail;
    private EditText editTextPhone;
    private EditText editTextPassword;
    private EditText editTextConfirmPassword;

    ProgressDialog progressDialog;
    private ApiService mAPIService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        progressDialog = new ProgressDialog(Register.this);

//      initializing edit texts
        editTextName = findViewById(R.id.name);
        editTextEmail = findViewById(R.id.email);
        editTextPhone = findViewById(R.id.phone);
        editTextPassword = findViewById(R.id.password);
        editTextConfirmPassword = findViewById(R.id.confirm_password);

//      initializing sign up Button
        signUpBtn = findViewById(R.id.signup);

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Loading...");
                progressDialog.show();



                //register();

//                if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) &&
//                        !TextUtils.isEmpty(phone) && !TextUtils.isEmpty(password)
//                        && !TextUtils.isEmpty(confirmPassword)) {
//
//                }
              userSignUp();


            }
        });





    }

    private void userSignUp() {
        String name = editTextName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String confirmPassword = editTextConfirmPassword.getText().toString().trim();


        Call<ResponseBody> call = ApiClient
                .getApiClient()
                .getApi()
                .createUser(name,email,phone,password,confirmPassword);


        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String s = response.toString();
                Toast.makeText(Register.this, s+"works!!!", Toast.LENGTH_LONG).show();
               Intent register = new Intent(Register.this, Login.class);
               startActivity(register);

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(Register.this, t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });



    };

    /*private void register(){
        String name = editTextName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String confirmPassword = editTextConfirmPassword.getText().toString().trim();
         mAPIService.createUser(name, email, phone, password, confirmPassword).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String s = response.toString();

                Toast.makeText(Register.this, s+"works!!!", Toast.LENGTH_LONG).show();

                Intent register = new Intent(Register.this, Login.class);
                startActivity(register);

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(Register.this, "Please fill in all the fields",Toast.LENGTH_LONG);

            }
        });
    }

    */


}