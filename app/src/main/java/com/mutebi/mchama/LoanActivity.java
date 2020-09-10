package com.mutebi.mchama;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

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

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class LoanActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private EditText fundDate, loanAmount;
    private EditText repayment_start_date;
    private int mYear,mMonth,mDay;
    private Button submitLoan;
    private int repaymentPeriod;
    private ProgressDialog progressDialog;

    private int currentUserId;
    private String token;
    public final String new_loan_url = "https://mchamatest.jeffreykingori.dev/api/v1/process/loans/new";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan);

        //instantiate some widgets
        fundDate = findViewById(R.id.newLoan_fund_date);
        fundDate.setOnClickListener(this);

        repayment_start_date = findViewById(R.id.newLoan_repayment_start_date);
        repayment_start_date.setOnClickListener(this);

        loanAmount = findViewById(R.id.newLoan_amount);
        submitLoan = findViewById(R.id.newLoan_submit);

        progressDialog = new ProgressDialog(LoanActivity.this);

        Spinner spinner = findViewById(R.id.repayment_period);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.repayment_period, android.R.layout.simple_spinner_item );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);


        submitLoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String grantDate = fundDate.getText().toString().trim();
                String repaymentDate = repayment_start_date.getText().toString().trim();
                String loanAmt = loanAmount.getText().toString().trim();

                if (TextUtils.isEmpty(grantDate)) {
                    fundDate.setError("Please enter a grant Date");
                    fundDate.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(repaymentDate)) {
                    repayment_start_date.setError("Please enter a repayment Start Date");
                    repayment_start_date.requestFocus();
                    return;
                }


                if (TextUtils.isEmpty(loanAmt)) {
                    loanAmount.setError("Please enter a Loan Amount");
                    loanAmount.requestFocus();
                    return;
                }
                progressDialog.setMessage("Processing Request...");
                progressDialog.show();

                processLoanRequest(grantDate, repaymentDate, loanAmt, repaymentPeriod);

            }
        });


    }

    private void processLoanRequest(final String grantDate, final String repaymentDate, final String amount, final int repaymentPeriod) {
        //volley request
        User currentAuthUser = SharedPrefManager.getInstance(LoanActivity.this).getUser();
        currentUserId = currentAuthUser.getId();
        token = currentAuthUser.getToken();
        StringRequest userRequest = new StringRequest(Request.Method.POST, new_loan_url, new com.android.volley.Response.Listener<String>() {

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    //Handling Response
                    System.out.println(response);
                    JSONObject regResponse = new JSONObject(response);

                    int success = regResponse.getInt("success");
                    String message = regResponse.getString("message");

                    //Handling Successful response
                    if(success == 1){
                            AlertDialog.Builder respBuilder = new AlertDialog.Builder(LoanActivity.this);
                            respBuilder.setTitle("Loan Requested")
                                    .setMessage("Your request for a Loan has been submitted successfully. \n\n" +
                                            "Its current status is pending for approval from the treasurer.")
                                    .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.dismiss();
                                            finish();
                                            Intent toDash = new Intent(LoanActivity.this, Dashboard.class);
                                            startActivity(toDash);
                                        }
                                    });
                            AlertDialog dialog = respBuilder.create();
                            dialog.show();

                    }
                    //Handling unsuccessful response
                    else if(success == 0){
                        if(regResponse.has("errors")){
                            JSONObject errors = regResponse.getJSONObject("errors");
                            String errorMsg = errors.toString();

                            String errMsg = "Error: "+ message.toUpperCase() +". \n\nDetails: "+errorMsg;
                            //Error Dialog
                            AlertDialog.Builder respBuilder = new AlertDialog.Builder(LoanActivity.this);
                            respBuilder.setTitle("Loan Request Failed!")
                                    .setMessage(errMsg)
                                    .setNeutralButton("Dismiss", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            Toast.makeText(LoanActivity.this, "Please retry...",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                            AlertDialog dialog = respBuilder.create();
                            dialog.show();
                        }
                        else{
                            AlertDialog.Builder respBuilder = new AlertDialog.Builder(LoanActivity.this);
                            respBuilder.setTitle("Loan Request Failed!")
                                    .setMessage(message)
                                    .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.dismiss();
                                            finish();
                                            Intent toDash = new Intent(LoanActivity.this, Dashboard.class);
                                            startActivity(toDash);
                                        }
                                    });
                            AlertDialog dialog = respBuilder.create();
                            dialog.show();
                        }
                    }
                    else{
                        //Some other server response message
                        Toast.makeText(LoanActivity.this, message,Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException je) {
                    je.printStackTrace();
                    Toast.makeText(LoanActivity.this, je.toString(),Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoanActivity.this,error.toString(),Toast.LENGTH_LONG).show();
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
                params.put("grant_date", grantDate);
                params.put("repayment_date", repaymentDate);
                params.put("amount", amount);
                params.put("repayment_period", String.valueOf(repaymentPeriod));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(LoanActivity.this);
        requestQueue.add(userRequest);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String fullPeriod = parent.getItemAtPosition(position).toString();
        // extract months
        String[] split = fullPeriod.split("\\s+");
        repaymentPeriod = Integer.parseInt(split[0]);

        //Toast.makeText(parent.getContext(),  , Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    @Override
    public void onClick(View v) {
        if (v == fundDate) {
            final Calendar C = Calendar.getInstance();
            mYear = C.get(Calendar.YEAR);
            mMonth = C.get(Calendar.MONTH);
            mDay = C.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog mDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    fundDate.setText(year + "/" + (month + 1 + "/" + day));
                }
            }, mYear, mMonth, mDay);
            mDatePickerDialog.show();
        } else {

            if (v == repayment_start_date) {
                final Calendar C = Calendar.getInstance();
                mYear = C.get(Calendar.YEAR);
                mMonth = C.get(Calendar.MONTH);
                mDay = C.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        repayment_start_date.setText(year + "/" + (month + 1 + "/" + day));
                    }
                }, mYear, mMonth, mDay);
                mDatePickerDialog.show();
            }
        }
    }




}