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
import android.widget.TextView;
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

public class LoanService extends AppCompatActivity {
    TextView balTxt;
    EditText uPhone, uAmount;
    Button submitBtn;
    private ProgressDialog progressDialog;
    private int currentUserId;
    private String token;
    private String transaction_url = "https://mchamatest.jeffreykingori.dev/api/v1/process/transactions/new";
    private String loanbal_url = "https://mchamatest.jeffreykingori.dev/api/v1/process/loans/balance";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_service);

        //Instantiate the widgets
        balTxt = findViewById(R.id.current_loan_balance);
        uPhone = findViewById(R.id.loan_service_phone);
        uAmount = findViewById(R.id.loan_service_amount);
        submitBtn = findViewById(R.id.loan_service_submit);
        progressDialog = new ProgressDialog(LoanService.this);

        User currentAuthUser = SharedPrefManager.getInstance(LoanService.this).getUser();
        currentUserId = currentAuthUser.getId();
        token = currentAuthUser.getToken();

        uPhone.setText(currentAuthUser.getPhone());

        updateLoanBalance();

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Processing Transaction ...");
                progressDialog.show();
                String editTextPhone = uPhone.getText().toString().trim();
                String editTextAmount = uAmount.getText().toString().trim();


                if(!TextUtils.isEmpty(editTextPhone) && !TextUtils.isEmpty(editTextAmount)) {
                    createTransaction(editTextPhone, Integer.parseInt(editTextAmount));
                }
                else {
                    //validation responses
                    progressDialog.dismiss();
                    if (TextUtils.isEmpty(editTextPhone)) {
                        uPhone.setError("Please enter your mChama uPhone");
                        uPhone.requestFocus();
                        return;
                    }

                    if (TextUtils.isEmpty(editTextAmount)) {
                        uAmount.setError("Enter a uAmount");
                        uAmount.requestFocus();
                        return;
                    }
                }
            }
        });



    }

    private void createTransaction(final String editTextPhone, final int editTextAmount) {
        //volley request

        StringRequest userRequest = new StringRequest(Request.Method.POST, transaction_url, new com.android.volley.Response.Listener<String>() {

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    //Handling Response
                    System.out.println(response);
                    JSONObject transResponse = new JSONObject(response);

                    int success = transResponse.getInt("success");
                    String message = transResponse.getString("message");

                    //Handling Successful resposnse
                    if(success == 1){
                        if(transResponse.has("balance")){
                            updateLoanBalance();
                        }
                        AlertDialog.Builder respBuilder = new AlertDialog.Builder(LoanService.this);
                        respBuilder.setTitle("Transaction Completed")
                                .setMessage("Your M-Pesa Payment has been received. \nYour Loan account balance has been updated. \n" +
                                        "A confirmation message has been sent by SMS")
                                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();
                                    }
                                });
                        AlertDialog dialog = respBuilder.create();
                        dialog.show();


                    }
                    //Handling unsuccessful response
                    else if(success == 0){
                        if(transResponse.has("errors")){
                            JSONObject errors = transResponse.getJSONObject("errors");
                            String errorMsg = errors.toString();

                            String errMsg = "Error: "+ message.toUpperCase() +". \n\nDetails: "+errorMsg;
                            //Error Dialog
                            AlertDialog.Builder respBuilder = new AlertDialog.Builder(LoanService.this);
                            respBuilder.setTitle("Transaction Failed!")
                                    .setMessage(errMsg)
                                    .setNeutralButton("Dismiss", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            Toast.makeText(LoanService.this, "Please retry...",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                            AlertDialog dialog = respBuilder.create();
                            dialog.show();
                        }
                        else{
                            AlertDialog.Builder respBuilder = new AlertDialog.Builder(LoanService.this);
                            respBuilder.setTitle("Transaction Failed!")
                                    .setMessage(message)
                                    .setNeutralButton("Dismiss", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.dismiss();
                                            Toast.makeText(LoanService.this, "Please retry...",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                            AlertDialog dialog = respBuilder.create();
                            dialog.show();
                        }

                    }
                    else{
                        //Some other server response message
                        Toast.makeText(LoanService.this, message,Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException je) {
                    je.printStackTrace();
                    Toast.makeText(LoanService.this, je.toString(),Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoanService.this,error.toString(),Toast.LENGTH_LONG).show();
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
                params.put("user_id", Integer.toString(currentUserId));
                params.put("phone", editTextPhone);
                params.put("amount", String.valueOf(editTextAmount));
                params.put("type", "1");

                return params;
            }
        };
        userRequest.setRetryPolicy(new DefaultRetryPolicy(0, -1, 0));
        RequestQueue requestQueue = Volley.newRequestQueue(LoanService.this);
        requestQueue.add(userRequest);
    }

    private void updateLoanBalance() {

        //Volley Loan Balance request
        StringRequest loanBalRequest = new StringRequest(Request.Method.POST, loanbal_url, new com.android.volley.Response.Listener<String>() {

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    //Handling Response
                    System.out.println(response);
                    JSONObject loanBalRequest = new JSONObject(response);

                    int success = loanBalRequest.getInt("success");
                    String message = loanBalRequest.getString("message");

                    //Handling Successful resposnse
                    if(success == 1){
                        if(loanBalRequest.has("balance")){
                            if(loanBalRequest.getBoolean("active")){
                                //Update loan balance text
                                balTxt.setText(loanBalRequest.getString("balance"));
                            }
                            else{
                                //Update loan bal text
                                balTxt.setText(loanBalRequest.getString("balance"));
                                //No Active loans Dialog
                                AlertDialog.Builder respBuilder = new AlertDialog.Builder(LoanService.this);
                                respBuilder.setTitle("No Active Loans!")
                                        .setMessage("Ooops!, Looks like you have no active loans in your account which can be serviced \n\nHave you applied for a loan? \n" +
                                                "If you have, it may be pending approval...")
                                        .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.dismiss();
                                                Intent toDash = new Intent(LoanService.this, Dashboard.class);
                                                startActivity(toDash);
                                            }
                                        });
                                AlertDialog dialog = respBuilder.create();
                                dialog.show();
                            }
                        }

                    }
                    //Handling unsuccessful response
                    else if(success == 0){
                        if(loanBalRequest.has("errors")){
                            JSONObject errors = loanBalRequest.getJSONObject("errors");
                            String errorMsg = errors.toString();

                            String errMsg = "Error: "+ message.toUpperCase() +". \n\nDetails: "+errorMsg;
                            //Error Dialog
                            AlertDialog.Builder respBuilder = new AlertDialog.Builder(LoanService.this);
                            respBuilder.setTitle("Transaction Failed!")
                                    .setMessage(errMsg)
                                    .setNeutralButton("Dismiss", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            Toast.makeText(LoanService.this, "Please retry...",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                            AlertDialog dialog = respBuilder.create();
                            dialog.show();
                        }
                        else{
                            AlertDialog.Builder respBuilder = new AlertDialog.Builder(LoanService.this);
                            respBuilder.setTitle("Transaction Failed!")
                                    .setMessage(message)
                                    .setNeutralButton("Dismiss", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.dismiss();
                                            Toast.makeText(LoanService.this, "Please retry...",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                            AlertDialog dialog = respBuilder.create();
                            dialog.show();
                        }

                    }
                    else{
                        //Some other server response message
                        Toast.makeText(LoanService.this, message,Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException je) {
                    je.printStackTrace();
                    Toast.makeText(LoanService.this, je.toString(),Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoanService.this,error.toString(),Toast.LENGTH_LONG).show();
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
                params.put("user_id", Integer.toString(currentUserId));

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(LoanService.this);
        requestQueue.add(loanBalRequest);

    }

}